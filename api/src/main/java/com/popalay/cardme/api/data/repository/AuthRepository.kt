package com.popalay.cardme.api.data.repository

import com.gojuno.koptional.Optional
import com.popalay.cardme.api.auth.AuthCredentials
import com.popalay.cardme.api.auth.AuthResult
import com.popalay.cardme.api.core.model.User
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single

interface AuthRepository {

    fun authState(): Flowable<Optional<User>>

    fun signOut(): Completable

    fun auth(credentials: AuthCredentials): Single<Optional<User>>

    fun handleResult(result: AuthResult): Single<Optional<User>>

    fun currentUser(): Single<Optional<User>>
}