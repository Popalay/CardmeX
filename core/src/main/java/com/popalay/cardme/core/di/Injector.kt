package com.popalay.cardme.core.di

import android.app.Application
import com.popalay.cardme.authenticator.AuthenticatorModule
import com.popalay.cardme.cache.di.CacheModule
import com.popalay.cardme.data.di.DataModule
import com.popalay.cardme.pushnotification.NotificationModule
import com.popalay.cardme.remote.di.RemoteModule
import org.koin.android.ext.android.startKoin

internal fun Application.addDependencies() {
    startKoin(
        this,
        listOf(
            CoreModule.get(),
            CacheModule.get(),
            RemoteModule.get(),
            DataModule.get(),
            AuthenticatorModule.get(),
            NotificationModule.get()
        )
    )
}