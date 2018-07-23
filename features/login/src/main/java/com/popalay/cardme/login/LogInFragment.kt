package com.popalay.cardme.login

import android.app.Activity
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
import com.popalay.cardme.api.error.ErrorHandler
import com.popalay.cardme.core.extensions.bindView
import com.popalay.cardme.core.extensions.loadImage
import com.popalay.cardme.core.picasso.CircleImageTransformation
import com.popalay.cardme.core.state.BindableMviView
import com.popalay.cardme.core.widget.ProgressMaterialButton
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import org.koin.android.ext.android.inject
import org.koin.android.scope.ext.android.scopedWith
import org.koin.android.viewmodel.ext.android.getViewModel
import org.koin.core.parameter.parametersOf
import org.koin.dsl.path.moduleName
import java.util.concurrent.TimeUnit

internal class LogInFragment : Fragment(), BindableMviView<LogInViewState, LogInIntent> {

    private val imageUserPhoto: ImageView by bindView(R.id.image_user_photo)
    private val textUserDisplayName: TextView by bindView(R.id.text_user_display_name)
    private val buttonGoogle: ProgressMaterialButton by bindView(R.id.button_google)

    private val errorHandler: ErrorHandler by inject()

    private val activityResultSubject = PublishSubject.create<LogInIntent.OnActivityResult>().toSerialized()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
        inflater.inflate(R.layout.log_in_fragment, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bind(getViewModel<LogInViewModel> { parametersOf(this) })
        scopedWith(LogInModule::class.moduleName)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        super.onActivityResult(requestCode, resultCode, data)
        activityResultSubject.onNext(LogInIntent.OnActivityResult(resultCode == Activity.RESULT_OK, requestCode, data))
    }

    override val intents: Observable<LogInIntent> = Observable.defer {
        Observable.merge(
            activityResultSubject,
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
            if (showUserInfo) user.toNullable()?.run {
                textUserDisplayName.text = "Hi $displayName!"
                imageUserPhoto.loadImage(photoUrl, CircleImageTransformation())
                if (canStart) findNavController().popBackStack()
            }
            errorHandler.accept(error)
        }
    }

    private val googleSignInClickedIntent
        get() = RxView.clicks(buttonGoogle)
            .throttleLast(500L, TimeUnit.MILLISECONDS, Schedulers.computation())
            .map { LogInIntent.GoogleLogInClicked }
}