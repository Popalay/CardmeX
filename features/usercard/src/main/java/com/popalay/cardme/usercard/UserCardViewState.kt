package com.popalay.cardme.usercard

import com.popalay.cardme.api.model.Card
import com.popalay.cardme.api.model.User
import com.popalay.cardme.api.state.ViewState

internal data class UserCardViewState(
    val user: User? = null,
    val card: Card? = null,
    val progress: Boolean = true,
    val showAddCardDialog: Boolean = false,
    val error: Throwable? = null
) : ViewState