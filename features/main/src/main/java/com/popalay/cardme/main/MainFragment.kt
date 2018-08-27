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
import com.gojuno.koptional.Some
import com.jakewharton.rxbinding2.view.RxView
import com.popalay.cardme.api.error.ErrorHandler
import com.popalay.cardme.api.navigation.NavigatorHolder
import com.popalay.cardme.core.extensions.applyThrottling
import com.popalay.cardme.core.extensions.bindView
import com.popalay.cardme.core.extensions.loadImage
import com.popalay.cardme.core.picasso.CircleImageTransformation
import com.popalay.cardme.core.state.BindableMviView
import com.popalay.cardme.core.widget.ProgressMaterialButton
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import org.koin.android.ext.android.inject
import org.koin.androidx.scope.ext.android.scopedWith
import org.koin.androidx.viewmodel.ext.android.getViewModel
import org.koin.core.parameter.parametersOf
import org.koin.dsl.path.moduleName
import kotlin.properties.Delegates

internal class MainFragment : Fragment(), NavHost, BindableMviView<MainViewState, MainIntent> {

    private val constraintLayout: ConstraintLayout by bindView(R.id.constraint_layout)
    private val navHostFragment: View by bindView(R.id.nav_host_fragment)
    private val textUserDisplayName: TextView by bindView(R.id.text_user_display_name)
    private val imageUserPhoto: ImageView by bindView(R.id.image_user_photo)
    private val buttonSync: ProgressMaterialButton by bindView(R.id.button_sync)

    private val errorHandler: ErrorHandler by inject()
    private val navigatorHolder: NavigatorHolder by inject()
    private var state: MainViewState by Delegates.notNull()
    private val activityResultSubject = PublishSubject.create<MainIntent.OnActivityResult>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
        inflater.inflate(R.layout.main_fragment, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navigatorHolder.navigator = MainNavigator(this)
        bind(getViewModel<MainViewModel> { parametersOf(this) })
        scopedWith(MainModule::class.moduleName)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        super.onActivityResult(requestCode, resultCode, data)
        activityResultSubject.onNext(MainIntent.OnActivityResult(resultCode == Activity.RESULT_OK, requestCode, data))
    }

    override fun getNavController(): NavController = Navigation.findNavController(view!!)

    override val intents: Observable<MainIntent> = Observable.defer {
        Observable.merge(
            Observable.just(MainIntent.OnStarted),
            syncClickedIntent
        )
    }

    override fun accept(viewState: MainViewState) {
        state = viewState
        with(viewState) {
            TransitionManager.beginDelayedTransition(
                constraintLayout, AutoTransition()
                    .addTarget(buttonSync)
                    .addTarget(textUserDisplayName)
                    .addTarget(imageUserPhoto)
            )
            user.toNullable()?.run {
                imageUserPhoto.loadImage(photoUrl, CircleImageTransformation())
                textUserDisplayName.text = displayName
            }
            buttonSync.text = if (state.user is Some) "Unsync" else "Sync"
            textUserDisplayName.isVisible = user is Some
            imageUserPhoto.isVisible = user is Some
            buttonSync.isProgress = isSyncProgress

            errorHandler.accept(error)
        }
    }

    private val syncClickedIntent
        get() = RxView.clicks(buttonSync)
            .applyThrottling()
            .map { if (state.user is Some) MainIntent.OnUnsyncClicked else MainIntent.OnSyncClicked }
}