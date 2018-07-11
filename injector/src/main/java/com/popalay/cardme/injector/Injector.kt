package com.popalay.cardme.injector

import android.app.Application
import com.popalay.cardme.base.DefaultErrorHandler
import com.popalay.cardme.base.ErrorHandler
import com.popalay.cardme.login.LogInViewModel
import com.popalay.cardme.login.usecase.ValidatePhoneNumberUseCase
import org.koin.android.architecture.ext.viewModel
import org.koin.android.ext.android.startKoin
import org.koin.dsl.module.Module
import org.koin.dsl.module.applicationContext

object Injector {

    fun inject(app: Application) {
        app.startKoin(listOf(mainModule, logInModule))
    }

    private val mainModule: Module = applicationContext {
        bean { DefaultErrorHandler() as ErrorHandler }
    }

    private val logInModule: Module = applicationContext {
        viewModel { LogInViewModel(get()) }
        bean { ValidatePhoneNumberUseCase() }
    }
}