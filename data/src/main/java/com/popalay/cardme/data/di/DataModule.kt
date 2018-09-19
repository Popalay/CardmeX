package com.popalay.cardme.data.di

import com.popalay.cardme.data.repository.CardRepository
import com.popalay.cardme.data.repository.DeviceRepository
import com.popalay.cardme.data.repository.UserRepository
import com.popalay.cardme.data.store.CardListStore
import com.popalay.cardme.data.store.CardStore
import com.popalay.cardme.data.store.UserStore
import org.koin.dsl.module.module

object DataModule {

    fun get() = module {
        single<com.popalay.cardme.api.repository.CardRepository> { CardRepository(get(), get(), get(), get()) }
        single<com.popalay.cardme.api.repository.UserRepository> { UserRepository(get(), get(), get()) }
        single<com.popalay.cardme.api.repository.DeviceRepository> { DeviceRepository(get()) }
        single { CardListStore(get(), get()) }
        single { CardStore(get(), get()) }
        single { UserStore(get(), get()) }
    }
}