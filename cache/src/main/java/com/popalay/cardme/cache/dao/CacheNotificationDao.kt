package com.popalay.cardme.cache.dao

import com.popalay.cardme.api.cache.dao.CacheNotificationDao
import com.popalay.cardme.api.core.model.Notification
import com.popalay.cardme.cache.database.dao.NotificationDao
import com.popalay.cardme.cache.mapper.CacheNotificationToNotificationMapper
import com.popalay.cardme.cache.mapper.NotificationToCacheNotificationMapper
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.schedulers.Schedulers

internal class CacheNotificationDao(
    private val notificationDao: NotificationDao,
    private val cacheNotificationMapper: CacheNotificationToNotificationMapper,
    private val notificationMapper: NotificationToCacheNotificationMapper
) : CacheNotificationDao {

    override fun getAll(userId: String): Flowable<List<Notification>> = notificationDao.findAll(userId)
        .map { it.map(cacheNotificationMapper::invoke) }
        .subscribeOn(Schedulers.io())

    override fun saveAll(data: List<Notification>): Completable =
        notificationDao.insertAll(data.map(notificationMapper::invoke))
            .subscribeOn(Schedulers.io())
}