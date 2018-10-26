package com.popalay.cardme.core.di

import android.app.Application
import com.popalay.cardme.BuildConfig
import com.popalay.cardme.authenticator.AuthenticatorModule
import com.popalay.cardme.cache.di.CacheModule
import com.popalay.cardme.data.di.DataModule
import com.popalay.cardme.pushnotification.NotificationModule
import com.popalay.cardme.remote.di.RemoteModule
import org.koin.android.ext.android.startKoin
import org.koin.android.logger.AndroidLogger
import org.koin.log.EmptyLogger

internal fun Application.addDependencies() {
    val logger = if(BuildConfig.DEBUG) AndroidLogger() else EmptyLogger()
    startKoin(
        this,
        listOf(
            CoreModule.get(),
            CacheModule.get(),
            RemoteModule.get(),
            DataModule.get(),
            AuthenticatorModule.get(),
            NotificationModule.get()
        ),
        logger = logger
    )
}