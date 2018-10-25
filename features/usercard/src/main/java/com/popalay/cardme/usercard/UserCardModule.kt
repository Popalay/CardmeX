package com.popalay.cardme.usercard

import com.popalay.cardme.usercard.usecase.GetCurrentUserWithCardUseCase
import org.koin.androidx.scope.ext.android.bindScope
import org.koin.androidx.scope.ext.android.createScope
import org.koin.androidx.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module

object UserCardModule {

    fun get() = module("UserCardModule") {
        viewModel { UserCardViewModel(get { it }, get { it }, get { it }, get { it }, get { it }) }
        single { GetCurrentUserWithCardUseCase(get { it }, get { it }, get { it }) }
    }
}

private const val scopeId = "UserCardFeature"

internal fun UserCardFragment.loadModule() {
    org.koin.standalone.StandAloneContext.loadKoinModules(UserCardModule.get())
    bindScope(createScope(scopeId))
}