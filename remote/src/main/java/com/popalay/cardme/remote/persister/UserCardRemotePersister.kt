package com.popalay.cardme.remote.persister

import com.google.android.gms.tasks.Tasks
import com.google.firebase.firestore.FirebaseFirestore
import com.popalay.cardme.api.data.Key
import com.popalay.cardme.api.data.persister.UserCardRemotePersister
import com.popalay.cardme.api.model.Card
import com.popalay.cardme.remote.mapper.CardToRemoteCardMapper
import com.popalay.cardme.remote.users
import io.reactivex.Completable
import io.reactivex.schedulers.Schedulers

class UserCardRemotePersister internal constructor(
    private val cardMapper: CardToRemoteCardMapper
) : UserCardRemotePersister {

    override fun persist(key: Key, data: Card): Completable = Completable.fromAction {
        val remoteCard = cardMapper(data)
        val map = mapOf(
            "card.id" to remoteCard.id,
            "card.number" to remoteCard.number,
            "card.holder.id" to remoteCard.holder.id,
            "card.holder.name" to remoteCard.holder.name,
            "card.isPublic" to remoteCard.isPublic,
            "card.cardType" to remoteCard.cardType,
            "card.userId" to remoteCard.userId,
            "card.createdDate" to remoteCard.createdDate,
            "card.updatedDate" to remoteCard.updatedDate
        )
        Tasks.await(FirebaseFirestore.getInstance().users.document(data.userId).update(map))
    }.subscribeOn(Schedulers.io())
}