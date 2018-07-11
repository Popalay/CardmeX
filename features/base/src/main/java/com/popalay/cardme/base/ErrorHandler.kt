package com.popalay.cardme.base

import io.reactivex.functions.Consumer

typealias ErrorHandler = Consumer<Throwable?>

class DefaultErrorHandler : ErrorHandler {

    override fun accept(t: Throwable?) {
        t?.run { throw this }
    }
}