package com.popalay.cardme.cardlist

import com.popalay.cardme.cardlist.usecase.CardListUseCase
import com.popalay.cardme.core.usecase.CopyCardNumberUseCase
import org.koin.androidx.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module

object CardListModule {

    fun get() = module("CardListModule") {
        viewModel { CardListViewModel(get { it }, get { it }, get { it }) }
        single { CardListUseCase(get { it }, get { it }, get { it }) }
    }
}