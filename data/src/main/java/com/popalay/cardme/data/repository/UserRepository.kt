package com.popalay.cardme.data.repository

import com.popalay.cardme.api.data.key.EmptyKey
import com.popalay.cardme.api.data.persister.UserCardRemotePersister
import com.popalay.cardme.api.data.persister.UserRemotePersister
import com.popalay.cardme.api.model.Card
import com.popalay.cardme.api.model.User
import com.popalay.cardme.api.repository.UserRepository
import io.reactivex.Completable

class UserRepository(
    private val userRemotePersister: UserRemotePersister,
    private val userCardRemotePersister: UserCardRemotePersister
) : UserRepository {

    override fun save(user: User): Completable = userRemotePersister.persist(EmptyKey, user)

    override fun updateUserCard(card: Card): Completable = userCardRemotePersister.persist(EmptyKey, card)
}