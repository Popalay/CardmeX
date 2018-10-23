package com.popalay.cardme.carddetails

import com.popalay.cardme.carddetails.usecase.GetCardUseCase
import com.popalay.cardme.carddetails.usecase.SaveCardUseCase
import org.koin.androidx.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module

object CardDetailsModule {

    fun get() = module("CardDetailsModule") {
        viewModel { (cardId: String) -> CardDetailsViewModel(cardId, get(), get(), get(), get()) }
        single { GetCardUseCase(get { it }) }
        single { SaveCardUseCase(get { it }) }
    }
}