package com.popalay.cardme.usercard

import com.popalay.cardme.usercard.usecase.GetUserCardUseCase
import org.koin.androidx.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module
import org.koin.dsl.path.moduleName

object UserCardModule {

    fun get() = module(UserCardModule::class.moduleName) {
        viewModel { UserCardViewModel(get(), get(), get()) }
        single { GetUserCardUseCase(get(), get()) }
    }
}