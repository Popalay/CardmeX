package com.popalay.cardme.api.cache.dao

import com.popalay.cardme.api.core.model.Notification
import io.reactivex.Completable
import io.reactivex.Flowable

interface CacheNotificationDao {

    fun getAll(userId: String): Flowable<List<Notification>>

    fun saveAll(data: List<Notification>): Completable
}