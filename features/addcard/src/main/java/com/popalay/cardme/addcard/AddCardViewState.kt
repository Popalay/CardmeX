package com.popalay.cardme.addcard

import com.popalay.cardme.api.core.model.CardType
import com.popalay.cardme.api.core.model.User
import com.popalay.cardme.api.ui.state.ViewState

data class AddCardViewState(
    val holderName: String = "",
    val isHolderNameEditable: Boolean = true,

    val cardType: CardType = CardType.UNKNOWN,
    val cardNumber: String = "",
    val isCardNumberEditable: Boolean = true,

    val isPublic: Boolean = true,
    val isPublicEditable: Boolean = true,

    val saveProgress: Boolean = false,
    val requestProgress: Boolean = false,
    val peopleProgress: Boolean = false,

    val showClearButton: Boolean = false,
    val isValid: Boolean = false,
    val toastMessage: String? = null,

    val users: List<User>? = null,

    val error: Throwable? = null
) : ViewState