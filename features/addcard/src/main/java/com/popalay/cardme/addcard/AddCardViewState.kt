package com.popalay.cardme.addcard

import com.popalay.cardme.api.core.model.CardType
import com.popalay.cardme.api.core.model.User
import com.popalay.cardme.api.ui.state.ViewState

data class AddCardViewState(
    val number: String = "",
    val holderName: String = "",
    val isHolderNameEditable: Boolean = true,
    val isPublic: Boolean = true,
    val isPublicEditable: Boolean = true,
    val saveProgress: Boolean = false,
    val peopleProgress: Boolean = false,
    val saved: Boolean = false,
    val isValid: Boolean = false,
    val cardType: CardType = CardType.UNKNOWN,
    val users: List<User>? = null,
    val error: Throwable? = null
) : ViewState