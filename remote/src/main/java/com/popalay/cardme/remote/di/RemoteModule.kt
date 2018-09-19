package com.popalay.cardme.remote.di

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.popalay.cardme.remote.dao.RemoteCardDao
import com.popalay.cardme.remote.dao.RemoteUserDao
import com.popalay.cardme.remote.mapper.*
import org.koin.dsl.module.module

object RemoteModule {

    fun get() = module {

        single { RemoteHolderToHolderMapper() }
        single { HolderToRemoteHolderMapper() }
        single { RemoteCardToCardMapper(get()) }
        single { CardToRemoteCardMapper(get()) }
        single { UserToRemoteUserMapper() }
        single { RemoteUserToUserMapper(get()) }
        single { RemoteCardDao(get(), get(), get()) as com.popalay.cardme.api.remote.dao.RemoteCardDao }
        single { RemoteUserDao(get(), get(), get(), get()) as com.popalay.cardme.api.remote.dao.RemoteUserDao }
        single {
            FirebaseFirestore.getInstance().apply {
                firestoreSettings = FirebaseFirestoreSettings.Builder()
                    .setPersistenceEnabled(false)
                    .setTimestampsInSnapshotsEnabled(true)
                    .build()
            }
        }
    }
}