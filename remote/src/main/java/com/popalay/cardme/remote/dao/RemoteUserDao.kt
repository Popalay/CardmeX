package com.popalay.cardme.remote.dao

import com.gojuno.koptional.None
import com.gojuno.koptional.Optional
import com.gojuno.koptional.toOptional
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.popalay.cardme.api.core.model.User
import com.popalay.cardme.api.remote.dao.RemoteUserDao
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
    private val remoteUserToUserMapper: RemoteUserToUserMapper
) : RemoteUserDao {

    override fun save(data: User): Completable = Completable.create { emitter ->
        firestore.users.document(data.uuid).set(
            userToRemoteUserMapper(data),
            SetOptions.mergeFields("uuid", "email", "photoUrl", "phoneNumber", "displayName")
        )
            .addOnSuccessListener { emitter.onComplete() }
            .addOnFailureListener { emitter.tryOnError(it) }
    }.subscribeOn(Schedulers.io())

    override fun update(data: User): Completable = Completable.create { emitter ->
        firestore.users.document(data.uuid).set(userToRemoteUserMapper(data), SetOptions.merge())
            .addOnSuccessListener { emitter.onComplete() }
            .addOnFailureListener { emitter.tryOnError(it) }
    }.subscribeOn(Schedulers.io())

    override fun get(id: String): Flowable<Optional<User>> = Flowable.create<Optional<User>>({ emitter ->
        val listenerRegistration = firestore.users.document(id)
            .addSnapshotListener { snapshot, exception ->
                if (exception != null) emitter.tryOnError(exception)
                if (snapshot != null) emitter.onNext(snapshot.toObject(RemoteUser::class.java)?.let { remoteUserToUserMapper(it) }.toOptional())
            }
        emitter.setCancellable { listenerRegistration.remove() }
    }, BackpressureStrategy.LATEST)
        .onErrorReturnItem(None)
        .subscribeOn(Schedulers.io())

    override fun getAll(lastDisplayName: String, limit: Long): Flowable<List<User>> = Flowable.create<List<User>>({ emitter ->
        val listenerRegistration = firestore.users
            .orderBy("displayName")
            .startAt(lastDisplayName)
            .limit(limit)
            .addSnapshotListener { snapshot, exception ->
                if (exception != null) emitter.tryOnError(exception)
                if (snapshot != null) emitter.onNext(snapshot.toObjects(RemoteUser::class.java).map { remoteUserToUserMapper(it) })
            }
        emitter.setCancellable { listenerRegistration.remove() }
    }, BackpressureStrategy.LATEST)
        .subscribeOn(Schedulers.io())

    override fun getAllLike(like: String, lastDisplayName: String, limit: Long): Flowable<List<User>> = Flowable.create<List<User>>({ emitter ->
        val listenerRegistration = firestore.users
            .orderBy("displayName")
            .startAt(like)
            .startAfter(lastDisplayName)
            .limit(limit)
            .addSnapshotListener { snapshot, exception ->
                if (exception != null) emitter.tryOnError(exception)
                if (snapshot != null) emitter.onNext(snapshot.toObjects(RemoteUser::class.java).map { remoteUserToUserMapper(it) })
            }
        emitter.setCancellable { listenerRegistration.remove() }
    }, BackpressureStrategy.LATEST)
        .subscribeOn(Schedulers.io())

    override fun getAllLikeWithCard(like: String, lastDisplayName: String, limit: Long): Flowable<List<User>> =
        Flowable.create<List<User>>({ emitter ->
            val listenerRegistration = firestore.users
                .orderBy("cardId")
                .whereGreaterThan("cardId", "")
                .orderBy("displayName")
                .startAt(like)
                .startAfter(lastDisplayName)
                .limit(limit)
                .addSnapshotListener { snapshot, exception ->
                    if (exception != null) emitter.tryOnError(exception)
                    if (snapshot != null) emitter.onNext(snapshot.toObjects(RemoteUser::class.java).map { remoteUserToUserMapper(it) })
                }
            emitter.setCancellable { listenerRegistration.remove() }
        }, BackpressureStrategy.LATEST)
            .subscribeOn(Schedulers.io())

    override fun delete(id: String): Completable = Completable.create { emitter ->
        firestore.users.document(id).delete()
            .addOnSuccessListener { emitter.onComplete() }
            .addOnFailureListener { emitter.tryOnError(it) }
    }.subscribeOn(Schedulers.io())
}