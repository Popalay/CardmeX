package com.popalay.cardme.remote.mapper

import com.popalay.cardme.api.core.mapper.Mapper
import com.popalay.cardme.api.core.model.Notification
import com.popalay.cardme.remote.model.RemoteNotification
import java.util.*

internal class RemoteNotificationToNotificationMapper(
    private val remoteUserMapper: RemoteUserToUserMapper
) : Mapper<RemoteNotification, Notification> {

    override fun apply(value: RemoteNotification): Notification = Notification(
        id = value.id,
        description = value.description,
        title = value.title,
        fromUser = remoteUserMapper(value.fromUser),
        toUserUuid = value.toUserUuid,
        type = value.type,
        createdDate = value.createdDate?.toDate() ?: Date()
    )
}