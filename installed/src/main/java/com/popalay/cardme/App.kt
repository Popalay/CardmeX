package com.popalay.cardme

import android.app.Application
import com.popalay.cardme.injector.Injector

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        Injector.inject(this)
    }
}