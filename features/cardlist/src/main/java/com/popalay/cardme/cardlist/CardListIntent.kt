package com.popalay.cardme.cardlist

import com.popalay.cardme.api.model.Card
import com.popalay.cardme.api.state.Intent

sealed class CardListIntent : Intent {

    object OnStart : CardListIntent()
    object OnAddCardClicked : CardListIntent()
    object OnAddCardDialogDismissed : CardListIntent()
    data class OnCardClicked(val card: Card) : CardListIntent()
    data class OnCardLongClicked(val card: Card) : CardListIntent()
}