package com.popalay.cardme.core.state

import com.popalay.cardme.api.ui.state.*
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.subjects.PublishSubject

abstract class BaseMviViewModel<S : ViewState, I : Intent> : BaseViewModel(), MviViewModel<S, I> {

    override val states: Observable<S> by lazy(LazyThreadSafetyMode.NONE) {
        intentsSubject
            .hide()
            .compose(processor)
            .observeOn(AndroidSchedulers.mainThread())
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