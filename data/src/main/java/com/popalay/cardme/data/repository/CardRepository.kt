package com.popalay.cardme.data.repository

import com.gojuno.koptional.Optional
import com.gojuno.koptional.Some
import com.popalay.cardme.api.data.Data
import com.popalay.cardme.api.data.Source
import com.popalay.cardme.api.data.datasource.CardCacheDataSource
import com.popalay.cardme.api.data.datasource.CardRemoteDataSource
import com.popalay.cardme.api.data.key.EmptyKey
import com.popalay.cardme.api.data.persister.CardCachePersister
import com.popalay.cardme.api.data.persister.CardRemotePersister
import com.popalay.cardme.api.model.Card
import com.popalay.cardme.api.model.User
import com.popalay.cardme.api.repository.CardRepository
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.rxkotlin.Flowables

class CardRepository(
    private val cardCacheDataSource: CardCacheDataSource,
    private val cardRemoteDataSource: CardRemoteDataSource,
    private val cardCachePersister: CardCachePersister,
    private val cardRemotePersister: CardRemotePersister,
    private val user: Optional<User>
) : CardRepository {

    override fun save(card: Card): Completable = Completable.mergeArray(
        cardCachePersister.persist(EmptyKey, card),
        if (card.isPublic) cardRemotePersister.persist(EmptyKey, card).onErrorComplete() else Completable.complete()
    )

    override fun getAll(): Flowable<List<Card>> = Flowables.combineLatest(
        cardCacheDataSource.flow(EmptyKey),
        if (user is Some) cardRemoteDataSource.flow(CardRemoteDataSource.Key(requireNotNull(user.toNullable().uuid)))
        else Flowable.just(Data(listOf(), Source.Network))
    ) { cache, remote ->
        mutableListOf<Card>().apply {
            addAll(cache.content)
            addAll(remote.content)
            distinctBy { it.id }
        }
    }
}