package com.popalay.cardme.main.auth

import com.gojuno.koptional.Optional
import com.popalay.cardme.api.auth.AuthCredentials
import com.popalay.cardme.api.auth.AuthResult
import com.popalay.cardme.api.auth.Authenticator
import com.popalay.cardme.api.core.model.User
import io.reactivex.Single
import kotlin.reflect.KClass

internal class AuthenticatorFacade(
    private val authenticators: Map<KClass<out Authenticator>, Authenticator>
) : Authenticator {

    override fun auth(credentials: AuthCredentials): Single<Optional<User>> = when (credentials as CardmeAuthCredentials) {
        is CardmeAuthCredentials.Phone -> requireNotNull(authenticators[FirebasePhoneAuthenticator::class]).auth(credentials)
        CardmeAuthCredentials.Google -> requireNotNull(authenticators[GoogleAuthenticator::class]).auth(credentials)
    }

    override fun handleResult(result: AuthResult): Single<Optional<User>> = when (result as CardmeAuthResult) {
        is CardmeAuthResult.Phone -> checkNotNull(authenticators[FirebasePhoneAuthenticator::class]).handleResult(result)
        is CardmeAuthResult.Google -> requireNotNull(authenticators[GoogleAuthenticator::class]).handleResult(result)
    }
}