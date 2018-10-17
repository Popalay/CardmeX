package com.popalay.cardme.main

import com.popalay.cardme.api.core.model.User
import com.popalay.cardme.api.ui.state.ViewState

internal data class MainViewState(
    val user: User? = null,
    val isSyncProgress: Boolean = false,
    val error: Throwable? = null
) : ViewState