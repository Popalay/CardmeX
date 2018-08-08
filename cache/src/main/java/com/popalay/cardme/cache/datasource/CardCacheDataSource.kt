package com.popalay.cardme.cache.datasource

import com.popalay.cardme.api.data.Data
import com.popalay.cardme.api.data.Key
import com.popalay.cardme.api.data.Source
import com.popalay.cardme.api.data.datasource.CardCacheDataSource
import com.popalay.cardme.api.model.Card
import com.popalay.cardme.cache.dao.CardDao
import com.popalay.cardme.cache.mapper.CacheCardWithHolderToCardMapper
import io.reactivex.Flowable

class CardCacheDataSource internal constructor(
    private val cardDao: CardDao,
    private val mapper: CacheCardWithHolderToCardMapper
) : CardCacheDataSource {

    override fun flow(key: Key): Flowable<Data<List<Card>>> = Flowable.just(listOf<Card>())
        .map { Data(it, Source.Cache) }
/*        cardDao.findAllWithHolder()
            .map { it.map(mapper::invoke) }

            .subscribeOn(Schedulers.io())*/
}