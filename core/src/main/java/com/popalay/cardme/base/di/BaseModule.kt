package com.popalay.cardme.base.di

import com.popalay.cardme.api.error.ErrorHandler
import com.popalay.cardme.base.error.DefaultErrorHandler
import org.koin.dsl.module.module
import org.koin.dsl.path.moduleName

object BaseModule {

    fun get() = module(BaseModule::class.moduleName) {
        single { DefaultErrorHandler() as ErrorHandler }
    }
}