package com.popalay.cardme.cardlist

import com.popalay.cardme.api.model.Card
import com.popalay.cardme.api.state.ViewState

internal data class CardListViewState(
    val cards: List<Card>,
    val progress: Boolean,
    val showAddCardDialog: Boolean,
    val error: Throwable?
) : ViewState {

    companion object {

        fun idle() = CardListViewState(
            listOf(),
            true,
            false,
            null
        )
    }
}