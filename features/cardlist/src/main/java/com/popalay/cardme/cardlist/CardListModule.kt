package com.popalay.cardme.cardlist

import com.popalay.cardme.cardlist.usecase.CardListUseCase
import org.koin.androidx.scope.ext.android.bindScope
import org.koin.androidx.scope.ext.android.createScope
import org.koin.androidx.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module

private object CardListModule {

    fun get() = module("CardListModule") {
        viewModel { CardListViewModel(get { it }, get { it }, get { it }) }
        scope(scopeId) { CardListUseCase(get { it }, get { it }, get { it }) }
    }
}

private const val scopeId = "CardListFeature"

internal fun CardListFragment.loadModule() {
    org.koin.standalone.StandAloneContext.loadKoinModules(CardListModule.get())
    bindScope(createScope(scopeId))
}