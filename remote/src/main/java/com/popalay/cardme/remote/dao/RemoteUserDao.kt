package com.popalay.cardme.remote.dao

import com.gojuno.koptional.None
import com.gojuno.koptional.Optional
import com.gojuno.koptional.toOptional
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.popalay.cardme.api.dao.RemoteUserDao
import com.popalay.cardme.api.model.User
import com.popalay.cardme.remote.mapper.CardToRemoteCardMapper
import com.popalay.cardme.remote.mapper.RemoteUserToUserMapper
import com.popalay.cardme.remote.mapper.UserToRemoteUserMapper
import com.popalay.cardme.remote.model.RemoteUser
import com.popalay.cardme.remote.users
import io.reactivex.BackpressureStrategy
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.schedulers.Schedulers

internal class RemoteUserDao(
    private val firestore: FirebaseFirestore,
    private val userToRemoteUserMapper: UserToRemoteUserMapper,
    private val remoteUserToUserMapper: RemoteUserToUserMapper,
    private val cardToRemoteCardMapper: CardToRemoteCardMapper
) : RemoteUserDao {

    override fun save(data: User): Completable = Completable.create { emitter ->
        firestore.users.document(data.uuid).set(
            userToRemoteUserMapper(data),
            SetOptions.mergeFields(listOf("uuid", "email", "photoUrl", "phoneNumber", "displayName"))
        )
            .addOnSuccessListener { emitter.onComplete() }
            .addOnFailureListener { emitter.tryOnError(it) }
    }.subscribeOn(Schedulers.io())

    override fun update(data: User): Completable = Completable.create { emitter ->
        data.card?.let { cardToRemoteCardMapper(it) }?.let { card ->
            val map = mapOf(
                "card.id" to card.id,
                "card.number" to card.number,
                "card.holder.id" to card.holder.id,
                "card.holder.name" to card.holder.name,
                "card.isPublic" to card.isPublic,
                "card.cardType" to card.cardType,
                "card.userId" to card.userId,
                "card.createdDate" to card.createdDate as Any,
                "card.updatedDate" to card.updatedDate as Any
            )
            firestore.users.document(data.uuid).update(map)
                .addOnSuccessListener { emitter.onComplete() }
                .addOnFailureListener { emitter.tryOnError(it) }
        } ?: emitter.onComplete()
    }.subscribeOn(Schedulers.io())

    override fun get(id: String): Flowable<Optional<User>> = Flowable.create<Optional<User>>({ emitter ->
        val listenerRegistration = firestore.users.document(id)
            .addSnapshotListener { snapshot, exception ->
                if (exception != null) emitter.tryOnError(exception)
                if (snapshot != null) {
                    emitter.onNext(snapshot.toObject(RemoteUser::class.java)?.let { remoteUserToUserMapper(it) }.toOptional())
                }
            }
        emitter.setCancellable { listenerRegistration.remove() }
    }, BackpressureStrategy.LATEST)
        .onErrorReturnItem(None)
        .subscribeOn(Schedulers.io())

    override fun delete(id: String): Completable = Completable.create { emitter ->
        firestore.users.document(id).delete()
            .addOnSuccessListener { emitter.onComplete() }
            .addOnFailureListener { emitter.tryOnError(it) }
    }.subscribeOn(Schedulers.io())
}