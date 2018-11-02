package com.popalay.cardme.notifications;

import com.popalay.cardme.notifications.usecase.NotificationListUseCase
import org.koin.androidx.scope.ext.android.bindScope
import org.koin.androidx.scope.ext.android.getOrCreateScope
import org.koin.androidx.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module
import org.koin.standalone.StandAloneContext.loadKoinModules

private object NotificationsModule {

    fun get() = module("NotificationsModule", override = true) {
        viewModel { NotificationsViewModel(get { it }) }
        scope(scopeId) { NotificationListUseCase(get { it }, get { it }) }
    }
}

private const val scopeId = "NotificationsFeature"

internal fun NotificationsFragment.loadModule() {
    loadKoinModules(NotificationsModule.get())
    bindScope(getOrCreateScope(scopeId))
}