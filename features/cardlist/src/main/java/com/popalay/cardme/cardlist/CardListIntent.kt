package com.popalay.cardme.cardlist

import com.popalay.cardme.api.core.model.Card
import com.popalay.cardme.api.ui.state.Intent

internal sealed class CardListIntent : Intent {

    object OnStart : CardListIntent()
    object OnCardActionsDialogDismissed : CardListIntent()
    data class OnCardClicked(val card: Card) : CardListIntent()
    data class OnCardLongClicked(val card: Card) : CardListIntent()
}