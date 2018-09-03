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
        Tasks.await(FirebaseFirestore.getInstance().cards.document(data.id).set(cardMapper(data)))
    }.subscribeOn(Schedulers.io())

    override fun delete(key: Key): Completable = if (key is CardRemotePersister.Key.ById) {
        Completable.create { emitter ->
            FirebaseFirestore.getInstance().cards.document(key.cardId).delete()
                .addOnCompleteListener { emitter.onComplete() }
                .addOnFailureListener { emitter.onError(it) }
        }.subscribeOn(Schedulers.io())
    } else throw IllegalArgumentException("Unsupported key")
}