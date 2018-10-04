package com.popalay.cardme.addcard

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.DialogInterface
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.AutoTransition
import androidx.transition.TransitionManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.jakewharton.rxbinding2.view.RxView
import com.jakewharton.rxbinding2.widget.RxCompoundButton
import com.jakewharton.rxbinding2.widget.RxTextView
import com.popalay.cardme.addcard.adapter.UserListAdapter
import com.popalay.cardme.addcard.model.UserListItem
import com.popalay.cardme.api.core.error.ErrorHandler
import com.popalay.cardme.core.adapter.SpacingItemDecoration
import com.popalay.cardme.core.extensions.*
import com.popalay.cardme.core.picasso.CircleImageTransformation
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

    private val imageFace: ImageView by bindView(R.id.image_face)
    private val constraintLayout: ConstraintLayout by bindView(R.id.constraint_layout)
    private val imageCardType: ImageView by bindView(R.id.image_card_type)
    private val inputNumber: EditText by bindView(R.id.input_number)
    private val buttonCamera: ImageButton by bindView(R.id.button_camera)
    private val buttonPeople: ProgressImageButton by bindView(R.id.button_people)
    private val buttonSave: ProgressMaterialButton by bindView(R.id.button_save)
    private val inputName: EditText by bindView(R.id.input_name)
    private val checkPublic: CheckBox by bindView(R.id.check_public)
    private val listUsers: RecyclerView by bindView(R.id.list_users)

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
                peopleClickedIntent,
                userClickedIntent,
                isPublicChangedIntent
            )
        )
    }

    override fun accept(viewState: AddCardViewState) {
        state = viewState
        with(viewState) {
            if (saved) dismissAllowingStateLoss()
            buttonSave.apply {
                isEnabled = isValid
                isProgress = saveProgress
            }
            inputName.apply {
                setTextIfNeeded(selectedUser?.displayName?.value ?: holderName)
                isEnabled = isHolderNameEditable
            }
            inputNumber.apply {
                setTextIfNeeded(selectedUser?.card?.number ?: cardNumber)
                isEnabled = isCardNumberEditable
            }
            checkPublic.apply {
                isChecked = isPublic
                isEnabled = isPublicEditable
            }
            buttonPeople.apply {
                TransitionManager.beginDelayedTransition(view as ViewGroup, AutoTransition().addTarget(this))
                setImageResource(if (showClearButton) R.drawable.ic_clear else R.drawable.ic_people)
                isVisible = isHolderNameEditable || showClearButton
                isProgress = peopleProgress
                isSelected = !users.isNullOrEmpty()
                drawable.setTint(if (isSelected) Color.WHITE else context.getColor(R.color.colorPrimary))
            }

            TransitionManager.beginDelayedTransition(view as ViewGroup)
            imageCardType.setImageResource(cardType.icon.takeIf { it != 0 } ?: R.drawable.ic_credit_card)
            usersAdapter.submitList(users?.map(::UserListItem))
            imageFace.apply {
                imageTintMode = selectedUser?.let { PorterDuff.Mode.DST } ?: PorterDuff.Mode.MULTIPLY
                loadImage(selectedUser?.photoUrl, R.drawable.ic_account, CircleImageTransformation())
            }

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
                    state.cardNumber,
                    state.holderName,
                    state.isPublic,
                    state.cardType,
                    state.selectedUser
                )
            }

    private val cameraClickedIntent
        get() = RxView.clicks(buttonCamera)
            .applyThrottling()
            .map { AddCardIntent.CameraClicked }

    private val peopleClickedIntent
        get() = RxView.clicks(buttonPeople)
            .applyThrottling()
            .map { if (state.showClearButton) AddCardIntent.CrossClicked else AddCardIntent.PeopleClicked(!state.users.isNullOrEmpty()) }

    private val nameChangedIntent
        get() = RxTextView.afterTextChangeEvents(inputName)
            .applyThrottling()
            .map { AddCardIntent.NameChanged(it.editable().toString()) }

    private val numberChangedIntent
        get() = RxTextView.afterTextChangeEvents(inputNumber)
            .applyThrottling()
            .map { AddCardIntent.NumberChanged(it.editable().toString()) }

    private val isPublicChangedIntent
        get() = RxCompoundButton.checkedChanges(checkPublic)
            .map { AddCardIntent.IsPublicChanged(it) }

    private val userClickedIntent
        get() = usersAdapter.itemClickObservable
            .map { AddCardIntent.OnUserClicked(it.user) }

    private fun initView() {
        listUsers.apply {
            adapter = usersAdapter
            addItemDecoration(SpacingItemDecoration(8.px, betweenItems = true))
        }
    }
}