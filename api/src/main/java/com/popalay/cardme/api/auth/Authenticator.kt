package com.popalay.cardme.api.auth

import com.gojuno.koptional.Optional
import com.popalay.cardme.api.core.model.User
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single

interface Authenticator {

    fun auth(credentials: AuthCredentials): Single<Optional<User>>

    fun handleResult(result: AuthResult): Single<Optional<User>>

    fun authState(): Flowable<Optional<User>>

    fun signOut(): Completable

    fun currentUser(): Single<Optional<User>>
}