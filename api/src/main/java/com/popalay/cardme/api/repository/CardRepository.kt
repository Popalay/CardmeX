package com.popalay.cardme.api.repository

import com.popalay.cardme.api.model.Card
import io.reactivex.Completable
import io.reactivex.Flowable

interface CardRepository {

    fun save(card: Card): Completable

    fun getAll(): Flowable<List<Card>>
}