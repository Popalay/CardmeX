package com.popalay.cardme.login

import android.content.Context
import android.content.Intent
import android.support.v4.app.Fragment
import com.gojuno.koptional.None
import com.gojuno.koptional.Optional
import com.gojuno.koptional.toOptional
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import com.popalay.cardme.api.auth.AuthCredentials
import com.popalay.cardme.api.auth.AuthResult
import com.popalay.cardme.api.auth.Authenticator
import com.popalay.cardme.api.model.User
import com.popalay.cardme.base.BuildConfig
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import kotlin.reflect.KClass

class GoogleAuthenticator(
    private val context: Context,
    private val fragment: Fragment
) : Authenticator {

    companion object {

        private const val REQUEST_CODE_SIGN_IN = 101
    }

    private val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestIdToken(BuildConfig.GOOGLE_CLIENT_ID)
        .requestEmail()
        .requestProfile()
        .build()

    override fun auth(credentials: AuthCredentials): Single<Optional<User>> = Single.create<Optional<User>> { emitter ->
        if (credentials !== CardmeAuthCredentials.Google) {
            emitter.onError(IllegalArgumentException("Can handle only Google"))
            return@create
        }
        val account = GoogleSignIn.getLastSignedInAccount(context)
        if (account != null) {
            val credential = GoogleAuthProvider.getCredential(account.idToken, null)
            FirebaseAuth.getInstance().signInWithCredential(credential)
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        emitter.onSuccess(it.result.user.toUser().toOptional())
                    } else {
                        emitter.onError(it.exception!!)
                    }
                }
                .addOnFailureListener { emitter.onError(it) }
        } else {
            val client = GoogleSignIn.getClient(context, gso)
            val intent = client.signInIntent
            fragment.startActivityForResult(intent, REQUEST_CODE_SIGN_IN)
            emitter.onSuccess(None)
        }
    }

    override fun handleResult(result: AuthResult): Single<Optional<User>> = Single.create<Optional<User>> { emitter ->
        if (result !is CardmeAuthResult.Google) {
            emitter.onError(IllegalArgumentException("Can handle only Google"))
            return@create
        }
        if (result.success && result.requestCode == REQUEST_CODE_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            try {
                val account = task.getResult(ApiException::class.java)
                val credential = GoogleAuthProvider.getCredential(account.idToken, null)
                FirebaseAuth.getInstance().signInWithCredential(credential)
                    .addOnCompleteListener {
                        if (it.isSuccessful) {
                            emitter.onSuccess(it.result.user.toUser().toOptional())
                        } else {
                            emitter.onError(it.exception!!)
                        }
                    }
                    .addOnFailureListener { emitter.onError(it) }
            } catch (exception: ApiException) {
                emitter.onError(exception)
            }
        } else {
            emitter.onSuccess(None)
        }
    }
}

class FirebasePhoneAuthenticator : Authenticator {

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
                                emitter.onSuccess(it.result.user.toUser().toOptional())
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
                    emitter.onSuccess(it.result.user.toUser().toOptional())
                } else {
                    emitter.onError(it.exception!!)
                }
            }
            .addOnFailureListener { emitter.onError(it) }
    }.subscribeOn(Schedulers.io())
}

class AuthenticatorFacade(
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

fun FirebaseUser.toUser() = User(
    uuid = uid,
    email = email ?: "",
    phoneNumber = phoneNumber ?: "",
    photoUrl = photoUrl?.toString() ?: "",
    displayName = displayName ?: "Undefined"
)

sealed class CardmeAuthCredentials : AuthCredentials {
    data class Phone(val phoneNumber: String) : CardmeAuthCredentials()
    object Google : CardmeAuthCredentials()
}

sealed class CardmeAuthResult : AuthResult {
    data class Phone(val code: String) : CardmeAuthResult()
    data class Google(val success: Boolean, val requestCode: Int, val data: Intent) : CardmeAuthResult()
}