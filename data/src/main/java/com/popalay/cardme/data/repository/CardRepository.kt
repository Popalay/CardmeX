package com.popalay.cardme.data.repository

import com.gojuno.koptional.Optional
import com.gojuno.koptional.toOptional
import com.popalay.cardme.api.cache.dao.CacheCardDao
import com.popalay.cardme.api.core.model.Card
import com.popalay.cardme.api.data.repository.CardRepository
import com.popalay.cardme.api.remote.dao.RemoteCardDao
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.schedulers.Schedulers

internal class CardRepository(
    private val cacheCardDao: CacheCardDao,
    private val remoteCardDao: RemoteCardDao
) : BaseRepository(), CardRepository {

    override fun save(data: Card): Completable = Completable.mergeArray(
        cacheCardDao.save(data.toOptional()),
        if (data.isPublic) remoteCardDao.save(data).onErrorComplete() else Completable.complete()
    )

    override fun saveAll(data: List<Card>): Completable = Completable.mergeArray(
        cacheCardDao.saveAll(data),
        remoteCardDao.saveAll(data.filter { it.isPublic })
    )

    override fun delete(id: String): Completable = Completable.mergeArray(
        remoteCardDao.delete(id).onErrorComplete(),
        cacheCardDao.delete(id)
    )

    override fun get(id: String): Flowable<Optional<Card>> =
        flowElement(
            cacheCardDao.get(id),
            remoteCardDao.get(id),
            cacheCardDao::save
        ).subscribeOn(Schedulers.io())

    override fun getAll(userId: String): Flowable<List<Card>> =
        flow(
            cacheCardDao.getAll(),
            remoteCardDao.getAll(userId),
            cacheCardDao::saveAll
        ).subscribeOn(Schedulers.io())
}
