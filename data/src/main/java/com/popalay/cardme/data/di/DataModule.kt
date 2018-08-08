package com.popalay.cardme.data.di

import com.popalay.cardme.data.repository.CardRepository
import org.koin.dsl.module.module

object DataModule {

    fun get() = module {
        single { CardRepository(get(), get()) as com.popalay.cardme.api.repository.CardRepository }
    }
}