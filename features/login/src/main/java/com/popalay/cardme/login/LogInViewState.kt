package com.popalay.cardme.login

import com.popalay.cardme.base.state.ViewState

data class LogInViewState(
    val phoneNumber: String,
    val canStart: Boolean,
    val error: Throwable?
) : ViewState {

    companion object {

        fun idle() = LogInViewState(
            phoneNumber = "",
            canStart = false,
            error = null
        )
    }
}