package com.popalay.cardme.cache.datasource

import com.popalay.cardme.api.datasource.Data
import com.popalay.cardme.api.datasource.DataSource
import com.popalay.cardme.api.datasource.EmptyKey
import com.popalay.cardme.api.datasource.Source
import com.popalay.cardme.cache.dao.CardDao
import com.popalay.cardme.cache.mapper.CardMapper
import io.reactivex.Flowable
import io.reactivex.schedulers.Schedulers
import com.popalay.cardme.api.model.Card as ApiCard
import com.popalay.cardme.api.model.Holder as ApiHolder

class CardCacheDataSource internal constructor(
    private val cardDao: CardDao,
    private val mapper: CardMapper
) : DataSource<EmptyKey, List<ApiCard>> {

    override fun flow(key: EmptyKey): Flowable<Data<List<ApiCard>>> =
        cardDao.findAllWithHolder()
            .map { it.map(mapper::invoke) }
            .map { Data(it, Source.CACHE) }
            .subscribeOn(Schedulers.io())
}