package com.popalay.cardme.api.remote.dao

import com.gojuno.koptional.Optional
import com.popalay.cardme.api.core.model.Notification
import io.reactivex.Flowable

interface RemoteNotificationDao {

    fun getAll(userId: String): Flowable<List<Notification>>

    fun get(id: String): Flowable<Optional<Notification>>
}