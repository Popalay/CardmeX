package com.popalay.cardme.notifications;

import com.popalay.cardme.api.core.model.Notification
import com.popalay.cardme.api.ui.state.ViewState

internal data class NotificationsViewState(
    val notifications: List<Notification> = listOf(),
    val progress: Boolean = true,
    val error: Throwable? = null
) : ViewState