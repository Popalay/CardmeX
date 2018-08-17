package com.popalay.cardme.remote.persister

import com.google.android.gms.tasks.Tasks
import com.google.firebase.firestore.FirebaseFirestore
import com.popalay.cardme.api.data.Key
import com.popalay.cardme.api.data.persister.CardRemotePersister
import com.popalay.cardme.api.model.Card
import com.popalay.cardme.remote.cards
import com.popalay.cardme.remote.mapper.CardToRemoteCardMapper
import io.reactivex.Completable
import io.reactivex.schedulers.Schedulers

class CardRemotePersister internal constructor(
    private val cardMapper: CardToRemoteCardMapper
) : CardRemotePersister {

    override fun persist(key: Key, data: Card): Completable = Completable.fromAction {
        val userId = "user_id_1"
        Tasks.await(FirebaseFirestore.getInstance().cards.add(cardMapper(data)))
    }.subscribeOn(Schedulers.io())
}