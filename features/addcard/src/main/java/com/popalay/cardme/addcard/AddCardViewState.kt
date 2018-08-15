package com.popalay.cardme.addcard

import com.popalay.cardme.api.state.ViewState

data class AddCardViewState(
    val number: String,
    val holderName: String,
    val isPublic: Boolean,
    val progress: Boolean,
    val saved: Boolean,
    val isValid: Boolean,
    val numberError: Throwable?
) : ViewState {

    companion object {

        fun idle() = AddCardViewState(
            number = "",
            holderName = "",
            isPublic = true,
            progress = false,
            saved = false,
            isValid = false,
            numberError = null
        )
    }
}