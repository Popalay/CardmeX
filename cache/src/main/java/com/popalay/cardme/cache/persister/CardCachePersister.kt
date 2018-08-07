package com.popalay.cardme.cache.persister

import com.popalay.cardme.api.data.Key
import com.popalay.cardme.api.mapper.Mapper
import com.popalay.cardme.api.model.Card
import com.popalay.cardme.api.model.Holder
import com.popalay.cardme.cache.dao.CardDao
import com.popalay.cardme.cache.dao.HolderDao
import com.popalay.cardme.cache.model.CacheCard
import com.popalay.cardme.cache.model.CacheHolder
import io.reactivex.Completable

class CardCachePersister internal constructor(
    private val cardDao: CardDao,
    private val holderDao: HolderDao,
    private val cardMapper: Mapper<Card, CacheCard>,
    private val holderMapper: Mapper<Holder, CacheHolder>
) : CachePersister<Card>() {

    override fun persist(key: Key, data: Card): Completable = Completable.fromAction {
        holderDao.insertOrUpdate(holderMapper(data.holder))
        cardDao.insertOrUpdate(cardMapper(data))
    }
}