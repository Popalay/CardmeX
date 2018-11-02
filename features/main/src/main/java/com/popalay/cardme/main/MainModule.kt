package com.popalay.cardme.main

import com.popalay.cardme.core.usecase.LogOutUseCase
import com.popalay.cardme.main.usecase.AuthUseCase
import com.popalay.cardme.main.usecase.HandleAuthResultUseCase
import com.popalay.cardme.main.usecase.SyncTokenUseCase
import org.koin.androidx.scope.ext.android.bindScope
import org.koin.androidx.scope.ext.android.getOrCreateScope
import org.koin.androidx.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module
import org.koin.standalone.StandAloneContext.loadKoinModules

private object MainModule {

    fun get() = module("MainModule", override = true) {
        viewModel { MainViewModel(get { it }, get { it }, get { it }, get { it }, get { it }, get { it }, get { it }) }
        scope(scopeId) { LogOutUseCase(get { it }, get { it }) }
        scope(scopeId) { AuthUseCase(get { it }, get { it }, get { it }, get { it }) }
        scope(scopeId) { HandleAuthResultUseCase(get { it }, get { it }, get { it }, get { it }) }
        scope(scopeId) { SyncTokenUseCase(get { it }) }
    }
}

private const val scopeId = "MainFeature"

internal fun MainFragment.loadModule() {
    loadKoinModules(MainModule.get())
    bindScope(getOrCreateScope(scopeId))
}