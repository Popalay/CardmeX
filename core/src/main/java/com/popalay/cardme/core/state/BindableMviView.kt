package com.popalay.cardme.core.state

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import com.popalay.cardme.api.ui.state.Intent
import com.popalay.cardme.api.ui.state.MviView
import com.popalay.cardme.api.ui.state.MviViewModel
import com.popalay.cardme.api.ui.state.ViewState
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign

interface BindableMviView<S : ViewState, I : Intent> : MviView<S, I>, LifecycleOwner {

    fun bind(viewModel: MviViewModel<S, I>) {
        var disposables: CompositeDisposable? = null

        lifecycle.addObserver(object : LifecycleObserver {
            @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
            fun onCreate() {
                disposables?.clear()
                disposables = CompositeDisposable()

                requireNotNull(disposables) += viewModel.states
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this@BindableMviView)

                requireNotNull(disposables) += intents
                    .delaySubscription(viewModel.states.observeOn(AndroidSchedulers.mainThread()))
                    .subscribe(viewModel)
            }

            @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
            fun onDestroy() {
                disposables?.clear()
            }
        })
    }
}