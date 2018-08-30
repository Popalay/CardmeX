package com.popalay.cardme.remote.datasource

import com.gojuno.koptional.Optional
import com.gojuno.koptional.toOptional
import com.google.firebase.firestore.FirebaseFirestore
import com.popalay.cardme.api.data.Data
import com.popalay.cardme.api.data.Source
import com.popalay.cardme.api.data.datasource.UserCardRemoteDataSource
import com.popalay.cardme.api.model.Card
import com.popalay.cardme.remote.mapper.RemoteCardToCardMapper
import com.popalay.cardme.remote.model.RemoteUser
import com.popalay.cardme.remote.users
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.schedulers.Schedulers

class UserCardRemoteDataSource internal constructor(
    private val mapper: RemoteCardToCardMapper
) : UserCardRemoteDataSource {

    override fun flow(key: UserCardRemoteDataSource.Key): Flowable<Data<Card?>> = Flowable.create<Optional<Card>>({ emitter ->
        FirebaseFirestore.getInstance().users.document(key.userId)
            .addSnapshotListener { snapshot, exception ->
                if (exception != null) emitter.onError(exception)
                if (snapshot != null) {
                    emitter.onNext(snapshot.toObject(RemoteUser::class.java)?.card?.let { mapper(it) }.toOptional())
                }
            }
    }, BackpressureStrategy.LATEST)
        .map { Data(it.toNullable(), Source.Network) }
        .subscribeOn(Schedulers.io())
}