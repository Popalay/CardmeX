package com.popalay.cardme.injector

import android.app.Application
import com.popalay.cardme.addcard.AddCardModule
import com.popalay.cardme.authenticator.AuthenticatorModule
import com.popalay.cardme.cache.di.CacheModule
import com.popalay.cardme.cardactions.CardActionsModule
import com.popalay.cardme.carddetails.CardDetailsModule
import com.popalay.cardme.cardlist.CardListModule
import com.popalay.cardme.core.di.CoreModule
import com.popalay.cardme.data.di.DataModule
import com.popalay.cardme.main.MainModule
import com.popalay.cardme.pushnotification.NotificationModule
import com.popalay.cardme.remote.di.RemoteModule
import com.popalay.cardme.usercard.UserCardModule
import org.koin.android.ext.android.startKoin

object Injector {

    fun inject(app: Application) {
        app.startKoin(
            app,
            listOf(
                CoreModule.get(),
                CacheModule.get(),
                RemoteModule.get(),
                DataModule.get(),
                AuthenticatorModule.get(),
                NotificationModule.get(),
                MainModule.get(),
                CardListModule.get(),
                AddCardModule.get(),
                UserCardModule.get(),
                CardActionsModule.get(),
                CardDetailsModule.get()
            )
        )
    }
}