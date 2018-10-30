package com.popalay.cardme.main

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.NavHost
import androidx.navigation.fragment.findNavController
import com.google.android.gms.instantapps.InstantApps
import com.jakewharton.rxbinding2.view.RxView
import com.popalay.cardme.api.core.error.ErrorHandler
import com.popalay.cardme.api.ui.navigation.NavigatorHolder
import com.popalay.cardme.core.extensions.applyThrottling
import com.popalay.cardme.core.extensions.bindView
import com.popalay.cardme.core.extensions.loadImage
import com.popalay.cardme.core.extensions.px
import com.popalay.cardme.core.picasso.CircleBorderedImageTransformation
import com.popalay.cardme.core.state.BindableMviView
import com.popalay.cardme.core.widget.ProgressImageButton
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.getViewModel
import org.koin.core.parameter.parametersOf
import kotlin.properties.Delegates

internal class MainFragment : Fragment(), NavHost, BindableMviView<MainViewState, MainIntent> {

    private val buttonAddCard: View by bindView(R.id.button_add_card)
    private val constraintLayout: ConstraintLayout by bindView(R.id.constraint_layout)
    private val imageUserPhoto: ImageView by bindView(R.id.image_user_photo)
    private val buttonSync: ProgressImageButton by bindView(R.id.button_sync)
    private val buttonInstall: ProgressImageButton by bindView(R.id.button_install)

    private val intentSubject = PublishSubject.create<MainIntent>()
    private val errorHandler: ErrorHandler by inject()
    private val navigatorHolder: NavigatorHolder by inject()
    private var state: MainViewState by Delegates.notNull()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
        inflater.inflate(R.layout.main_fragment, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navigatorHolder.navigator = MainNavigator(this)
        loadModule()
        bind(getViewModel<MainViewModel> { parametersOf(this) })
        initView()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        super.onActivityResult(requestCode, resultCode, data)
        intentSubject.onNext(MainIntent.OnActivityResult(resultCode == Activity.RESULT_OK, requestCode, data))
    }

    override fun getNavController(): NavController = findNavController()

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
            imageUserPhoto.apply {
                isVisible = user != null
                loadImage(
                    user?.photoUrl,
                    R.drawable.ic_holder_placeholder,
                    CircleBorderedImageTransformation(2.px.toFloat(), ContextCompat.getColor(requireContext(), R.color.colorAccent))
                )
            }
            buttonSync.apply {
                setImageResource(user?.let { R.drawable.ic_exit } ?: R.drawable.ic_sync)
                isProgress = isSyncProgress
            }

            errorHandler.accept(error)
        }
    }

    private val syncClickedIntent
        get() = RxView.clicks(buttonSync)
            .applyThrottling()
            .map { state.user?.let { MainIntent.OnUnsyncClicked } ?: MainIntent.OnSyncClicked }

    private val userClickedIntent
        get() = RxView.clicks(imageUserPhoto)
            .applyThrottling()
            .map { MainIntent.OnUserClicked }

    private val addCardClickedIntent
        get() = RxView.clicks(buttonAddCard)
            .applyThrottling()
            .map { MainIntent.OnAddCardClicked }

    private fun initView() {
        buttonInstall.apply {
            isVisible = com.google.android.gms.common.wrappers.InstantApps.isInstantApp(context)
            setOnClickListener {
                val intent = Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://mecard.page.link")
                ).addCategory(Intent.CATEGORY_BROWSABLE)
                InstantApps.showInstallPrompt(requireActivity(), intent, 0, null)
            }
        }
    }
}