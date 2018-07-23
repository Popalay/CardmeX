package com.popalay.cardme.api.auth

import com.gojuno.koptional.Optional
import com.popalay.cardme.api.model.User
import io.reactivex.Single

interface Authenticator {

    fun auth(credentials: AuthCredentials): Single<Optional<User>>

    fun handleResult(result: AuthResult): Single<Optional<User>>
}

interface AuthCredentials

interface AuthResult