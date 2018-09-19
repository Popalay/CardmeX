package com.popalay.cardme.api.ui.state

import io.reactivex.Observable

interface StateProvider<S : ViewState> {
    val states: Observable<S>
}