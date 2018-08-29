package com.popalay.cardme.api.repository

import com.popalay.cardme.api.model.User
import io.reactivex.Completable

interface UserRepository {

    fun save(user: User): Completable
}