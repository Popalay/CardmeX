package com.popalay.cardme.api.dao

import com.gojuno.koptional.Optional
import com.popalay.cardme.api.model.User
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single

interface CacheUserDao {

    fun save(data: User): Completable

    fun get(): Flowable<Optional<User>>

    fun delete(): Completable

    fun isPresent(): Single<Boolean>
}