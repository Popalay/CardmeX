package com.popalay.cardme.login

import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import io.reactivex.Completable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import kotlin.reflect.KClass

class FirebasePhoneAuthenticator : Authenticator {

    @Volatile var verificationId: String = ""

    override fun auth(credentials: AuthCredentials): Completable = Completable.create { emitter ->
        if (credentials !is AuthCredentials.PhoneAuthCredentials) {
            emitter.onError(IllegalArgumentException("Can handle only PhoneAuthCredentials"))
            return@create
        }
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
            credentials.phoneNumber,
            60,
            TimeUnit.SECONDS,
            Executors.newSingleThreadExecutor(),
            object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                    FirebaseAuth.getInstance().signInWithCredential(credential)
                        .addOnCompleteListener { emitter.onComplete() }
                        .addOnFailureListener { emitter.onError(it) }
                }

                override fun onVerificationFailed(exception: FirebaseException) {
                    emitter.onError(exception)
                }

                override fun onCodeSent(verificationId: String, token: PhoneAuthProvider.ForceResendingToken) {
                    this@FirebasePhoneAuthenticator.verificationId = verificationId
                }
            }
        )
    }.subscribeOn(Schedulers.io())

    override fun signOut(): Completable =
        Completable.fromAction { FirebaseAuth.getInstance().signOut() }
            .subscribeOn(Schedulers.io())

    override fun onVerificationCodeRetrieved(code: String): Completable = Completable.create { emitter ->
        val credential = PhoneAuthProvider.getCredential(verificationId, code)
        FirebaseAuth.getInstance().signInWithCredential(credential)
            .addOnCompleteListener { emitter.onComplete() }
            .addOnFailureListener { emitter.onError(it) }
    }.subscribeOn(Schedulers.io())
}

interface Authenticator {

    fun auth(credentials: AuthCredentials): Completable

    fun signOut(): Completable

    fun onVerificationCodeRetrieved(code: String): Completable
}

sealed class AuthCredentials {
    data class PhoneAuthCredentials(val phoneNumber: String) : AuthCredentials()
}

class AuthenticatorFactory(
    private val authenticators: Map<KClass<out AuthCredentials>, Authenticator>
) : Authenticator {

    override fun auth(credentials: AuthCredentials): Completable =
        requireNotNull(authenticators[credentials::class]).auth(credentials)

    override fun signOut(): Completable = Completable.merge(authenticators.map { it.value.signOut() })

    override fun onVerificationCodeRetrieved(code: String): Completable =
        Completable.merge(authenticators.map { it.value.onVerificationCodeRetrieved(code) })
}