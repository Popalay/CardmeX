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
        val holderId = holderDao.insertOrUpdate(holderMapper(data.holder))
        cardDao.insertOrUpdate(cardMapper(data).copy(holderId = holderId))
    }.subscribeOn(Schedulers.io())
}