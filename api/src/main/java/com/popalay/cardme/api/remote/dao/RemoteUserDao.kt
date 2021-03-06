package com.popalay.cardme.api.remote.dao

import com.gojuno.koptional.Optional
import com.popalay.cardme.api.core.model.User
import io.reactivex.Completable
import io.reactivex.Flowable

interface RemoteUserDao {

    fun save(data: User): Completable

    fun update(data: User): Completable

    fun get(id: String): Flowable<Optional<User>>

    fun delete(id: String): Completable

    fun getAll(lastDisplayName: String, limit: Long): Flowable<List<User>>

    fun getAllLike(like: String, lastDisplayName: String, limit: Long): Flowable<List<User>>

    fun getAllLikeWithCard(like: String, lastDisplayName: String, limit: Long): Flowable<List<User>>
}