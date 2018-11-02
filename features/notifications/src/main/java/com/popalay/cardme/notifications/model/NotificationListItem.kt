package com.popalay.cardme.notifications.model

import com.popalay.cardme.api.core.model.Notification
import com.popalay.cardme.core.adapter.Identifiable

data class NotificationListItem(
    val notification: Notification
) : Identifiable {

    override val id: String = notification.id
}