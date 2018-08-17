package com.popalay.cardme.data.repository

import com.popalay.cardme.api.data.datasource.CardCacheDataSource
import com.popalay.cardme.api.data.key.EmptyKey
import com.popalay.cardme.api.data.persister.CardCachePersister
import com.popalay.cardme.api.data.persister.CardRemotePersister
import com.popalay.cardme.api.model.Card
import com.popalay.cardme.api.repository.CardRepository
import io.reactivex.Completable
import io.reactivex.Flowable

class CardRepository(
    private val cardCacheDataSource: CardCacheDataSource,
    private val cardCachePersister: CardCachePersister,
    private val cardRemotePersister: CardRemotePersister
) : CardRepository {

    override fun save(card: Card): Completable = Completable.mergeArray(
        cardCachePersister.persist(EmptyKey, card),
        cardRemotePersister.persist(EmptyKey, card)
    )

    override fun getAll(): Flowable<List<Card>> = cardCacheDataSource.flow(EmptyKey)
        .map { it.content }
}