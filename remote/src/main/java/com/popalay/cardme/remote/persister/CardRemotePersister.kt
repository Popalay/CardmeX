package com.popalay.cardme.remote.persister

import com.google.android.gms.tasks.Tasks
import com.popalay.cardme.api.data.Key
import com.popalay.cardme.api.data.persister.CardCachePersister
import com.popalay.cardme.api.model.Card
import com.popalay.cardme.remote.cardFirestore
import com.popalay.cardme.remote.mapper.CardToRemoteCardMapper
import io.reactivex.Completable
import io.reactivex.schedulers.Schedulers

class CardRemotePersister internal constructor(
    private val cardMapper: CardToRemoteCardMapper
) : CardCachePersister {

    override fun persist(key: Key, data: Card): Completable = Completable.fromAction {
        Tasks.await(cardFirestore.add(cardMapper(data)))
    }.subscribeOn(Schedulers.io())
}