package com.popalay.cardme.core

import android.app.Application
import android.os.Looper
import com.crashlytics.android.Crashlytics
import com.crashlytics.android.core.CrashlyticsCore
import com.facebook.stetho.Stetho
import com.popalay.cardme.BuildConfig
import com.popalay.cardme.core.di.addDependencies
import com.popalay.cardme.core.error.DefaultRxErrorHandler
import com.popalay.cardme.core.util.UiModeDelegate
import io.fabric.sdk.android.Fabric
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.plugins.RxJavaPlugins
import org.koin.android.ext.android.get

class App : Application() {

    override fun onCreate() {
        addDependencies()
        super.onCreate()
        initDevTools()
        initRx()
    }

    private fun initRx() {
        RxJavaPlugins.setErrorHandler(DefaultRxErrorHandler())
        val asyncMainThreadScheduler = AndroidSchedulers.from(Looper.getMainLooper(), true)
        RxAndroidPlugins.setInitMainThreadSchedulerHandler { asyncMainThreadScheduler }
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