package com.popalay.cardme.api.state

import io.reactivex.Observable

interface StateProvider<S : ViewState> {
    val states: Observable<S>
}