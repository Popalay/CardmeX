package com.popalay.cardme.pushnotification

import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.iid.InstanceIdResult
import com.popalay.cardme.api.data.repository.TokenRepository
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.Executors

internal class TokenRepository(
    private val firestore: FirebaseFirestore,
    private val firebaseAuth: FirebaseAuth
) : TokenRepository {

    override fun syncToken(): Completable = Single.create<String> { emitter ->
        FirebaseInstanceId.getInstance().instanceId
            .addOnSuccessListener(Executors.newSingleThreadExecutor(), OnSuccessListener<InstanceIdResult> { emitter.onSuccess(it.token) })
            .addOnFailureListener { emitter.tryOnError(it) }
    }
        .flatMapCompletable { syncToken(it) }
        .subscribeOn(Schedulers.io())

    override fun syncToken(token: String): Completable = Completable.create { emitter ->
        firebaseAuth.currentUser?.uid?.also { userId ->
            val firestoreToken = Token(userId, token)
            firestore.collection("token").document(userId).set(firestoreToken)
                .addOnSuccessListener { emitter.onComplete() }
                .addOnFailureListener { emitter.tryOnError(it) }
        }
    }.subscribeOn(Schedulers.io())
}