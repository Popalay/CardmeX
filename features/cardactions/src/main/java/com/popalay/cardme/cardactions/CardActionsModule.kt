package com.popalay.cardme.cardactions

import androidx.fragment.app.Fragment
import com.popalay.cardme.cardactions.usecase.RemoveCardUseCase
import com.popalay.cardme.cardactions.usecase.ShareCardUseCase
import org.koin.androidx.viewmodel.ext.koin.viewModel
import org.koin.core.parameter.parametersOf
import org.koin.dsl.module.module

object CardActionsModule {

    fun get() = module("CardActionsModule") {
        viewModel { (fragment: Fragment, cardId: String) -> CardActionsViewModel(cardId, get { parametersOf(fragment) }, get()) }
        factory { (fragment: Fragment) -> ShareCardUseCase(fragment, get()) }
        single { RemoveCardUseCase(get { it }) }
    }
}