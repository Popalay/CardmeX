package com.popalay.cardme.usercard

import com.popalay.cardme.usercard.usecase.GetCurrentUserWithCardUseCase
import org.koin.androidx.scope.ext.android.bindScope
import org.koin.androidx.scope.ext.android.getOrCreateScope
import org.koin.androidx.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module
import org.koin.standalone.StandAloneContext

private object UserCardModule {

    fun get() = module("UserCardModule", override = true) {
        viewModel { UserCardViewModel(get { it }, get { it }, get { it }, get { it }, get { it }) }
        scope(scopeId) { GetCurrentUserWithCardUseCase(get { it }, get { it }, get { it }) }
    }
}

private const val scopeId = "UserCardFeature"

internal fun UserCardFragment.loadModule() {
    StandAloneContext.loadKoinModules(UserCardModule.get())
    bindScope(getOrCreateScope(scopeId))
}