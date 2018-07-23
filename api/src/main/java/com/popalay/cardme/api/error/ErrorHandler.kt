package com.popalay.cardme.api.error

import io.reactivex.functions.Consumer

interface ErrorHandler : Consumer<Throwable?> {

    override fun accept(throwable: Throwable?)
}