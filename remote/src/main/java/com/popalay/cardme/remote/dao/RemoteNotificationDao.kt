package com.popalay.cardme.remote.dao

import com.gojuno.koptional.Optional
import com.gojuno.koptional.toOptional
import com.google.firebase.firestore.FirebaseFirestore
import com.popalay.cardme.api.core.model.Notification
import com.popalay.cardme.api.remote.dao.RemoteNotificationDao
import com.popalay.cardme.remote.mapper.RemoteNotificationToNotificationMapper
import com.popalay.cardme.remote.model.RemoteNotification
import com.popalay.cardme.remote.notifications
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.schedulers.Schedulers

internal class RemoteNotificationDao(
    private val firestore: FirebaseFirestore,
    private val remoteNotificationMapper: RemoteNotificationToNotificationMapper
) : RemoteNotificationDao {

    override fun get(id: String): Flowable<Optional<Notification>> = Flowable.create<Optional<Notification>>({ emitter ->
        val listenerRegistration = firestore.notifications.document(id)
            .addSnapshotListener { snapshot, exception ->
                if (exception != null) emitter.tryOnError(exception)
                if (snapshot != null) {
                    val card = snapshot.toObject(RemoteNotification::class.java)?.let { remoteNotificationMapper(it) }.toOptional()
                    emitter.onNext(card)
                }
            }
        emitter.setCancellable { listenerRegistration.remove() }
    }, BackpressureStrategy.LATEST)
        .subscribeOn(Schedulers.io())

    override fun getAll(userId: String): Flowable<List<Notification>> = Flowable.create<List<Notification>>({ emitter ->
        val listenerRegistration = firestore.notifications
            .whereEqualTo("toUserUuid", userId)
            .addSnapshotListener { snapshot, exception ->
                if (exception != null) emitter.tryOnError(exception)
                if (snapshot != null) emitter.onNext(snapshot.toObjects(RemoteNotification::class.java).map { remoteNotificationMapper(it) })
            }
        emitter.setCancellable { listenerRegistration.remove() }
    }, BackpressureStrategy.LATEST)
        .onErrorReturnItem(listOf())
        .subscribeOn(Schedulers.io())
}