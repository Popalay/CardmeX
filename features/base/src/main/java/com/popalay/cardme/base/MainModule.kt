package com.popalay.cardme.base

import org.koin.dsl.module.applicationContext

object MainModule {

    fun get() = applicationContext {
        bean { DefaultErrorHandler() as ErrorHandler }
    }
}