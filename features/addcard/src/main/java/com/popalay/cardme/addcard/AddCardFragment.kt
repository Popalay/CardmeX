package com.popalay.cardme.addcard

import android.graphics.PorterDuff
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.core.widget.ContentLoadingProgressBar
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.TransitionManager
import com.google.android.material.appbar.AppBarLayout
import com.jakewharton.rxbinding2.view.RxView
import com.jakewharton.rxbinding2.widget.RxCompoundButton
import com.jakewharton.rxbinding2.widget.RxTextView
import com.popalay.cardme.addcard.adapter.UserListAdapter
import com.popalay.cardme.addcard.model.UserListItem
import com.popalay.cardme.api.core.error.ErrorHandler
import com.popalay.cardme.api.ui.navigation.NavigatorHolder
import com.popalay.cardme.core.adapter.SpacingItemDecoration
import com.popalay.cardme.core.extensions.*
import com.popalay.cardme.core.picasso.CircleImageTransformation
import com.popalay.cardme.core.state.BindableMviView
import com.popalay.cardme.core.widget.ProgressImageButton
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.getViewModel
import org.koin.core.parameter.parametersOf
import kotlin.properties.Delegates


class AddCardFragment : Fragment(), BindableMviView<AddCardViewState, AddCardIntent> {

    companion object {

        private const val ARG_IS_USER_CARD = "ARG_IS_USER_CARD"

        fun newInstance(isUserCard: Boolean = false) = AddCardFragment().apply {
            arguments = bundleOf(ARG_IS_USER_CARD to isUserCard)
        }
    }

    private val progressBar: ContentLoadingProgressBar by bindView(R.id.progress_bar)
    private val toolbar: Toolbar by bindView(R.id.toolbar)
    private val appbar: AppBarLayout by bindView(R.id.appbar)
    private val imageFace: ImageView by bindView(R.id.image_face)
    private val constraintLayout: ConstraintLayout by bindView(R.id.constraint_layout)
    private val imageCardType: ImageView by bindView(R.id.image_card_type)
    private val inputNumber: EditText by bindView(R.id.input_number)
    private val buttonCamera: ImageButton by bindView(R.id.button_camera)
    private val buttonCross: ProgressImageButton by bindView(R.id.button_cross)
    private val inputName: EditText by bindView(R.id.input_name)
    private val checkPublic: CheckBox by bindView(R.id.check_public)
    private val listUsers: RecyclerView by bindView(R.id.list_users)

    private val errorHandler: ErrorHandler by inject()

    private val navigatorHolder: NavigatorHolder by inject()
    private val usersAdapter = UserListAdapter()
    private var state: AddCardViewState by Delegates.notNull()
    private val intentSubject = PublishSubject.create<AddCardIntent>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
        inflater.inflate(R.layout.add_card_fragment_full, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navigatorHolder.navigator = AddCardNavigator(this)
        bind(getViewModel<AddCardViewModel> { parametersOf(arguments?.getBoolean(ARG_IS_USER_CARD) ?: false) })
        initView()
    }

    override val intents: Observable<AddCardIntent> = Observable.defer {
        Observable.merge(
            listOf(
                Observable.just(AddCardIntent.OnStart),
                nameChangedIntent,
                numberChangedIntent,
                intentSubject,
                cameraClickedIntent,
                crossClickedIntent,
                userClickedIntent,
                isPublicChangedIntent
            )
        )
    }

    override fun accept(viewState: AddCardViewState) {
        state = viewState
        with(viewState) {
            toolbar.menu.findItem(R.id.action_save)?.apply {
                isEnabled = isValid
                icon.alpha = if (isValid) 255 else 64
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
            if (peopleProgress) progressBar.show() else progressBar.hide()
            TransitionManager.beginDelayedTransition(view as ViewGroup)
            buttonCross.apply {
                isVisible = showClearButton
            }

            imageCardType.setImageResource(cardType.icon.takeIf { it != 0 } ?: R.drawable.ic_credit_card)
            usersAdapter.submitList(users?.map(::UserListItem))
            imageFace.apply {
                imageTintMode = selectedUser?.let { PorterDuff.Mode.DST } ?: PorterDuff.Mode.MULTIPLY
                loadImage(selectedUser?.photoUrl, R.drawable.ic_account, CircleImageTransformation())
            }

            errorHandler.accept(error)
        }
    }

    private val cameraClickedIntent
        get() = RxView.clicks(buttonCamera)
            .applyThrottling()
            .map { AddCardIntent.CameraClicked }

    private val crossClickedIntent
        get() = RxView.clicks(buttonCross)
            .applyThrottling()
            .map { AddCardIntent.CrossClicked }

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
        NavigationUI.setupWithNavController(toolbar, findNavController())
        listUsers.apply {
            adapter = usersAdapter
            addItemDecoration(SpacingItemDecoration(8.px, betweenItems = true))
        }

        toolbar.inflateMenu(R.menu.add_card_menu)
        toolbar.setOnMenuItemClickListener {
            when (it?.itemId) {
                R.id.action_save -> {
                    intentSubject.onNext(
                        AddCardIntent.SaveClicked(
                            state.cardNumber,
                            state.holderName,
                            state.isPublic,
                            state.cardType,
                            state.selectedUser
                        )
                    )
                    true
                }
                else -> false
            }
        }
    }
}