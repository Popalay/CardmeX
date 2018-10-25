package com.popalay.cardme.core.error

import com.popalay.cardme.BuildConfig
import com.popalay.cardme.api.core.error.ErrorHandler

class DefaultErrorHandler : ErrorHandler {

    override fun accept(throwable: Throwable?) {
        if (BuildConfig.DEBUG) {
            throwable?.run { throw this }
        }
    }
}