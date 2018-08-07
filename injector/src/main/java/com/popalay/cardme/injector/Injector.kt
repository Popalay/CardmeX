package com.popalay.cardme.injector

import android.app.Application
import com.popalay.cardme.cache.di.CacheModule
import com.popalay.cardme.cardlist.CardListModule
import com.popalay.cardme.core.di.CoreModule
import com.popalay.cardme.login.LogInModule
import com.popalay.cardme.main.MainModule
import org.koin.android.ext.android.startKoin

object Injector {

    fun inject(app: Application) {
        app.startKoin(
            app,
            listOf(
                CoreModule.get(),
                CacheModule.get(),
                LogInModule.get(),
                MainModule.get(),
                CardListModule.get()
            )
        )
    }
}