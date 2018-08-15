package com.popalay.cardme.addcard

import com.popalay.cardme.addcard.usecase.IdentifyCardNumberUseCase
import com.popalay.cardme.addcard.usecase.SaveCardUseCase
import com.popalay.cardme.addcard.usecase.ValidateCardUseCase
import org.koin.androidx.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module
import org.koin.dsl.path.moduleName

object AddCardModule {

    fun get() = module(AddCardModule::class.moduleName) {
        viewModel { AddCardViewModel(get(), get(), get()) }
        single { SaveCardUseCase(get()) }
        single { ValidateCardUseCase() }
        single { IdentifyCardNumberUseCase() }
    }
}