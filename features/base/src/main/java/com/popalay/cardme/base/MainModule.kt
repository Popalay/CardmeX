package com.popalay.cardme.base

import org.koin.dsl.module.module
import org.koin.dsl.path.moduleName

object MainModule {

    fun get() = module(MainModule::class.moduleName) {
        single { DefaultErrorHandler() as ErrorHandler }
    }
}