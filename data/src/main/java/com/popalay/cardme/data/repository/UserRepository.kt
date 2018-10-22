package com.popalay.cardme.data.repository

import com.gojuno.koptional.Optional
import com.popalay.cardme.api.cache.dao.CacheUserDao
import com.popalay.cardme.api.core.model.User
import com.popalay.cardme.api.data.repository.UserRepository
import com.popalay.cardme.api.remote.dao.RemoteUserDao
import com.popalay.cardme.data.store.UserStore
import io.reactivex.Completable
import io.reactivex.Flowable

internal class UserRepository(
    private val remoteUserDao: RemoteUserDao,
    private val cacheUserDao: CacheUserDao,
    private val userStore: UserStore
) : UserRepository {

    override fun get(id: String): Flowable<Optional<User>> =
        userStore.get(UserStore.Key.ById(id))

    override fun getAll(lastDisplayName: String, limit: Long): Flowable<List<User>> =
        remoteUserDao.getAll(lastDisplayName, limit).onErrorReturnItem(listOf())

    override fun getAllLikeWithCard(like: String, lastDisplayName: String, limit: Long): Flowable<List<User>> =
        remoteUserDao.getAllLikeWithCard(like, lastDisplayName, limit).onErrorReturnItem(listOf())

    override fun save(user: User): Completable = Completable.mergeArray(
        remoteUserDao.save(user),
        cacheUserDao.save(user)
    )

    override fun delete(): Completable = cacheUserDao.delete()

    override fun update(user: User): Completable = Completable.mergeArray(
        remoteUserDao.update(user),
        cacheUserDao.save(user)
    )
}