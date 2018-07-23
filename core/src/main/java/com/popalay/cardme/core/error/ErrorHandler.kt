package com.popalay.cardme.core.error

import com.popalay.cardme.api.error.ErrorHandler
import com.popalay.cardme.core.BuildConfig

class DefaultErrorHandler : ErrorHandler {

    override fun accept(throwable: Throwable?) {
        if (BuildConfig.DEBUG) {
            throwable?.run { throw this }
        }
    }
}