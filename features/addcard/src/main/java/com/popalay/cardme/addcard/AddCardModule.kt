package com.popalay.cardme.addcard

import com.popalay.cardme.addcard.usecase.SaveCardUseCase
import org.koin.android.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module
import org.koin.dsl.path.moduleName

object AddCardModule {

    fun get() = module(AddCardModule::class.moduleName) {
        viewModel { AddCardViewModel(get()) }
        single { SaveCardUseCase(get()) }
    }
}