package com.popalay.cardme.pushnotification

import org.koin.dsl.module.module

object NotificationModule {

    fun get() = module {
        single<com.popalay.cardme.api.data.repository.TokenRepository> { TokenRepository(get(), get()) }
    }
}