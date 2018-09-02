package com.popalay.cardme.remote.datasource

import com.gojuno.koptional.Optional
import com.google.firebase.firestore.FirebaseFirestore
import com.popalay.cardme.api.data.Data
import com.popalay.cardme.api.data.Source
import com.popalay.cardme.api.data.datasource.UserRemoteDataSource
import com.popalay.cardme.api.data.key.EmptyKey
import com.popalay.cardme.api.model.User
import com.popalay.cardme.remote.mapper.RemoteCardToCardMapper
import com.popalay.cardme.remote.mapper.RemoteUserToUserMapper
import com.popalay.cardme.remote.model.RemoteUser
import com.popalay.cardme.remote.users
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.schedulers.Schedulers

class UserRemoteDataSource internal constructor(
    private val mapper: RemoteUserToUserMapper
) : UserRemoteDataSource {

    override fun flow(key: UserRemoteDataSource.Key): Flowable<Data<User?>> = Flowable.create<Data<User?>>({ emitter ->
        FirebaseFirestore.getInstance().users.document(key.userId)
            .addSnapshotListener { snapshot, exception ->
                if (exception != null) emitter.onError(exception)
                if (snapshot != null) {
                    val card = snapshot.toObject(RemoteUser::class.java)?.let { mapper(it) }
                    emitter.onNext(Data(card, Source.Network))
                }
            }
    }, BackpressureStrategy.LATEST)
        .onErrorReturnItem(Data(null, Source.Network))
        .subscribeOn(Schedulers.io())
}