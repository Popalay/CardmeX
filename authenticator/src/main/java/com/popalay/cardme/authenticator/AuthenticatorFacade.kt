package com.popalay.cardme.authenticator

import com.gojuno.koptional.Optional
import com.google.firebase.auth.FirebaseAuth
import com.popalay.cardme.api.auth.AuthCredentials
import com.popalay.cardme.api.auth.AuthResult
import com.popalay.cardme.api.auth.Authenticator
import com.popalay.cardme.api.core.model.User
import com.popalay.cardme.authenticator.mapper.FirebaseUserToUserMapper
import io.reactivex.BackpressureStrategy
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import kotlin.reflect.KClass

internal class AuthenticatorFacade(
    private val authenticators: Map<KClass<out Authenticator>, Authenticator>,
    private val userMapper: FirebaseUserToUserMapper,
    private val firebaseAuth: FirebaseAuth
) : Authenticator {

    override fun auth(credentials: AuthCredentials): Single<Optional<User>> = when (credentials as CardmeAuthCredentials) {
        is CardmeAuthCredentials.Phone -> requireNotNull(authenticators[FirebasePhoneAuthenticator::class]).auth(credentials)
        CardmeAuthCredentials.Google -> requireNotNull(authenticators[GoogleAuthenticator::class]).auth(credentials)
    }

    override fun handleResult(result: AuthResult): Single<Optional<User>> = when (result as CardmeAuthResult) {
        is CardmeAuthResult.Phone -> checkNotNull(authenticators[FirebasePhoneAuthenticator::class]).handleResult(result)
        is CardmeAuthResult.Google -> requireNotNull(authenticators[GoogleAuthenticator::class]).handleResult(result)
    }

    override fun authState(): Flowable<Optional<User>> = Flowable.create<Optional<User>>({ emitter ->
        val authStateListener = FirebaseAuth.AuthStateListener {
            emitter.onNext(userMapper(it.currentUser))
        }
        firebaseAuth.addAuthStateListener(authStateListener)
        emitter.setCancellable { firebaseAuth.removeAuthStateListener(authStateListener) }
    }, BackpressureStrategy.LATEST)
        .subscribeOn(Schedulers.io())

    override fun signOut(): Completable = Completable.fromAction { firebaseAuth.signOut() }

    override fun currentUser(): Single<Optional<User>> = Single.fromCallable { userMapper(firebaseAuth.currentUser) }
}