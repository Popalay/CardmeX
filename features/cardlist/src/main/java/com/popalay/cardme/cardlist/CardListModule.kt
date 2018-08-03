package com.popalay.cardme.cardlist

import org.koin.android.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module
import org.koin.dsl.path.moduleName

object CardListModule {

    fun get() = module(CardListModule::class.moduleName) {
        viewModel { CardListViewModel() }
    }
}