package com.popalay.cardme.api.cache.dao

import com.gojuno.koptional.Optional
import com.popalay.cardme.api.core.model.User
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single

interface CacheUserDao {

    fun save(data: Optional<User>): Completable

    fun get(): Flowable<Optional<User>>

    fun delete(): Completable

    fun isPresent(): Single<Boolean>
}