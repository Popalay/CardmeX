package com.popalay.cardme.api.repository

import com.gojuno.koptional.Optional
import com.popalay.cardme.api.model.Card
import io.reactivex.Completable
import io.reactivex.Flowable

interface CardRepository {

    fun save(card: Card): Completable

    fun get(id: String): Flowable<Optional<Card>>

    fun getAll(): Flowable<List<Card>>

    fun delete(id: String): Completable
}