package com.popalay.cardme.main

import com.gojuno.koptional.None
import com.gojuno.koptional.Optional
import com.popalay.cardme.api.core.model.User
import com.popalay.cardme.api.ui.state.ViewState

internal data class MainViewState(
    val user: Optional<User> = None,
    val isSyncProgress: Boolean = false,
    val showAddCardDialog: Boolean = false,
    val error: Throwable? = null
) : ViewState