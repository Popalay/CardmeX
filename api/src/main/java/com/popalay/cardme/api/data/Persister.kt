package com.popalay.cardme.api.data

import io.reactivex.Completable

interface Persister<T, S : Source> {

    val source: S

    fun persist(key: Key, data: T): Completable
}