package com.popalay.cardme.core.di

import com.popalay.cardme.api.error.ErrorHandler
import com.popalay.cardme.api.navigation.NavigatorHolder
import com.popalay.cardme.api.navigation.Router
import com.popalay.cardme.core.error.DefaultErrorHandler
import com.popalay.cardme.core.mapper.FirebaseUserToUserMapper
import com.popalay.cardme.core.navigation.BaseNavigationHolder
import com.popalay.cardme.core.navigation.BaseRouter
import com.popalay.cardme.core.usecase.SpecificIntentUseCase
import org.koin.dsl.module.module

object CoreModule {

    fun get() = module {
        single<ErrorHandler> { DefaultErrorHandler() }
        single { FirebaseUserToUserMapper() }
        single<NavigatorHolder> { BaseNavigationHolder() }
        single<Router> { BaseRouter(get()) }
        single { SpecificIntentUseCase() }
    }
}