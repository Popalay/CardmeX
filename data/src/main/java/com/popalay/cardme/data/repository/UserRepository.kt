package com.popalay.cardme.data.repository

import com.popalay.cardme.api.data.key.EmptyKey
import com.popalay.cardme.api.data.persister.UserRemotePersister
import com.popalay.cardme.api.model.User
import com.popalay.cardme.api.repository.UserRepository
import io.reactivex.Completable
import io.reactivex.schedulers.Schedulers

class UserRepository(
    private val userRemotePersister: UserRemotePersister
) : UserRepository {

    override fun save(user: User): Completable = userRemotePersister.persist(EmptyKey, user)
        .subscribeOn(Schedulers.io())
}