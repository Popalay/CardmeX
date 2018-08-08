package com.popalay.cardme.cache.persister

import com.popalay.cardme.api.data.Key
import com.popalay.cardme.api.data.persister.HolderCachePersister
import com.popalay.cardme.api.model.Holder
import com.popalay.cardme.cache.dao.HolderDao
import com.popalay.cardme.cache.mapper.HolderToCacheHolderMapper
import io.reactivex.Completable
import io.reactivex.schedulers.Schedulers

class HolderCachePersister internal constructor(
    private val holderDao: HolderDao,
    private val holderMapper: HolderToCacheHolderMapper
) : HolderCachePersister {

    override fun persist(key: Key, data: Holder): Completable = Completable.fromAction {
        holderDao.insertOrUpdate(holderMapper(data))
    }.subscribeOn(Schedulers.io())
}