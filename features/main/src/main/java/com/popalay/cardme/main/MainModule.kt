package com.popalay.cardme.main

import com.popalay.cardme.core.usecase.LogOutUseCase
import com.popalay.cardme.main.usecase.AuthUseCase
import com.popalay.cardme.main.usecase.HandleAuthResultUseCase
import org.koin.androidx.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module

object MainModule {

    fun get() = module("MainModule") {
        viewModel { MainViewModel(get { it }, get { it }, get { it }, get { it }, get { it }, get { it }) }
        single { LogOutUseCase(get { it }, get { it }) }
        single { AuthUseCase(get { it }, get { it }, get { it }) }
        single { HandleAuthResultUseCase(get { it }, get { it }, get { it }) }
    }
}