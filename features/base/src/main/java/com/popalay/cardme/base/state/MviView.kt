package com.popalay.cardme.base.state

import io.reactivex.Observable
import io.reactivex.functions.Consumer

interface MviView<S : ViewState, I : Intent> : Consumer<S> {

    val intents: Observable<I>
}