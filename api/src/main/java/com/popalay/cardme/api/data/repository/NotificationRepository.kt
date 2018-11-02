package com.popalay.cardme.api.data.repository

import com.popalay.cardme.api.core.model.Notification
import io.reactivex.Flowable

interface NotificationRepository {

    fun getAll(userId: String): Flowable<List<Notification>>
}