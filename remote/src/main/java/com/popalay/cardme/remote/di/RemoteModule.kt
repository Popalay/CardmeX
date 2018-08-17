package com.popalay.cardme.remote.di

import com.popalay.cardme.remote.mapper.CardToRemoteCardMapper
import com.popalay.cardme.remote.mapper.HolderToRemoteHolderMapper
import com.popalay.cardme.remote.persister.CardRemotePersister
import org.koin.dsl.module.module

object RemoteModule {

    fun get() = module {
        //single { RemoteHolderToHolderMapper() }
        single { HolderToRemoteHolderMapper() }
        //single { RemoteCardWithHolderToCardMapper(get()) }
        single { CardToRemoteCardMapper(get()) }
        single { CardRemotePersister(get()) as com.popalay.cardme.api.data.persister.CardRemotePersister }
        //single { HolderRemotePersister(get(), get()) as com.popalay.cardme.api.data.persister.HolderRemotePersister }
    }
}