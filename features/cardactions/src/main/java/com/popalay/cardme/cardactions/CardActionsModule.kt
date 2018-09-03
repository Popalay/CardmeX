package com.popalay.cardme.cardactions

import com.popalay.cardme.cardactions.usecase.ShareCardUseCase
import org.koin.androidx.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module
import org.koin.dsl.path.moduleName

object CardActionsModule {

    fun get() = module(CardActionsModule::class.moduleName) {
        viewModel { (cardId: String) -> CardActionsViewModel(cardId, get()) }
        single { ShareCardUseCase(get(), get()) }
    }
}