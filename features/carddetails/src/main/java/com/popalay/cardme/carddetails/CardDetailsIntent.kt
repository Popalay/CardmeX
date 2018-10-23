package com.popalay.cardme.carddetails

import com.popalay.cardme.api.core.model.Card
import com.popalay.cardme.api.ui.state.Intent

sealed class CardDetailsIntent : Intent {

    data class OnAddClicked(val card: Card) : CardDetailsIntent()
    data class OnCardClicked(val card: Card) : CardDetailsIntent()
    object OnStart : CardDetailsIntent()
}