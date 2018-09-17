package com.popalay.cardme.addcard

import com.popalay.cardme.addcard.usecase.IdentifyCardNumberUseCase
import com.popalay.cardme.addcard.usecase.SaveCardUseCase
import com.popalay.cardme.addcard.usecase.SaveUserCardUseCase
import com.popalay.cardme.addcard.usecase.ValidateCardUseCase
import org.koin.androidx.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module

object AddCardModule {

    fun get() = module("AddCardModule") {
        viewModel { (isUserCard: Boolean) -> AddCardViewModel(isUserCard, get(), get(), get(), get(), get()) }
        single { SaveCardUseCase(get(), get()) }
        single { SaveUserCardUseCase(get(), get()) }
        single { ValidateCardUseCase() }
        single { IdentifyCardNumberUseCase() }
    }
}