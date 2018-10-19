package com.popalay.cardme.main

import com.popalay.cardme.core.usecase.LogOutUseCase
import com.popalay.cardme.main.usecase.AuthUseCase
import com.popalay.cardme.main.usecase.HandleAuthResultUseCase
import org.koin.androidx.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module

object MainModule {

    fun get() = module("MainModule") {
        viewModel { MainViewModel(get(), get(), get { it }, get { it }, get(), get()) }
        single { LogOutUseCase(get()) }
        single { AuthUseCase(get { it }, get()) }
        single { HandleAuthResultUseCase(get { it }, get()) }
    }
}