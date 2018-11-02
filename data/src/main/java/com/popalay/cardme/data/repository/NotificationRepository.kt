package com.popalay.cardme.data.repository

import com.popalay.cardme.api.cache.dao.CacheNotificationDao
import com.popalay.cardme.api.core.model.Notification
import com.popalay.cardme.api.data.repository.NotificationRepository
import com.popalay.cardme.api.remote.dao.RemoteNotificationDao
import io.reactivex.Flowable
import io.reactivex.schedulers.Schedulers

internal class NotificationRepository(
    private val cacheNotificationDao: CacheNotificationDao,
    private val remoteNotificationDao: RemoteNotificationDao
) : BaseRepository(), NotificationRepository {

    override fun getAll(userId: String): Flowable<List<Notification>> =
        flow(
            cacheNotificationDao.getAll(userId),
            remoteNotificationDao.getAll(userId),
            cacheNotificationDao::saveAll
        ).subscribeOn(Schedulers.io())
}
