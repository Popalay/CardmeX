package com.popalay.cardme.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.NavHost
import androidx.navigation.findNavController
import com.gojuno.koptional.None
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
import org.koin.android.ext.android.inject
import org.koin.androidx.scope.ext.android.scopedWith
import org.koin.androidx.viewmodel.ext.android.getViewModel
import org.koin.dsl.path.moduleName

internal class MainFragment : Fragment(), NavHost, BindableMviView<MainViewState, MainIntent> {

    private val navHostFragment: View by bindView(R.id.nav_host_fragment)
    private val textUserDisplayName: TextView by bindView(R.id.text_user_display_name)
    private val imageUserPhoto: ImageView by bindView(R.id.image_user_photo)
    private val buttonUnsync: ProgressMaterialButton by bindView(R.id.button_unsync)
    private val buttonSync: ProgressMaterialButton by bindView(R.id.button_sync)

    private val errorHandler: ErrorHandler by inject()
    private val navigatorHolder: NavigatorHolder by inject()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
        inflater.inflate(R.layout.main_fragment, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navigatorHolder.navigator = MainNavigator(this)
        bind(getViewModel<MainViewModel>())
        scopedWith(MainModule::class.moduleName)
    }

    override fun getNavController(): NavController = navHostFragment.findNavController()

    override val intents: Observable<MainIntent> = Observable.defer {
        Observable.merge(
            Observable.just(MainIntent.OnStarted),
            unsyncClickedIntent,
            syncClickedIntent
        )
    }

    override fun accept(viewState: MainViewState) {
        with(viewState) {
            user.toNullable()?.run {
                imageUserPhoto.loadImage(photoUrl, CircleImageTransformation())
                textUserDisplayName.text = displayName
            }
            buttonSync.isVisible = user === None
            buttonUnsync.isVisible = user is Some
            textUserDisplayName.isVisible = user is Some
            imageUserPhoto.isVisible = user is Some
            buttonSync.isProgress = isUnsyncProgress
            buttonUnsync.isProgress = isUnsyncProgress

            errorHandler.accept(error)
        }
    }

    private val unsyncClickedIntent
        get() = RxView.clicks(buttonUnsync)
            .applyThrottling()
            .map { MainIntent.OnUnsyncClicked }

    private val syncClickedIntent
        get() = RxView.clicks(buttonSync)
            .applyThrottling()
            .map { MainIntent.OnSyncClicked }
}