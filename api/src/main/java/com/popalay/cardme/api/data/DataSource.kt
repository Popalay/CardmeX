package com.popalay.cardme.api.data

import io.reactivex.Flowable

interface DataSource<T, S : Source> {

    fun flow(key: Key): Flowable<Data<T>>
}