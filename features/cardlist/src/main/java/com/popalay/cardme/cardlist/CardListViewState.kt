package com.popalay.cardme.cardlist

import com.popalay.cardme.api.model.Card
import com.popalay.cardme.api.state.ViewState

internal data class CardListViewState(
    val cards: List<Card> = listOf(),
    val progress: Boolean = false,
    val showAddCardDialog: Boolean = false,
    val selectedCard: Card? = null,
    val toastMessage: String? = null,
    val showToast: Boolean = false,
    val error: Throwable? = null
) : ViewState