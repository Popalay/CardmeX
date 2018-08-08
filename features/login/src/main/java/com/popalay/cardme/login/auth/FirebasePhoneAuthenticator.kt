package com.popalay.cardme.login.auth

import com.gojuno.koptional.None
import com.gojuno.koptional.Optional
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import com.popalay.cardme.api.auth.AuthCredentials
import com.popalay.cardme.api.auth.AuthResult
import com.popalay.cardme.api.auth.Authenticator
import com.popalay.cardme.api.model.User
import com.popalay.cardme.core.mapper.FirebaseUserToUserMapper
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

internal class FirebasePhoneAuthenticator(
    private val userMapper: FirebaseUserToUserMapper
) : Authenticator {

    @Volatile var verificationId: String = ""

    override fun auth(credentials: AuthCredentials): Single<Optional<User>> = Single.create<Optional<User>> { emitter ->
        if (credentials !is CardmeAuthCredentials.Phone) {
            emitter.onError(IllegalArgumentException("Can handle only Phone"))
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
                        .addOnCompleteListener {
                            if (it.isSuccessful) {
                                emitter.onSuccess(userMapper(it.result.user))
                            } else {
                                emitter.onError(it.exception!!)
                            }
                        }
                        .addOnFailureListener { emitter.onError(it) }
                }

                override fun onVerificationFailed(exception: FirebaseException) {
                    emitter.onError(exception)
                }

                override fun onCodeSent(verificationId: String, token: PhoneAuthProvider.ForceResendingToken) {
                    this@FirebasePhoneAuthenticator.verificationId = verificationId
                    emitter.onSuccess(None)
                }
            }
        )
    }.subscribeOn(Schedulers.io())

    override fun handleResult(result: AuthResult): Single<Optional<User>> = Single.create<Optional<User>> { emitter ->
        if (result !is CardmeAuthResult.Phone) {
            emitter.onError(IllegalArgumentException("Can handle only Phone"))
            return@create
        }
        val credential = PhoneAuthProvider.getCredential(verificationId, result.code)
        FirebaseAuth.getInstance().signInWithCredential(credential)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    emitter.onSuccess(userMapper(it.result.user))
                } else {
                    emitter.onError(it.exception!!)
                }
            }
            .addOnFailureListener { emitter.onError(it) }
    }.subscribeOn(Schedulers.io())
}