package com.popalay.cardme.remote.persister

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.popalay.cardme.api.data.Key
import com.popalay.cardme.api.data.persister.UserRemotePersister
import com.popalay.cardme.api.model.User
import com.popalay.cardme.remote.mapper.UserToRemoteUserMapper
import com.popalay.cardme.remote.users
import io.reactivex.Completable
import io.reactivex.schedulers.Schedulers

class UserRemotePersister internal constructor(
    private val userMapper: UserToRemoteUserMapper
) : UserRemotePersister {

    override fun persist(key: Key, data: User): Completable = Completable.create { emitter ->
        FirebaseFirestore.getInstance().users.document(data.uuid).set(
            userMapper(data),
            SetOptions.mergeFields(listOf("uuid", "email", "photoUrl", "phoneNumber", "displayName"))
        )
            .addOnSuccessListener { emitter.onComplete() }
            .addOnFailureListener { emitter.onError(it) }
    }.subscribeOn(Schedulers.io())
}