package com.popalay.cardme.api.cache.dao

import com.popalay.cardme.api.core.model.Card
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single

interface CacheCardDao {

    fun save(data: Card): Completable

    fun saveAll(data: List<Card>): Completable

    fun get(id: String): Flowable<Card>

    fun getAll(): Flowable<List<Card>>

    fun delete(id: String): Completable

    fun isPresent(id: String): Single<Boolean>

    fun isNotEmpty(): Single<Boolean>
}