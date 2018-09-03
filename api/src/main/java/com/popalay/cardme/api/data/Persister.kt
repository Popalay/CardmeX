package com.popalay.cardme.api.data

import io.reactivex.Completable

interface Persister<T, S : Source> {

    fun persist(key: Key, data: T): Completable = throw UnsupportedOperationException()

    fun persist(key: Key, data: List<T>): Completable = throw UnsupportedOperationException()
}