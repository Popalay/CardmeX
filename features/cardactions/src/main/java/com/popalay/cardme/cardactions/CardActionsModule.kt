package com.popalay.cardme.cardactions

import androidx.fragment.app.Fragment
import com.popalay.cardme.cardactions.usecase.RemoveCardUseCase
import org.koin.androidx.scope.ext.android.bindScope
import org.koin.androidx.scope.ext.android.createScope
import org.koin.androidx.viewmodel.ext.koin.viewModel
import org.koin.core.parameter.parametersOf
import org.koin.dsl.module.module

private object CardActionsModule {

    fun get() = module("CardActionsModule", override = true) {
        viewModel { (fragment: Fragment, cardId: String) -> CardActionsViewModel(cardId, get { parametersOf(fragment) }, get()) }
        single { RemoveCardUseCase(get { it }) }
    }
}

private const val scopeId = "CardActionsFeature"

internal fun CardActionsFragment.loadModule() {
    org.koin.standalone.StandAloneContext.loadKoinModules(CardActionsModule.get())
    bindScope(createScope(scopeId))
}