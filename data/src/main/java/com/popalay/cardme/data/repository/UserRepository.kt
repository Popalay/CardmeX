package com.popalay.cardme.data.repository

import com.gojuno.koptional.Optional
import com.google.firebase.auth.FirebaseAuth
import com.popalay.cardme.api.dao.CacheUserDao
import com.popalay.cardme.api.dao.RemoteUserDao
import com.popalay.cardme.api.model.User
import com.popalay.cardme.api.repository.UserRepository
import com.popalay.cardme.data.store.UserStore
import io.reactivex.Completable
import io.reactivex.Flowable

class UserRepository(
    private val remoteUserDao: RemoteUserDao,
    private val cacheUserDao: CacheUserDao,
    private val userStore: UserStore
) : UserRepository {

    override fun getCurrentUser(): Flowable<Optional<User>> =
        userStore.get(UserStore.Key.ById(FirebaseAuth.getInstance().currentUser?.uid ?: ""))

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