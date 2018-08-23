package com.popalay.cardme.main.auth

import android.content.Context
import androidx.fragment.app.Fragment
import com.gojuno.koptional.None
import com.gojuno.koptional.Optional
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.popalay.cardme.api.auth.AuthCredentials
import com.popalay.cardme.api.auth.AuthResult
import com.popalay.cardme.api.auth.Authenticator
import com.popalay.cardme.api.model.User
import com.popalay.cardme.core.BuildConfig
import com.popalay.cardme.core.mapper.FirebaseUserToUserMapper
import io.reactivex.Single

internal class GoogleAuthenticator(
    private val context: Context,
    private val fragment: Fragment,
    private val userMapper: FirebaseUserToUserMapper
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
                        emitter.onSuccess(userMapper(it.result.user))
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
                            emitter.onSuccess(userMapper(it.result.user))
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