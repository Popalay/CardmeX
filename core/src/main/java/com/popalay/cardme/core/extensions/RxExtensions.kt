package com.popalay.cardme.core.extensions

import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

fun <T> Observable<T>.applyThrottling(): Observable<T> =
    throttleLast(500L, TimeUnit.MILLISECONDS, Schedulers.computation())