package com.popalay.cardme.usercard

import com.popalay.cardme.usercard.usecase.GetCurrentUserWithCardUseCase
import org.koin.androidx.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module

object UserCardModule {

    fun get() = module("UserCardModule") {
        viewModel { UserCardViewModel(get(), get(), get()) }
        single { GetCurrentUserWithCardUseCase(get(), get()) }
    }
}