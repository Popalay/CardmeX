package com.popalay.cardme.cache.persister

import com.popalay.cardme.api.data.Key
import com.popalay.cardme.api.data.persister.CardCachePersister
import com.popalay.cardme.api.model.Card
import com.popalay.cardme.cache.dao.CardDao
import com.popalay.cardme.cache.dao.HolderDao
import com.popalay.cardme.cache.mapper.CardToCacheCardMapper
import com.popalay.cardme.cache.mapper.HolderToCacheHolderMapper
import io.reactivex.Completable
import io.reactivex.schedulers.Schedulers

class CardCachePersister internal constructor(
    private val cardDao: CardDao,
    private val cardMapper: CardToCacheCardMapper,
    private val holderMapper: HolderToCacheHolderMapper,
    private val holderDao: HolderDao
) : CardCachePersister {

    override fun persist(key: Key, data: Card): Completable = Completable.fromAction {
        holderDao.insertOrUpdate(holderMapper(data.holder))
        cardDao.insertOrUpdate(cardMapper(data))
    }.subscribeOn(Schedulers.io())

    override fun persist(key: Key, data: List<Card>): Completable = Completable.fromAction {
        holderDao.insertAll(data.map { holderMapper(it.holder) })
        cardDao.insertAll(data.map { cardMapper(it) })
    }.subscribeOn(Schedulers.io())

    override fun delete(key: Key): Completable = if (key is CardCachePersister.Key.ById) {
        Completable.fromAction { cardDao.delete(key.cardId) }.subscribeOn(Schedulers.io())
    } else throw IllegalArgumentException("Unsupported key")
}