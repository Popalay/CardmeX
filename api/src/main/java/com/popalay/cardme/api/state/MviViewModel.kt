package com.popalay.cardme.api.state

import io.reactivex.functions.Consumer

interface MviViewModel<S : ViewState, I : Intent> : Consumer<I>, StateProvider<S>