package com.popalay.cardme.main

import com.gojuno.koptional.None
import com.gojuno.koptional.Optional
import com.popalay.cardme.api.model.User
import com.popalay.cardme.base.state.ViewState

internal data class MainViewState(
    val user: Optional<User>,
    val isProgress: Boolean,
    val error: Throwable?
) : ViewState {

    companion object {

        fun idle() = MainViewState(
            user = None,
            isProgress = false,
            error = null
        )
    }
}