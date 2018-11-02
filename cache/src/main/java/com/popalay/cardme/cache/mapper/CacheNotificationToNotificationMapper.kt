package com.popalay.cardme.cache.mapper

import com.popalay.cardme.api.core.mapper.Mapper
import com.popalay.cardme.api.core.model.Notification
import com.popalay.cardme.cache.model.CacheNotification

internal class CacheNotificationToNotificationMapper(
    private val cacheUserMapper: CacheUserToUserMapper
) : Mapper<CacheNotification, Notification> {

    override fun apply(value: CacheNotification): Notification = Notification(
        id = value.id,
        description = value.description,
        title = value.title,
        fromUser = cacheUserMapper(value.fromUser),
        toUserUuid = value.toUserUuid,
        type = value.type,
        createdDate = value.createdDate
    )
}