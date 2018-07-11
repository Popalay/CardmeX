package com.popalay.cardme

import android.app.Application
import com.popalay.cardme.base.DefaultErrorHandler
import com.popalay.cardme.base.ErrorHandler
import com.popalay.cardme.injector.Injector
import com.popalay.cardme.login.LogInViewModel
import com.popalay.cardme.login.usecase.ValidatePhoneNumberUseCase
import org.koin.android.architecture.ext.viewModel
import org.koin.dsl.module.Module
import org.koin.dsl.module.applicationContext

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        Injector.inject(this)
    }
}