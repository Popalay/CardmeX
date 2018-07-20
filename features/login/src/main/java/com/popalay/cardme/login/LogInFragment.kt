package com.popalay.cardme.login

import android.content.Intent
import android.os.Bundle
import android.support.transition.TransitionManager
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import com.gojuno.koptional.None
import com.gojuno.koptional.Some
import com.jakewharton.rxbinding2.view.RxView
import com.popalay.cardme.base.ErrorHandler
import com.popalay.cardme.base.extensions.bindView
import com.popalay.cardme.base.state.MviView
import com.popalay.cardme.base.widget.ProgressMaterialButton
import com.squareup.picasso.Picasso
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import org.koin.android.ext.android.inject
import org.koin.android.scope.ext.android.scopedWith
import org.koin.android.viewmodel.ext.android.getViewModel
import org.koin.core.parameter.parametersOf
import org.koin.dsl.path.moduleName
import java.util.concurrent.TimeUnit

class LogInFragment : Fragment(), MviView<LogInViewState, LogInIntent> {

    private val buttonSync: ProgressMaterialButton by bindView(R.id.button_sync)
    private val imageUserPhoto: ImageView by bindView(R.id.image_user_photo)
    private val textUserDisplayName: TextView by bindView(R.id.text_user_display_name)
    private val buttonGoogle: ProgressMaterialButton by bindView(R.id.button_google)

    private val disposables = CompositeDisposable()
    private val errorHandler: ErrorHandler by inject()

    private val activityResultSubject = PublishSubject.create<LogInIntent.OnActivityResult>().toSerialized()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
        inflater.inflate(R.layout.log_in_fragment, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val viewModel = getViewModel<LogInViewModel> { parametersOf(this) }

        disposables += viewModel.states
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(this, errorHandler)

        disposables += intents
            .delaySubscription(viewModel.states.observeOn(AndroidSchedulers.mainThread()))
            .subscribe(viewModel, errorHandler)

        buttonSync.setOnClickListener { findNavController().popBackStack() }

        scopedWith(LogInModule::class.moduleName)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        super.onActivityResult(requestCode, resultCode, data)
        activityResultSubject.onNext(LogInIntent.OnActivityResult(requestCode, data))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        disposables.clear()
    }

    override val intents: Observable<LogInIntent> = Observable.defer {
        Observable.merge(
            onActivityResultIntent,
            googleSignInClickedIntent
        )
    }

    override fun accept(viewState: LogInViewState) {
        with(viewState) {
            TransitionManager.beginDelayedTransition(view as ViewGroup)
            buttonGoogle.isVisible = user === None
            buttonGoogle.isProgress = isProgress
            textUserDisplayName.isVisible = user is Some
            imageUserPhoto.isVisible = user is Some
            buttonSync.isVisible = user is Some
            user.toNullable()?.run {
                textUserDisplayName.text = "Hi $displayName!"
                Picasso.get().load(photoUrl).into(imageUserPhoto)
            }
            errorHandler.accept(error)
        }
    }

    private val googleSignInClickedIntent
        get() = RxView.clicks(buttonGoogle)
            .throttleLast(500L, TimeUnit.MILLISECONDS, Schedulers.computation())
            .map { LogInIntent.GoogleLogInClicked }

    private val onActivityResultIntent: Observable<LogInIntent.OnActivityResult> get() = activityResultSubject
}