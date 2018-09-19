package com.popalay.cardme.api.state

import com.popalay.cardme.api.usecase.UseCase
import io.reactivex.Observable

class IntentProcessor<I : Intent>(
    private val block: (sharedIntents: Observable<I>) -> List<Observable<out UseCase.Result>>
) : Processor<I> {

    override fun apply(intents: Observable<I>): Observable<UseCase.Result> = intents.publish { Observable.merge(block(it)) }
}