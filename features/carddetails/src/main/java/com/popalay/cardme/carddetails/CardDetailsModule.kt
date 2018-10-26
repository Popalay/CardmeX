package com.popalay.cardme.carddetails

import com.popalay.cardme.carddetails.usecase.GetCardUseCase
import com.popalay.cardme.carddetails.usecase.SaveCardUseCase
import org.koin.androidx.scope.ext.android.bindScope
import org.koin.androidx.scope.ext.android.getOrCreateScope
import org.koin.androidx.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module
import org.koin.standalone.StandAloneContext.loadKoinModules

private object CardDetailsModule {

    fun get() = module("CardDetailsModule", override = true) {
        viewModel { (cardId: String) -> CardDetailsViewModel(cardId, get(), get(), get(), get()) }
        scope(scopeId) { GetCardUseCase(get { it }) }
        scope(scopeId) { SaveCardUseCase(get { it }) }
    }
}

private const val scopeId = "CardDetailsFeature"

internal fun CardDetailsFragment.loadModule() {
    loadKoinModules(CardDetailsModule.get())
    bindScope(getOrCreateScope(scopeId))
}