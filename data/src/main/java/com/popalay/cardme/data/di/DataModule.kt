package com.popalay.cardme.data.di

import com.popalay.cardme.data.repository.CardRepository
import com.popalay.cardme.data.repository.UserRepository
import org.koin.dsl.module.module

object DataModule {

    fun get() = module {
        single<com.popalay.cardme.api.repository.CardRepository> { create<CardRepository>() }
        single<com.popalay.cardme.api.repository.UserRepository> { create<UserRepository>() }
    }
}