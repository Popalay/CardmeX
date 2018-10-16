package com.popalay.cardme.api.data.repository

import com.popalay.cardme.api.core.model.Card
import com.popalay.cardme.api.core.model.Request
import io.reactivex.Completable
import io.reactivex.Flowable

interface RequestRepository {

    fun save(data: Request): Completable
}