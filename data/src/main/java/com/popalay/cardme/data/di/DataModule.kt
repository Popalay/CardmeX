package com.popalay.cardme.data.di

import com.popalay.cardme.data.repository.CardRepository
import org.koin.dsl.module.module
import org.koin.dsl.path.moduleName

object DataModule {

    fun get() = module(DataModule::class.moduleName) {
        single { CardRepository(get(), get(), get()) as com.popalay.cardme.api.repository.CardRepository }
    }
}