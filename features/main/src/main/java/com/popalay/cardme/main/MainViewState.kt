package com.popalay.cardme.main

import com.gojuno.koptional.None
import com.gojuno.koptional.Optional
import com.popalay.cardme.api.core.model.User
import com.popalay.cardme.api.ui.state.ViewState

internal data class MainViewState(
    val user: Optional<User>,
    val isSyncProgress: Boolean,
    val error: Throwable?
) : ViewState {

    companion object {

        fun idle() = MainViewState(
            user = None,
            isSyncProgress = false,
            error = null
        )
    }
}