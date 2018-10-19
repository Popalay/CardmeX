package com.popalay.cardme.api.data.repository

import io.reactivex.Completable

interface NotificationRepository {

    fun syncToken(): Completable

    fun syncToken(token: String): Completable
}