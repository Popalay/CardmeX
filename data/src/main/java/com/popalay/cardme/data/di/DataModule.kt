package com.popalay.cardme.data.di

import com.popalay.cardme.data.repository.CardRepository
import com.popalay.cardme.data.repository.UserRepository
import org.koin.dsl.module.module

object DataModule {

    fun get() = module {
        single { CardRepository(get(), get(), get(), get(), get()) as com.popalay.cardme.api.repository.CardRepository }
        single { UserRepository(get()) as com.popalay.cardme.api.repository.UserRepository }
    }
}