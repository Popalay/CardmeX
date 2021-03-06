package com.popalay.cardme.usercard

import com.popalay.cardme.api.core.model.Card
import com.popalay.cardme.api.core.model.Notification
import com.popalay.cardme.api.core.model.User
import com.popalay.cardme.api.ui.state.ViewState

internal data class UserCardViewState(
    val user: User? = null,
    val card: Card? = null,
    val notifications: List<Notification> = listOf(),
    val progress: Boolean = true,
    val toastMessage: String? = null,
    val error: Throwable? = null
) : ViewState