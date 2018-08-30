package com.popalay.cardme.data.di

import com.popalay.cardme.data.repository.CardRepository
import com.popalay.cardme.data.repository.UserRepository
import org.koin.dsl.module.module

object DataModule {

    fun get() = module {
        single<com.popalay.cardme.api.repository.CardRepository> { CardRepository(get(), get(), get(), get(), get()) }
        single<com.popalay.cardme.api.repository.UserRepository> { UserRepository(get(), get(), get(), get()) }
    }
}