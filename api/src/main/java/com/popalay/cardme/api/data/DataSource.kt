package com.popalay.cardme.api.data

import io.reactivex.Flowable

interface DataSource<T, S : Source, K : Key> {

    fun flow(key: K): Flowable<Data<T>>
}