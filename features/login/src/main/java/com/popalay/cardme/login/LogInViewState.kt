package com.popalay.cardme.login

import com.gojuno.koptional.None
import com.gojuno.koptional.Optional
import com.popalay.cardme.api.model.User
import com.popalay.cardme.base.state.ViewState

data class LogInViewState(
    val user: Optional<User>,
    val isProgress:Boolean,
    val error: Throwable?
) : ViewState {

    companion object {

        fun idle() = LogInViewState(
            user = None,
            isProgress = false,
            error = null
        )
    }
}