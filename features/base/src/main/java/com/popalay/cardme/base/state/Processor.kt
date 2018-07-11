package com.popalay.cardme.base.state

import com.popalay.cardme.base.usecase.UseCase
import io.reactivex.Observable
import io.reactivex.ObservableTransformer

interface Processor<I> : ObservableTransformer<I, UseCase.Result>

class IntentProcessor<I : Intent>(
    private val block: (sharedIntents: Observable<I>) -> List<Observable<out UseCase.Result>>
) : Processor<I> {

    override fun apply(intents: Observable<I>): Observable<UseCase.Result> =
        intents.publish { Observable.merge(block(it)) }
}