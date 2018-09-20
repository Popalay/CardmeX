package com.popalay.cardme.remote.dao

import com.google.firebase.firestore.FirebaseFirestore
import com.popalay.cardme.api.core.model.Card
import com.popalay.cardme.api.remote.dao.RemoteCardDao
import com.popalay.cardme.remote.cards
import com.popalay.cardme.remote.mapper.CardToRemoteCardMapper
import com.popalay.cardme.remote.mapper.RemoteCardToCardMapper
import com.popalay.cardme.remote.model.RemoteCard
import io.reactivex.BackpressureStrategy
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.schedulers.Schedulers

internal class RemoteCardDao(
    private val firestore: FirebaseFirestore,
    private val remoteCardToCardMapper: RemoteCardToCardMapper,
    private val cardToRemoteCardMapper: CardToRemoteCardMapper
) : RemoteCardDao {

    override fun save(data: Card): Completable = Completable.create { emitter ->
        firestore.cards.document(data.id).set(cardToRemoteCardMapper(data))
            .addOnSuccessListener { emitter.onComplete() }
            .addOnFailureListener { emitter.tryOnError(it) }
    }.subscribeOn(Schedulers.io())

    override fun saveAll(data: List<Card>): Completable = Completable.create { emitter ->
        firestore.cards.add(data.map { cardToRemoteCardMapper(it) })
            .addOnSuccessListener { emitter.onComplete() }
            .addOnFailureListener { emitter.tryOnError(it) }
    }.subscribeOn(Schedulers.io())

    override fun get(id: String): Flowable<Card> = Flowable.create<Card>({ emitter ->
        val listenerRegistration = firestore.cards.document(id)
            .addSnapshotListener { snapshot, exception ->
                if (exception != null) emitter.tryOnError(exception)
                if (snapshot != null) emitter.onNext(remoteCardToCardMapper(snapshot.toObject(RemoteCard::class.java)!!))
            }
        emitter.setCancellable { listenerRegistration.remove() }
    }, BackpressureStrategy.LATEST)
        .subscribeOn(Schedulers.io())

    override fun getAll(userId: String): Flowable<List<Card>> = Flowable.create<List<Card>>({ emitter ->
        val listenerRegistration = firestore.cards
            .whereEqualTo("userId", userId)
            .addSnapshotListener { snapshot, exception ->
                if (exception != null) emitter.tryOnError(exception)
                if (snapshot != null) emitter.onNext(snapshot.toObjects(RemoteCard::class.java).map { remoteCardToCardMapper(it) })
            }
        emitter.setCancellable { listenerRegistration.remove() }
    }, BackpressureStrategy.LATEST)
        .onErrorReturnItem(listOf())
        .subscribeOn(Schedulers.io())

    override fun delete(id: String): Completable = Completable.create { emitter ->
        firestore.cards.document(id).delete()
            .addOnSuccessListener { emitter.onComplete() }
            .addOnFailureListener { emitter.tryOnError(it) }
    }.subscribeOn(Schedulers.io())
}