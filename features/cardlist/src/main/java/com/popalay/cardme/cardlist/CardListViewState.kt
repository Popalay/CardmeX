package com.popalay.cardme.cardlist

import com.popalay.cardme.api.core.model.Card
import com.popalay.cardme.api.ui.state.ViewState

internal data class CardListViewState(
    val cards: List<Card> = listOf(),
    val progress: Boolean = false,
    val selectedCard: Card? = null,
    val toastMessage: String? = null,
    val error: Throwable? = null
) : ViewState