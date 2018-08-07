package com.popalay.cardme.api.datasource

import io.reactivex.Flowable

interface DataSource<K : Key, T> {

    fun flow(key: K): Flowable<Data<T>>
}

data class Data<T>(
    val content: T,
    val source: Source
)

enum class Source {

    CACHE, NETWORK
}

interface Key

object EmptyKey : Key

data class IdentifiableKey(val id: String) : Key