package com.popalay.cardme.authenticator

import com.gojuno.koptional.None
import com.gojuno.koptional.Optional
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import com.popalay.cardme.api.auth.AuthCredentials
import com.popalay.cardme.api.auth.AuthResult
import com.popalay.cardme.api.auth.Authenticator
import com.popalay.cardme.api.core.model.User
import com.popalay.cardme.authenticator.mapper.FirebaseUserToUserMapper
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

internal class FirebasePhoneAuthenticator(
    private val userMapper: FirebaseUserToUserMapper,
    private val firebseAuth: FirebaseAuth
) : Authenticator {

    @Volatile
    var verificationId: String = ""

    override fun auth(credentials: AuthCredentials): Single<Optional<User>> = Single.create<Optional<User>> { emitter ->
        if (credentials !is CardmeAuthCredentials.Phone) {
            emitter.tryOnError(IllegalArgumentException("Can handle only Phone"))
            return@create
        }
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
            credentials.phoneNumber,
            60,
            TimeUnit.SECONDS,
            Executors.newSingleThreadExecutor(),
            object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                    firebseAuth.signInWithCredential(credential)
                        .addOnCompleteListener {
                            if (it.isSuccessful) {
                                emitter.onSuccess(userMapper(it.result?.user))
                            } else if (it.exception != null) {
                                emitter.tryOnError(it.exception!!)
                            }
                        }
                        .addOnFailureListener { emitter.tryOnError(it) }
                }

                override fun onVerificationFailed(exception: FirebaseException) {
                    emitter.tryOnError(exception)
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
            emitter.tryOnError(IllegalArgumentException("Can handle only Phone"))
            return@create
        }
        val credential = PhoneAuthProvider.getCredential(verificationId, result.code)
        firebseAuth.signInWithCredential(credential)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    emitter.onSuccess(userMapper(it.result?.user))
                } else if (it.exception != null) {
                    emitter.tryOnError(it.exception!!)
                }
            }
            .addOnFailureListener { emitter.tryOnError(it) }
    }.subscribeOn(Schedulers.io())

    override fun authState(): Flowable<Optional<User>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun signOut(): Completable {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun currentUser(): Single<Optional<User>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}