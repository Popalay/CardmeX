package com.popalay.cardme.data.repository

import com.gojuno.koptional.Optional
import com.gojuno.koptional.toOptional
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

    override fun get(id: String): Flowable<Optional<Card>> =
        cardCacheDataSource.flowSingle(CardCacheDataSource.Key.ById(id))
            .map { it.content.toOptional() }

    override fun getAll(): Flowable<List<Card>> =
/*        (if (user is Some) cardRemoteDataSource.flowList(CardRemoteDataSource.Key(requireNotNull(user.toNullable().uuid)))
        else Flowable.just(Data(listOf(), Source.Network)))
            .flatMap { cardCachePersister.persist(EmptyKey, it.content).toFlowable<Boolean>() }*/
            cardCacheDataSource.flowList(CardCacheDataSource.Key.List)
            .map { it.content }
}