package com.popalay.cardme.injector

import android.app.Application
import com.popalay.cardme.base.MainModule
import com.popalay.cardme.login.LogInModule
import org.koin.android.ext.android.startKoin

object Injector {

    fun inject(app: Application) {
        app.startKoin(listOf(MainModule.get(), LogInModule.get()))
    }
}