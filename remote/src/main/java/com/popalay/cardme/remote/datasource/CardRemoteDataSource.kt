package com.popalay.cardme.remote.datasource

import com.google.firebase.firestore.FirebaseFirestore
import com.popalay.cardme.api.data.Data
import com.popalay.cardme.api.data.Source
import com.popalay.cardme.api.data.datasource.CardRemoteDataSource
import com.popalay.cardme.api.model.Card
import com.popalay.cardme.remote.cards
import com.popalay.cardme.remote.mapper.RemoteCardToCardMapper
import com.popalay.cardme.remote.model.RemoteCard
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.schedulers.Schedulers

class CardRemoteDataSource internal constructor(
    private val mapper: RemoteCardToCardMapper
) : CardRemoteDataSource {

    override fun flow(key: CardRemoteDataSource.Key): Flowable<Data<List<Card>>> = Flowable.create<List<Card>>({ emitter ->
        FirebaseFirestore.getInstance().cards
            .whereEqualTo("userId", key.userId)
            .addSnapshotListener { snapshot, exception ->
                if (exception != null) emitter.onError(exception)
                if (snapshot != null) emitter.onNext(snapshot.toObjects(RemoteCard::class.java).map { mapper(it) })
            }
    }, BackpressureStrategy.LATEST)
        .onErrorReturnItem(listOf())
        .map { Data(it, Source.Network) }
        .subscribeOn(Schedulers.io())
}