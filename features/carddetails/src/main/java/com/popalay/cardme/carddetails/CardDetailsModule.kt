package com.popalay.cardme.carddetails

import com.popalay.cardme.carddetails.usecase.GetCardUseCase
import com.popalay.cardme.carddetails.usecase.SaveCardUseCase
import org.koin.androidx.scope.ext.android.bindScope
import org.koin.androidx.scope.ext.android.createScope
import org.koin.androidx.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module

private object CardDetailsModule {

    fun get() = module("CardDetailsModule") {
        viewModel { (cardId: String) -> CardDetailsViewModel(cardId, get(), get(), get(), get()) }
        scope(scopeId) { GetCardUseCase(get { it }) }
        scope(scopeId) { SaveCardUseCase(get { it }) }
    }
}

private const val scopeId = "CardDetailsFeature"

internal fun CardDetailsFragment.loadModule() {
    org.koin.standalone.StandAloneContext.loadKoinModules(CardDetailsModule.get())
    bindScope(createScope(scopeId))
}