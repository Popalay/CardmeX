package com.popalay.cardme.remote.di

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.popalay.cardme.remote.dao.RemoteCardDao
import com.popalay.cardme.remote.dao.RemoteNotificationDao
import com.popalay.cardme.remote.dao.RemoteRequestDao
import com.popalay.cardme.remote.dao.RemoteUserDao
import com.popalay.cardme.remote.mapper.*
import org.koin.dsl.module.module

object RemoteModule {

    fun get() = module {

        single { RemoteHolderToHolderMapper() }
        single { HolderToRemoteHolderMapper() }
        single { RemoteCardToCardMapper(get { it }) }
        single { CardToRemoteCardMapper(get { it }) }
        single { UserToRemoteUserMapper() }
        single { RequestToRemoteRequestMapper() }
        single { RemoteUserToUserMapper() }
        single { RemoteNotificationToNotificationMapper(get()) }
        single { RemoteCardDao(get { it }, get { it }, get { it }) as com.popalay.cardme.api.remote.dao.RemoteCardDao }
        single { RemoteUserDao(get { it }, get { it }, get { it }) as com.popalay.cardme.api.remote.dao.RemoteUserDao }
        single { RemoteRequestDao(get { it }, get { it }) as com.popalay.cardme.api.remote.dao.RemoteRequestDao }
        single { RemoteNotificationDao(get { it }, get { it }) as com.popalay.cardme.api.remote.dao.RemoteNotificationDao }
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