package com.popalay.cardme.cache.persister

import com.popalay.cardme.api.data.Key
import com.popalay.cardme.api.mapper.Mapper
import com.popalay.cardme.api.model.Card
import com.popalay.cardme.cache.dao.CardDao
import com.popalay.cardme.cache.model.CacheCard
import io.reactivex.Completable
import io.reactivex.schedulers.Schedulers

class CardCachePersister internal constructor(
    private val cardDao: CardDao,
    private val cardMapper: Mapper<Card, CacheCard>
) : CachePersister<Card>() {

    override fun persist(key: Key, data: Card): Completable = Completable.fromAction {
        cardDao.insertOrUpdate(cardMapper(data))
    }.subscribeOn(Schedulers.io())
}