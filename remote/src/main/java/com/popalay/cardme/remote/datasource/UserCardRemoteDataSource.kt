package com.popalay.cardme.remote.datasource

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

    override fun flow(key: UserCardRemoteDataSource.Key): Flowable<Data<Card?>> = Flowable.create<Data<Card?>>({ emitter ->
        FirebaseFirestore.getInstance().users.document(key.userId)
            .addSnapshotListener { snapshot, exception ->
                if (exception != null) emitter.onError(exception)
                if (snapshot != null) {
                    val card = snapshot.toObject(RemoteUser::class.java)?.card?.let { mapper(it) }
                    emitter.onNext(Data(card, Source.Network))
                }
            }
    }, BackpressureStrategy.LATEST)
        .onErrorReturnItem(Data(null, Source.Network))
        .subscribeOn(Schedulers.io())
}