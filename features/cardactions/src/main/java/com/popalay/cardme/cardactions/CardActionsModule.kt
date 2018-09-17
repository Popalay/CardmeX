package com.popalay.cardme.cardactions

import com.popalay.cardme.cardactions.usecase.RemoveCardUseCase
import com.popalay.cardme.cardactions.usecase.ShareCardUseCase
import org.koin.androidx.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module

object CardActionsModule {

    fun get() = module("CardActionsModule") {
        viewModel { (cardId: String) -> CardActionsViewModel(cardId, get(), get()) }
        single { ShareCardUseCase(get(), get()) }
        single { RemoveCardUseCase(get()) }
    }
}