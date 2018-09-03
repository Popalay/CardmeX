package com.popalay.cardme.api.data

import io.reactivex.Flowable

interface DataSource<T, S : Source, K : Key> {

    fun flowSingle(key: K): Flowable<Data<T>> = throw UnsupportedOperationException()

    fun flowList(key: K): Flowable<Data<List<T>>> = throw UnsupportedOperationException()
}