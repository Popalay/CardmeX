package com.popalay.cardme.api.repository

import com.gojuno.koptional.Optional
import com.popalay.cardme.api.model.User
import io.reactivex.Completable
import io.reactivex.Flowable

interface UserRepository {

    fun save(user: User): Completable

    fun delete(): Completable

    fun update(user: User): Completable

    fun getCurrentUser(): Flowable<Optional<User>>
}