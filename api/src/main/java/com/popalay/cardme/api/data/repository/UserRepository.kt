package com.popalay.cardme.api.data.repository

import com.gojuno.koptional.Optional
import com.popalay.cardme.api.core.model.User
import io.reactivex.Completable
import io.reactivex.Flowable

interface UserRepository {

    fun save(user: User): Completable

    fun delete(): Completable

    fun update(user: User): Completable

    fun get(id: String): Flowable<Optional<User>>

    fun getAll(lastDisplayName: String, limit: Long): Flowable<List<User>>

    fun getAllLikeWithCard(like: String, lastDisplayName: String, limit: Long): Flowable<List<User>>
}