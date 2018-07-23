package com.popalay.cardme.base.state

import com.popalay.cardme.api.state.Intent
import com.popalay.cardme.api.state.MviViewModel
import com.popalay.cardme.api.state.Processor
import com.popalay.cardme.api.state.Reducer
import com.popalay.cardme.api.state.ViewState
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject

abstract class BaseMviViewModel<S : ViewState, I : Intent> : BaseViewModel(), MviViewModel<S, I> {

    override val states: Observable<S> by lazy(LazyThreadSafetyMode.NONE) {
        intentsSubject
            .hide()
            .observeOn(Schedulers.computation())
            .compose(processor)
            .scan(initialState, reducer)
            .distinctUntilChanged()
            .replay(1)
            .autoConnect(1) { disposables.add(it) }
    }

    protected abstract val initialState: S

    protected abstract val processor: Processor<I>

    protected abstract val reducer: Reducer<S>

    private val intentsSubject: PublishSubject<I> = PublishSubject.create<I>()

    override fun accept(intent: I) {
        intentsSubject.onNext(intent)
    }
}