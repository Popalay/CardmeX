package com.popalay.cardme.addcard

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.constraintlayout.widget.Group
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.TransitionManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.jakewharton.rxbinding2.view.RxView
import com.jakewharton.rxbinding2.widget.RxTextView
import com.popalay.cardme.addcard.adapter.UserListAdapter
import com.popalay.cardme.addcard.model.UserListItem
import com.popalay.cardme.api.core.error.ErrorHandler
import com.popalay.cardme.core.adapter.SpacingItemDecoration
import com.popalay.cardme.core.extensions.applyThrottling
import com.popalay.cardme.core.extensions.bindView
import com.popalay.cardme.core.extensions.icon
import com.popalay.cardme.core.extensions.px
import com.popalay.cardme.core.state.BindableMviView
import com.popalay.cardme.core.widget.OnDialogDismissed
import com.popalay.cardme.core.widget.ProgressImageButton
import com.popalay.cardme.core.widget.ProgressMaterialButton
import com.popalay.cardme.core.widget.RoundedBottomSheetDialogFragment
import io.reactivex.Observable
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.getViewModel
import org.koin.core.parameter.parametersOf
import kotlin.properties.Delegates


class AddCardFragment : RoundedBottomSheetDialogFragment(), BindableMviView<AddCardViewState, AddCardIntent> {

    companion object {

        private const val ARG_IS_USER_CARD = "ARG_IS_USER_CARD"

        fun newInstance(isUserCard: Boolean = false) = AddCardFragment().apply {
            arguments = bundleOf(ARG_IS_USER_CARD to isUserCard)
        }
    }

    private val imageCardType: ImageView by bindView(R.id.image_card_type)
    private val inputNumber: EditText by bindView(R.id.input_number)
    private val buttonCamera: ImageButton by bindView(R.id.button_camera)
    private val buttonPeople: ProgressImageButton by bindView(R.id.button_people)
    private val buttonSave: ProgressMaterialButton by bindView(R.id.button_save)
    private val inputName: EditText by bindView(R.id.input_name)
    private val checkPublic: CheckBox by bindView(R.id.check_public)
    private val listUsers: RecyclerView by bindView(R.id.list_users)
    private val groupCardFields: Group by bindView(R.id.group_card_fields)

    private val errorHandler: ErrorHandler by inject()

    private val usersAdapter = UserListAdapter()
    private var state: AddCardViewState by Delegates.notNull()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
        super.onCreateDialog(savedInstanceState).also { dialog ->
            dialog.setOnShowListener {
                val bottomSheet = dialog.findViewById<FrameLayout?>(R.id.design_bottom_sheet)
                val behaviour = BottomSheetBehavior.from(bottomSheet)
                behaviour.setBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
                    @SuppressLint("SwitchIntDef")
                    override fun onStateChanged(bottomSheet: View, newState: Int) {
                        when (newState) {
                            BottomSheetBehavior.STATE_HIDDEN -> dialog.cancel()
                            BottomSheetBehavior.STATE_EXPANDED -> {
                                //TODO: show toolbar
                            }
                        }
                        Log.d("AddCardFragment", "new state $newState")
                    }

                    override fun onSlide(bottomSheet: View, slideOffset: Float) {

                    }
                })
            }
        }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
        inflater.inflate(R.layout.add_card_fragment, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bind(getViewModel<AddCardViewModel> { parametersOf(arguments?.getBoolean(ARG_IS_USER_CARD) ?: false) })
        initView()
    }

    override val intents: Observable<AddCardIntent> = Observable.defer {
        Observable.merge(
            listOf(
                Observable.just(AddCardIntent.OnStart),
                saveClickedIntent,
                nameChangedIntent,
                numberChangedIntent,
                cameraClickedIntent,
                peopleClickedIntent
            )
        )
    }

    override fun accept(viewState: AddCardViewState) {
        state = viewState
        with(viewState) {
            buttonSave.isEnabled = isValid
            buttonSave.isProgress = saveProgress
            checkPublic.isEnabled = isPublicEditable
            inputName.isEnabled = isHolderNameEditable
            buttonPeople.isVisible = isHolderNameEditable
            buttonPeople.isProgress = peopleProgress
            if (holderName.isNotBlank()) inputName.setText(holderName)
            if (saved) dismissAllowingStateLoss()
            TransitionManager.beginDelayedTransition(view as ViewGroup)
            imageCardType.setImageResource(cardType.icon.takeIf { it != 0 } ?: R.drawable.ic_credit_card)
            listUsers.isVisible = users != null
            groupCardFields.isVisible = users == null
            usersAdapter.submitList(users?.map(::UserListItem))
            errorHandler.accept(error)
        }
    }

    override fun onDismiss(dialog: DialogInterface?) {
        super.onDismiss(dialog)
        (parentFragment as? OnDialogDismissed)?.onDialogDismissed()
    }

    private val saveClickedIntent
        get() = RxView.clicks(buttonSave)
            .applyThrottling()
            .map {
                AddCardIntent.SaveClicked(
                    inputNumber.text.toString(),
                    inputName.text.toString(),
                    checkPublic.isChecked,
                    state.cardType
                )
            }

    private val cameraClickedIntent
        get() = RxView.clicks(buttonCamera)
            .applyThrottling()
            .map { AddCardIntent.CameraClicked }

    private val peopleClickedIntent
        get() = RxView.clicks(buttonPeople)
            .applyThrottling()
            .map { AddCardIntent.PeopleClicked }

    private val nameChangedIntent
        get() = RxTextView.afterTextChangeEvents(inputName)
            .skipInitialValue()
            .map {
                AddCardIntent.NameChanged(
                    inputNumber.text.toString(),
                    inputName.text.toString(),
                    checkPublic.isChecked
                )
            }

    private val numberChangedIntent
        get() = RxTextView.afterTextChangeEvents(inputNumber)
            .skipInitialValue()
            .map {
                AddCardIntent.NumberChanged(
                    inputNumber.text.toString(),
                    inputName.text.toString(),
                    checkPublic.isChecked
                )
            }

    private fun initView() {
        listUsers.apply {
            setHasFixedSize(true)
            adapter = usersAdapter
            addItemDecoration(SpacingItemDecoration(8.px, betweenItems = true))
        }
        (dialog as BottomSheetDialog)
    }
}