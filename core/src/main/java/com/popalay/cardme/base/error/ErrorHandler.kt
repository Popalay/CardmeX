package com.popalay.cardme.base.error

import com.popalay.cardme.api.error.ErrorHandler
import com.popalay.cardme.base.BuildConfig

class DefaultErrorHandler : ErrorHandler {

    override fun accept(throwable: Throwable?) {
        if (BuildConfig.DEBUG) {
            throwable?.run { throw this }
        }
    }
}