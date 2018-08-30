package com.popalay.cardme.data.repository

import com.gojuno.koptional.Optional
import com.popalay.cardme.api.data.datasource.UserCardRemoteDataSource
import com.popalay.cardme.api.data.key.EmptyKey
import com.popalay.cardme.api.data.persister.UserCardRemotePersister
import com.popalay.cardme.api.data.persister.UserRemotePersister
import com.popalay.cardme.api.model.Card
import com.popalay.cardme.api.model.User
import com.popalay.cardme.api.repository.UserRepository
import io.reactivex.Completable
import io.reactivex.Flowable

class UserRepository(
    private val user: Optional<User>,
    private val userRemotePersister: UserRemotePersister,
    private val userCardRemotePersister: UserCardRemotePersister,
    private val userCardRemoteDataSource: UserCardRemoteDataSource
) : UserRepository {

    override fun save(user: User): Completable = userRemotePersister.persist(EmptyKey, user)

    override fun updateUserCard(card: Card): Completable = userCardRemotePersister.persist(EmptyKey, card)

    override fun getUserCard(): Flowable<Card> =
        userCardRemoteDataSource.flow(UserCardRemoteDataSource.Key(requireNotNull(user.toNullable()).uuid))
            .map { it.content }
}