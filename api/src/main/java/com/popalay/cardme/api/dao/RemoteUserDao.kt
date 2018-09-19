package com.popalay.cardme.api.dao

import com.gojuno.koptional.Optional
import com.popalay.cardme.api.model.User
import io.reactivex.Completable
import io.reactivex.Flowable

interface RemoteUserDao {

    fun save(data: User): Completable

    fun update(data: User): Completable

    fun get(id: String): Flowable<Optional<User>>

    fun delete(id: String): Completable
}