package com.popalay.cardme.remote.dao

import com.google.firebase.firestore.FirebaseFirestore
import com.popalay.cardme.api.core.model.Request
import com.popalay.cardme.api.remote.dao.RemoteRequestDao
import com.popalay.cardme.remote.mapper.RequestToRemoteRequestMapper
import com.popalay.cardme.remote.requests
import io.reactivex.Completable
import io.reactivex.schedulers.Schedulers

internal class RemoteRequestDao(
    private val firestore: FirebaseFirestore,
    private val requestToRemoteRequestMapper: RequestToRemoteRequestMapper
) : RemoteRequestDao {

    override fun save(data: Request): Completable = Completable.create { emitter ->
        firestore.requests.add(requestToRemoteRequestMapper(data))
            .addOnSuccessListener { emitter.onComplete() }
            .addOnFailureListener { emitter.tryOnError(it) }
    }.subscribeOn(Schedulers.io())
}