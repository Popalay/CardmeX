package com.popalay.cardme.data.repository

import com.popalay.cardme.api.data.DataSource
import com.popalay.cardme.api.data.Persister
import com.popalay.cardme.api.data.Source
import com.popalay.cardme.api.data.key.EmptyKey
import com.popalay.cardme.api.model.Card
import com.popalay.cardme.api.model.Holder
import com.popalay.cardme.api.repository.CardRepository
import io.reactivex.Completable
import io.reactivex.Flowable

class CardRepository(
    private val cardCacheDataSource: DataSource<List<Card>, Source.Cache>,
    private val cardCachePersister: Persister<Card, Source.Cache>,
    private val holderCachePersister: Persister<Holder, Source.Cache>
) : CardRepository {

    override fun save(card: Card): Completable = Completable.mergeArray(
        holderCachePersister.persist(EmptyKey, card.holder),
        cardCachePersister.persist(EmptyKey, card)
    )

    override fun getAll(): Flowable<List<Card>> = cardCacheDataSource.flow(EmptyKey)
        .map { it.content }
}