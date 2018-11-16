package com.popalay.cardme.addcard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.*
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import androidx.core.widget.ContentLoadingProgressBar
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.AutoTransition
import androidx.transition.TransitionManager
import com.google.android.material.appbar.AppBarLayout
import com.jakewharton.rxbinding2.view.RxMenuItem
import com.jakewharton.rxbinding2.view.RxView
import com.jakewharton.rxbinding2.widget.RxCompoundButton
import com.jakewharton.rxbinding2.widget.RxTextView
import com.popalay.cardme.addcard.adapter.UserListAdapter
import com.popalay.cardme.addcard.model.UserListItem
import com.popalay.cardme.api.core.error.ErrorHandler
import com.popalay.cardme.api.ui.navigation.NavigatorHolder
import com.popalay.cardme.core.adapter.SpacingItemDecoration
import com.popalay.cardme.core.extensions.*
import com.popalay.cardme.core.state.BindableMviView
import io.reactivex.Observable
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.getViewModel
import org.koin.core.parameter.parametersOf
import kotlin.properties.Delegates

internal class AddCardFragment : Fragment(), BindableMviView<AddCardViewState, AddCardIntent> {

    private val progressBar: ContentLoadingProgressBar by bindView(R.id.progress_bar)
    private val toolbar: Toolbar by bindView(R.id.toolbar)
    private val appbar: AppBarLayout by bindView(R.id.appbar)
    private val imageFace: ImageView by bindView(R.id.image_face)
    private val constraintLayout: ConstraintLayout by bindView(R.id.constraint_layout)
    private val imageCardType: ImageView by bindView(R.id.image_card_type)
    private val inputNumber: EditText by bindView(R.id.input_number)
    private val buttonCamera: ImageButton by bindView(R.id.button_camera)
    private val buttonCross: ImageButton by bindView(R.id.button_cross)
    private val inputName: EditText by bindView(R.id.input_name)
    private val checkPublic: CheckBox by bindView(R.id.check_public)
    private val listUsers: RecyclerView by bindView(R.id.list_users)

    private val errorHandler: ErrorHandler by inject()

    private val navigatorHolder: NavigatorHolder by inject()
    private val usersAdapter = UserListAdapter()
    private var state: AddCardViewState by Delegates.notNull()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
        inflater.inflate(R.layout.add_card_fragment, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadModule()
        navigatorHolder.navigator = AddCardNavigator(this)
        val args = AddCardFragmentArgs.fromBundle(arguments)
        bind(getViewModel<AddCardViewModel> { parametersOf(args.isUserCard) })
        initView()
    }

    override val intents: Observable<AddCardIntent> = Observable.defer {
        Observable.merge(
            listOf(
                Observable.just(AddCardIntent.OnStart),
                nameChangedIntent,
                numberChangedIntent,
                cameraClickedIntent,
                crossClickedIntent,
                userClickedIntent,
                isPublicChangedIntent,
                doneActionClickedIntent,
                saveClickedIntent
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
                setTextIfNeeded(holderName)
                isEnabled = isHolderNameEditable
            }
            inputNumber.apply {
                setTextIfNeeded(cardNumber)
                isEnabled = isCardNumberEditable
            }
            checkPublic.apply {
                isChecked = isPublic
                isEnabled = isPublicEditable
            }
            if (peopleProgress) progressBar.show() else progressBar.hide()
            TransitionManager.beginDelayedTransition(
                view as ViewGroup,
                AutoTransition().excludeChildren(listUsers, true)
            )
            buttonCross.apply {
                isVisible = showClearButton
            }

            imageCardType.setImageResource(cardType.icon.takeIf { it != 0 } ?: R.drawable.ic_credit_card)
            usersAdapter.submitList(users?.map { UserListItem(it, it == requestedUser) })
            toastMessage?.let { Toast.makeText(context, it, Toast.LENGTH_LONG).show() }

            errorHandler.accept(error)
        }
    }

    private val doneActionClickedIntent
        get() = RxTextView.editorActions(inputName) { it == EditorInfo.IME_ACTION_DONE && state.isValid }
            .map { createSaveIntent() }

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
            .filter { !it.isProgress }
            .applyThrottling()
            .map { AddCardIntent.OnUserClicked(it.user) }

    private val saveClickedIntent
        get() = RxMenuItem.clicks(toolbar.menu.findItem(R.id.action_save))
            .applyThrottling()
            .map { createSaveIntent() }

    private fun createSaveIntent() = with(state) {
        AddCardIntent.SaveClicked(
            cardNumber,
            holderName,
            isPublic,
            cardType
        )
    }

    private fun initView() {
        NavigationUI.setupWithNavController(toolbar, findNavController())
        toolbar.inflateMenu(R.menu.add_card_menu)
        listUsers.apply {
            adapter = usersAdapter
            addItemDecoration(SpacingItemDecoration(8.px, betweenItems = true))
        }
    }
}