package com.popalay.cardme.data.di

import com.popalay.cardme.data.repository.*
import com.popalay.cardme.data.store.CardListStore
import com.popalay.cardme.data.store.CardStore
import com.popalay.cardme.data.store.UserStore
import org.koin.dsl.module.module

object DataModule {

    fun get() = module {
        single<com.popalay.cardme.api.data.repository.CardRepository> { CardRepository(get { it }, get { it }, get { it }, get { it }) }
        single<com.popalay.cardme.api.data.repository.UserRepository> { UserRepository(get { it }, get { it }, get { it }) }
        single<com.popalay.cardme.api.data.repository.DeviceRepository> { DeviceRepository(get { it }) }
        single<com.popalay.cardme.api.data.repository.RequestRepository> { RequestRepository(get { it }) }
        single<com.popalay.cardme.api.data.repository.AuthRepository> { AuthRepository(get { it }) }
        single { CardListStore(get { it }, get { it }) }
        single { CardStore(get { it }, get { it }) }
        single { UserStore(get { it }, get { it }) }
    }
}