package com.popalay.cardme

import android.app.Application
import com.crashlytics.android.Crashlytics
import com.crashlytics.android.core.CrashlyticsCore
import com.popalay.cardme.injector.Injector
import io.fabric.sdk.android.Fabric

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        Injector.inject(this)
        initCrashlytics()
    }

    private fun initCrashlytics() {
        val crashlyticsKit = Crashlytics.Builder()
            .core(CrashlyticsCore.Builder().disabled(BuildConfig.DEBUG).build())
            .build()
        Fabric.with(this, crashlyticsKit)
    }
}