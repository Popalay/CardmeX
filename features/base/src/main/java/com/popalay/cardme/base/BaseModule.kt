package com.popalay.cardme.base

import org.koin.dsl.module.module
import org.koin.dsl.path.moduleName

object BaseModule {

    fun get() = module(BaseModule::class.moduleName) {
        single { DefaultErrorHandler() as ErrorHandler }
    }
}