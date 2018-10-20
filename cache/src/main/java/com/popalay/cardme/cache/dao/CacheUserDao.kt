package com.popalay.cardme.cache.dao

import com.gojuno.koptional.Optional
import com.gojuno.koptional.toOptional
import com.popalay.cardme.api.cache.dao.CacheUserDao
import com.popalay.cardme.api.core.model.User
import com.popalay.cardme.cache.database.dao.UserDao
import com.popalay.cardme.cache.mapper.CacheUserToUserMapper
import com.popalay.cardme.cache.mapper.UserToCacheUserMapper
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers

internal class CacheUserDao(
    private val userDao: UserDao,
    private val cacheUserToUserMapper: CacheUserToUserMapper,
    private val userToCacheUserMapper: UserToCacheUserMapper
) : CacheUserDao {

    override fun save(data: User): Completable = Completable.fromAction {
        userDao.insertOrUpdate(userToCacheUserMapper(data))
    }.subscribeOn(Schedulers.io())

    override fun get(): Flowable<Optional<User>> = userDao.findOne()
        .map { list -> list.firstOrNull()?.let { cacheUserToUserMapper(it) }.toOptional() }
        .subscribeOn(Schedulers.io())

    override fun delete(): Completable = Completable.fromAction {
        userDao.deleteAll()
    }.subscribeOn(Schedulers.io())

    override fun isPresent(): Single<Boolean> = userDao.isNotEmpty()
        .subscribeOn(Schedulers.io())
}