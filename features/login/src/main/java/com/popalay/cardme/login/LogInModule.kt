package com.popalay.cardme.login

import com.popalay.cardme.login.usecase.ValidatePhoneNumberUseCase
import org.koin.android.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module
import org.koin.dsl.path.moduleName

object LogInModule {

    fun get() = module(LogInModule::class.moduleName) {
        viewModel { LogInViewModel(get()) }
        single { ValidatePhoneNumberUseCase() }
    }
}