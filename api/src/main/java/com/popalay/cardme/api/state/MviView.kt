package com.popalay.cardme.api.state

import io.reactivex.Observable
import io.reactivex.functions.Consumer

interface MviView<S : ViewState, I : Intent> : Consumer<S> {

    val intents: Observable<I>

    override fun accept(viewState: S)
}