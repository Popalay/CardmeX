package com.popalay.cardme.main

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.NavHost
import androidx.navigation.Navigation
import androidx.transition.AutoTransition
import androidx.transition.TransitionManager
import com.jakewharton.rxbinding2.view.RxView
import com.popalay.cardme.addcard.AddCardFragment
import com.popalay.cardme.api.core.error.ErrorHandler
import com.popalay.cardme.api.ui.navigation.NavigatorHolder
import com.popalay.cardme.core.extensions.applyThrottling
import com.popalay.cardme.core.extensions.bindView
import com.popalay.cardme.core.extensions.loadImage
import com.popalay.cardme.core.picasso.CircleImageTransformation
import com.popalay.cardme.core.state.BindableMviView
import com.popalay.cardme.core.widget.OnDialogDismissed
import com.popalay.cardme.core.widget.ProgressMaterialButton
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.getViewModel
import org.koin.core.parameter.parametersOf
import kotlin.properties.Delegates

internal class MainFragment : Fragment(), NavHost, BindableMviView<MainViewState, MainIntent>, OnDialogDismissed {

    private val buttonAddCard: View by bindView(R.id.button_add_card)
    private val layoutUser: View by bindView(R.id.layout_user)
    private val constraintLayout: ConstraintLayout by bindView(R.id.constraint_layout)
    private val textUserDisplayName: TextView by bindView(R.id.text_user_display_name)
    private val imageUserPhoto: ImageView by bindView(R.id.image_user_photo)
    private val buttonSync: ProgressMaterialButton by bindView(R.id.button_sync)

    private val intentSubject = PublishSubject.create<MainIntent>()
    private val errorHandler: ErrorHandler by inject()
    private val navigatorHolder: NavigatorHolder by inject()
    private var state: MainViewState by Delegates.notNull()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
        inflater.inflate(R.layout.main_fragment, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navigatorHolder.navigator = MainNavigator(this)
        bind(getViewModel<MainViewModel> { parametersOf(this) })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        super.onActivityResult(requestCode, resultCode, data)
        intentSubject.onNext(MainIntent.OnActivityResult(resultCode == Activity.RESULT_OK, requestCode, data))
    }

    override fun getNavController(): NavController = Navigation.findNavController(view!!)

    override val intents: Observable<MainIntent> = Observable.defer {
        Observable.merge(
            listOf(
                Observable.just(MainIntent.OnStarted),
                intentSubject,
                syncClickedIntent,
                userClickedIntent,
                addCardClickedIntent
            )
        )
    }

    override fun accept(viewState: MainViewState) {
        state = viewState
        with(viewState) {
            if (showAddCardDialog) showAddCardDialog()
            TransitionManager.beginDelayedTransition(
                buttonSync.parent as ViewGroup, AutoTransition()
                    .addTarget(buttonSync)
                    .addTarget(textUserDisplayName)
                    .addTarget(imageUserPhoto)
            )
            textUserDisplayName.apply {
                isVisible = user != null
                text = user?.displayName?.value
            }
            imageUserPhoto.apply {
                isVisible = user != null
                loadImage(user?.photoUrl, R.drawable.ic_holder_placeholder, CircleImageTransformation())
            }
            buttonSync.apply {
                text = user?.let { "Unsync" } ?: "Sync"
                isProgress = isSyncProgress
            }

            errorHandler.accept(error)
        }
    }

    override fun onDialogDismissed() {
        intentSubject.onNext(MainIntent.OnAddCardDialogDismissed)
    }

    private fun showAddCardDialog() {
        if (childFragmentManager.findFragmentByTag(AddCardFragment::class.java.simpleName) == null) {
            AddCardFragment.newInstance().showNow(childFragmentManager, AddCardFragment::class.java.simpleName)
        }
    }

    private val syncClickedIntent
        get() = RxView.clicks(buttonSync)
            .applyThrottling()
            .map { state.user?.let { MainIntent.OnUnsyncClicked } ?: MainIntent.OnSyncClicked }

    private val userClickedIntent
        get() = RxView.clicks(layoutUser)
            .applyThrottling()
            .map { MainIntent.OnUserClicked }

    private val addCardClickedIntent
        get() = RxView.clicks(buttonAddCard)
            .applyThrottling()
            .map { MainIntent.OnAddCardClicked }
}