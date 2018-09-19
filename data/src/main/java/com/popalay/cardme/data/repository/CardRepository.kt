package com.popalay.cardme.data.repository

import com.popalay.cardme.api.cache.dao.CacheCardDao
import com.popalay.cardme.api.remote.dao.RemoteCardDao
import com.popalay.cardme.api.core.model.Card
import com.popalay.cardme.api.data.repository.CardRepository
import com.popalay.cardme.data.store.CardListStore
import com.popalay.cardme.data.store.CardStore
import io.reactivex.Completable
import io.reactivex.Flowable

class CardRepository(
    private val cacheCardDao: CacheCardDao,
    private val remoteCardDao: RemoteCardDao,
    private val cardStore: CardStore,
    private val cardListStore: CardListStore
) : CardRepository {

    override fun save(data: Card): Completable = Completable.mergeArray(
        cacheCardDao.save(data),
        if (data.isPublic) remoteCardDao.save(data) else Completable.complete()
    )

    override fun saveAll(data: List<Card>): Completable = Completable.mergeArray(
        cacheCardDao.saveAll(data),
        remoteCardDao.saveAll(data.filter { it.isPublic })
    )

    override fun get(id: String): Flowable<Card> = cardStore.get(CardStore.Key.ById(id))

    override fun delete(id: String): Completable = Completable.mergeArray(
        cacheCardDao.delete(id),
        remoteCardDao.delete(id)
    )

    override fun getAll(userId: String): Flowable<List<Card>> = cardListStore.get(CardListStore.Key.AllByUser(userId))
}