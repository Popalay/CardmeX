package com.popalay.cardme.login

import com.popalay.cardme.login.usecase.ValidatePhoneNumberUseCase
import org.koin.android.architecture.ext.viewModel
import org.koin.dsl.module.applicationContext

object LogInModule {

    fun get() = applicationContext {
        viewModel { LogInViewModel(get()) }
        bean { ValidatePhoneNumberUseCase() }
    }
}