package com.popalay.cardme.addcard

import com.popalay.cardme.api.model.CardType
import com.popalay.cardme.api.state.ViewState

data class AddCardViewState(
    val number: String = "",
    val holderName: String = "",
    val isHolderNameEditable: Boolean = true,
    val isPublic: Boolean = true,
    val isPublicEditable: Boolean = true,
    val progress: Boolean = false,
    val saved: Boolean = false,
    val isValid: Boolean = false,
    val cardType: CardType = CardType.UNKNOWN,
    val error: Throwable? = null
) : ViewState