package com.popalay.cardme

import android.app.Application
import com.crashlytics.android.Crashlytics
import com.crashlytics.android.core.CrashlyticsCore
import com.facebook.stetho.Stetho
import com.popalay.cardme.core.error.DefaultRxErrorHandler
import com.popalay.cardme.injector.Injector
import io.fabric.sdk.android.Fabric
import io.reactivex.plugins.RxJavaPlugins

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        Injector.inject(this)
        initDevTools()
        RxJavaPlugins.setErrorHandler(DefaultRxErrorHandler())
    }

    private fun initDevTools() {
        val crashlyticsKit = Crashlytics.Builder()
            .core(CrashlyticsCore.Builder().disabled(BuildConfig.DEBUG).build())
            .build()
        Fabric.with(this, crashlyticsKit)

        if (BuildConfig.DEBUG) {
            Stetho.initializeWithDefaults(this)
        }
    }
}