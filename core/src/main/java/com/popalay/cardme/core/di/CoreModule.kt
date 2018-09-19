package com.popalay.cardme.core.di

import com.popalay.cardme.api.core.error.ErrorHandler
import com.popalay.cardme.api.ui.navigation.NavigatorHolder
import com.popalay.cardme.api.ui.navigation.Router
import com.popalay.cardme.core.error.DefaultErrorHandler
import com.popalay.cardme.core.mapper.FirebaseUserToUserMapper
import com.popalay.cardme.core.navigation.BaseNavigationHolder
import com.popalay.cardme.core.navigation.BaseRouter
import com.popalay.cardme.core.usecase.GetCurrentUserUseCase
import com.popalay.cardme.core.usecase.SpecificIntentUseCase
import org.koin.dsl.module.module

object CoreModule {

    fun get() = module {
        single<ErrorHandler> { DefaultErrorHandler() }
        single { FirebaseUserToUserMapper() }
        single<NavigatorHolder> { BaseNavigationHolder() }
        single<Router> { BaseRouter(get()) }
        single { SpecificIntentUseCase() }
        single { GetCurrentUserUseCase(get()) }
    }
}