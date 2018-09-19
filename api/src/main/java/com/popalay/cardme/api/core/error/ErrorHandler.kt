package com.popalay.cardme.api.core.error

import io.reactivex.functions.Consumer

interface ErrorHandler : Consumer<Throwable?> {

    override fun accept(throwable: Throwable?)
}