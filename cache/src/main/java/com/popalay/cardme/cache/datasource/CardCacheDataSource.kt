package com.popalay.cardme.cache.datasource

import com.popalay.cardme.api.data.Data
import com.popalay.cardme.api.data.Source
import com.popalay.cardme.api.data.datasource.CardCacheDataSource
import com.popalay.cardme.api.model.Card
import com.popalay.cardme.cache.dao.CardDao
import com.popalay.cardme.cache.mapper.CacheCardWithHolderToCardMapper
import io.reactivex.Flowable
import io.reactivex.schedulers.Schedulers

class CardCacheDataSource internal constructor(
    private val cardDao: CardDao,
    private val mapper: CacheCardWithHolderToCardMapper
) : CardCacheDataSource {

    override fun flowList(key: CardCacheDataSource.Key): Flowable<Data<List<Card>>> =
        cardDao.findAllWithHolder()
            .map { it.map(mapper::invoke) }
            .map { Data(it, Source.Cache) }
            .subscribeOn(Schedulers.io())

    override fun flowSingle(key: CardCacheDataSource.Key): Flowable<Data<Card>> = if (key is CardCacheDataSource.Key.ById) {
        cardDao.findOneWithHolder(key.cardId)
            .map { mapper(it) }
            .map { Data(it, Source.Cache) }
            .subscribeOn(Schedulers.io())
    } else throw IllegalArgumentException("Unsupportable key")
}