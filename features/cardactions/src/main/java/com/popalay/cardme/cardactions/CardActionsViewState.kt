package com.popalay.cardme.cardactions

import com.popalay.cardme.api.ui.state.ViewState

data class CardActionsViewState(
    val completed: Boolean = false,
    val error: Throwable? = null
) : ViewState