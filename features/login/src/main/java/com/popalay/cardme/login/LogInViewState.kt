package com.popalay.cardme.login

import com.gojuno.koptional.None
import com.gojuno.koptional.Optional
import com.popalay.cardme.api.model.User
import com.popalay.cardme.api.state.ViewState

internal data class LogInViewState(
    val user: Optional<User>,
    val isProgress: Boolean,
    val canStart: Boolean,
    val error: Throwable?
) : ViewState {

    companion object {

        fun idle() = LogInViewState(
            user = None,
            isProgress = false,
            canStart = false,
            error = null
        )
    }
}