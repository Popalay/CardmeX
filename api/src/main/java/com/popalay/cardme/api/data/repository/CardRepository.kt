package com.popalay.cardme.api.data.repository

import com.popalay.cardme.api.core.model.Card
import io.reactivex.Completable
import io.reactivex.Flowable

interface CardRepository {

    fun save(data: Card): Completable

    fun saveAll(data: List<Card>): Completable

    fun get(id: String): Flowable<Card>

    fun getAll(userId: String): Flowable<List<Card>>

    fun delete(id: String): Completable
}