package com.popalay.cardme.main

import com.popalay.cardme.base.usecase.GetCurrentUserUseCase
import com.popalay.cardme.base.usecase.LogOutUseCase
import org.koin.android.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module
import org.koin.dsl.path.moduleName

object MainModule {

    fun get() = module(MainModule::class.moduleName) {
        viewModel { MainViewModel(get(), get()) }
        single { GetCurrentUserUseCase(get()) }
        single { LogOutUseCase() }
    }
}