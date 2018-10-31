package com.popalay.cardme.data.repository

import com.gojuno.koptional.Optional
import com.popalay.cardme.api.auth.AuthCredentials
import com.popalay.cardme.api.auth.AuthResult
import com.popalay.cardme.api.auth.Authenticator
import com.popalay.cardme.api.core.model.User
import com.popalay.cardme.api.data.repository.AuthRepository
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single

internal class AuthRepository(
    private val authenticator: Authenticator
) : BaseRepository(), AuthRepository {

    override fun authState(): Flowable<Optional<User>> = authenticator.authState()

    override fun signOut(): Completable = authenticator.signOut()

    override fun auth(credentials: AuthCredentials): Single<Optional<User>> = authenticator.auth(credentials)

    override fun handleResult(result: AuthResult): Single<Optional<User>> = authenticator.handleResult(result)

    override fun currentUser(): Single<Optional<User>> = authenticator.currentUser()
}