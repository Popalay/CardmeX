package com.popalay.cardme.injector

import android.app.Application
import com.popalay.cardme.base.BaseModule
import com.popalay.cardme.login.LogInModule
import com.popalay.cardme.main.MainModule
import org.koin.android.ext.android.startKoin

object Injector {

    fun inject(app: Application) {
        app.startKoin(
            app,
            listOf(
                BaseModule.get(),
                LogInModule.get(),
                MainModule.get()
            )
        )
    }
}