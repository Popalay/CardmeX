package com.popalay.cardme.main

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.navigation.Navigation
import com.gojuno.koptional.None
import com.gojuno.koptional.Some
import com.jakewharton.rxbinding2.view.RxView
import com.popalay.cardme.api.error.ErrorHandler
import com.popalay.cardme.api.state.MviView
import com.popalay.cardme.base.extensions.bindView
import com.popalay.cardme.base.picasso.CircleImageTransformation
import com.popalay.cardme.base.widget.ProgressMaterialButton
import com.squareup.picasso.Picasso
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.schedulers.Schedulers
import org.koin.android.ext.android.inject
import org.koin.android.scope.ext.android.scopedWith
import org.koin.android.viewmodel.ext.android.getViewModel
import org.koin.dsl.path.moduleName
import java.util.concurrent.TimeUnit

internal class MainFragment : Fragment(), MviView<MainViewState, MainIntent> {

    private val textUserDisplayName: TextView by bindView(R.id.text_user_display_name)
    private val imageUserPhoto: ImageView by bindView(R.id.image_user_photo)
    private val buttonAddCard: ProgressMaterialButton by bindView(R.id.button_add_card)
    private val buttonUnsync: ProgressMaterialButton by bindView(R.id.button_unsync)
    private val buttonSync: ProgressMaterialButton by bindView(R.id.button_sync)

    private val disposables = CompositeDisposable()
    private val errorHandler: ErrorHandler by inject()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
        inflater.inflate(R.layout.main_fragment, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val viewModel = getViewModel<MainViewModel>()

        disposables += viewModel.states
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(this, errorHandler)

        disposables += intents
            .delaySubscription(viewModel.states.observeOn(AndroidSchedulers.mainThread()))
            .subscribe(viewModel, errorHandler)

        scopedWith(MainModule::class.moduleName)

        buttonSync.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_from_main_to_log_in))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        disposables.clear()
    }

    override val intents: Observable<MainIntent> = Observable.defer {
        Observable.merge(
            Observable.just(MainIntent.OnStarted),
            unsyncClickedIntent
        )
    }

    override fun accept(viewState: MainViewState) {
        with(viewState) {
            user.toNullable()?.run {
                Picasso.get()
                    .load(photoUrl)
                    .transform(CircleImageTransformation())
                    .into(imageUserPhoto)
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
            .throttleLast(500L, TimeUnit.MILLISECONDS, Schedulers.computation())
            .map { MainIntent.OnUnsyncClicked }
}