package com.popalay.cardme.remote.di

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.popalay.cardme.remote.datasource.CardRemoteDataSource
import com.popalay.cardme.remote.datasource.UserCardRemoteDataSource
import com.popalay.cardme.remote.datasource.UserRemoteDataSource
import com.popalay.cardme.remote.mapper.CardToRemoteCardMapper
import com.popalay.cardme.remote.mapper.HolderToRemoteHolderMapper
import com.popalay.cardme.remote.mapper.RemoteCardToCardMapper
import com.popalay.cardme.remote.mapper.RemoteHolderToHolderMapper
import com.popalay.cardme.remote.mapper.RemoteUserToUserMapper
import com.popalay.cardme.remote.mapper.UserToRemoteUserMapper
import com.popalay.cardme.remote.persister.CardRemotePersister
import com.popalay.cardme.remote.persister.UserCardRemotePersister
import com.popalay.cardme.remote.persister.UserRemotePersister
import org.koin.dsl.module.module

object RemoteModule {

    init {
        FirebaseFirestore.getInstance().firestoreSettings = FirebaseFirestoreSettings.Builder()
            .setPersistenceEnabled(false)
            .setTimestampsInSnapshotsEnabled(true)
            .build()
    }

    fun get() = module {

        single { RemoteHolderToHolderMapper() }
        single { HolderToRemoteHolderMapper() }
        single { RemoteCardToCardMapper(get()) }
        single { CardToRemoteCardMapper(get()) }
        single { UserToRemoteUserMapper() }
        single { RemoteUserToUserMapper(get()) }
        single { CardRemotePersister(get()) as com.popalay.cardme.api.data.persister.CardRemotePersister }
        single { CardRemoteDataSource(get()) as com.popalay.cardme.api.data.datasource.CardRemoteDataSource }
        single { UserCardRemoteDataSource(get()) as com.popalay.cardme.api.data.datasource.UserCardRemoteDataSource }
        single { UserRemotePersister(get()) as com.popalay.cardme.api.data.persister.UserRemotePersister }
        single { UserRemoteDataSource(get()) as com.popalay.cardme.api.data.datasource.UserRemoteDataSource }
        single { UserCardRemotePersister(get()) as com.popalay.cardme.api.data.persister.UserCardRemotePersister }
    }
}