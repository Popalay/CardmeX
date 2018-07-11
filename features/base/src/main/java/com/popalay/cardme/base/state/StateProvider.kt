package com.popalay.cardme.base.state

import io.reactivex.Observable

interface StateProvider<S : ViewState> {
    val states: Observable<S>
}