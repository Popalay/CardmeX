package com.popalay.cardme.data.repository

import com.gojuno.koptional.Optional
import com.gojuno.koptional.toOptional
import com.popalay.cardme.api.cache.dao.CacheUserDao
import com.popalay.cardme.api.core.model.User
import com.popalay.cardme.api.data.repository.UserRepository
import com.popalay.cardme.api.remote.dao.RemoteUserDao
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.schedulers.Schedulers

internal class UserRepository(
    private val remoteUserDao: RemoteUserDao,
    private val cacheUserDao: CacheUserDao
) : BaseRepository(), UserRepository {

    override fun get(id: String): Flowable<Optional<User>> =
        flowElement(
            cacheUserDao.get(),
            remoteUserDao.get(id),
            cacheUserDao::save
        ).subscribeOn(Schedulers.io())

    override fun getAll(lastDisplayName: String, limit: Long): Flowable<List<User>> =
        remoteUserDao.getAll(lastDisplayName, limit).onErrorReturnItem(listOf())

    override fun getAllLikeWithCard(like: String, lastDisplayName: String, limit: Long): Flowable<List<User>> =
        remoteUserDao.getAllLikeWithCard(like, lastDisplayName, limit).onErrorReturnItem(listOf())

    override fun save(user: User): Completable = Completable.mergeArray(
        remoteUserDao.save(user),
        cacheUserDao.save(user.toOptional())
    )

    override fun delete(): Completable = cacheUserDao.delete()

    override fun update(user: User): Completable = Completable.mergeArray(
        remoteUserDao.update(user),
        cacheUserDao.save(user.toOptional())
    )
}