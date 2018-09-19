package com.popalay.cardme.cache.dao

import com.gojuno.koptional.Optional
import com.gojuno.koptional.toOptional
import com.popalay.cardme.api.dao.CacheUserDao
import com.popalay.cardme.api.model.User
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
        .map { list -> list.takeIf { it.isNotEmpty() }?.let { cacheUserToUserMapper(it.first()) }.toOptional() }
        .subscribeOn(Schedulers.io())

    override fun delete(): Completable = Completable.fromAction {
        userDao.deleteAll()
    }.subscribeOn(Schedulers.io())

    override fun isPresent(): Single<Boolean> = userDao.isNotEmpty()
        .subscribeOn(Schedulers.io())
}