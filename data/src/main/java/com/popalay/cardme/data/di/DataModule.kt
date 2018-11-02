package com.popalay.cardme.data.di

import com.popalay.cardme.data.repository.*
import org.koin.dsl.module.module

object DataModule {

    fun get() = module {
        single<com.popalay.cardme.api.data.repository.CardRepository> { CardRepository(get { it }, get { it }) }
        single<com.popalay.cardme.api.data.repository.UserRepository> { UserRepository(get { it }, get { it }) }
        single<com.popalay.cardme.api.data.repository.DeviceRepository> { DeviceRepository(get { it }) }
        single<com.popalay.cardme.api.data.repository.RequestRepository> { RequestRepository(get { it }) }
        single<com.popalay.cardme.api.data.repository.NotificationRepository> { NotificationRepository(get { it }, get { it }) }
        factory<com.popalay.cardme.api.data.repository.AuthRepository> { AuthRepository(get { it }) }
    }
}