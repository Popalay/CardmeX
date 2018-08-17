package com.popalay.cardme.remote.di

import com.popalay.cardme.remote.datasource.CardRemoteDataSource
import com.popalay.cardme.remote.mapper.CardToRemoteCardMapper
import com.popalay.cardme.remote.mapper.HolderToRemoteHolderMapper
import com.popalay.cardme.remote.mapper.RemoteCardToCardMapper
import com.popalay.cardme.remote.mapper.RemoteHolderToHolderMapper
import com.popalay.cardme.remote.persister.CardRemotePersister
import org.koin.dsl.module.module

object RemoteModule {

    fun get() = module {
        single { RemoteHolderToHolderMapper() }
        single { HolderToRemoteHolderMapper() }
        single { RemoteCardToCardMapper(get()) }
        single { CardToRemoteCardMapper(get()) }
        single { CardRemotePersister(get()) as com.popalay.cardme.api.data.persister.CardRemotePersister }
        single { CardRemoteDataSource(get()) as com.popalay.cardme.api.data.datasource.CardRemoteDataSource }
    }
}