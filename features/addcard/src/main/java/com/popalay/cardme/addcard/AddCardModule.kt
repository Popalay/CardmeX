package com.popalay.cardme.addcard

import com.popalay.cardme.addcard.usecase.*
import org.koin.androidx.scope.ext.android.bindScope
import org.koin.androidx.scope.ext.android.createScope
import org.koin.androidx.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module

private object AddCardModule {

    fun get() = module("AddCardModule", override = true) {
        viewModel { (isUserCard: String) -> AddCardViewModel(isUserCard.toBoolean(), get(), get(), get(), get(), get(), get(), get(), get(), get()) }
        scope(scopeId) { SaveCardUseCase(get { it }, get { it }) }
        scope(scopeId) { SaveUserCardUseCase(get { it }, get { it }, get { it }) }
        scope(scopeId) { ValidateCardUseCase() }
        scope(scopeId) { IdentifyCardNumberUseCase() }
        scope(scopeId) { UserListUseCase(get { it }, get { it }) }
        scope(scopeId) { SendAddCardRequestUseCase(get { it }, get { it }) }
    }
}

private const val scopeId = "AddCardFeature"

internal fun AddCardFragment.loadModule() {
    org.koin.standalone.StandAloneContext.loadKoinModules(AddCardModule.get())
    bindScope(createScope(scopeId))
}