package com.popalay.cardme.cardlist

import com.popalay.cardme.api.state.Intent

sealed class CardListIntent : Intent {

    object OnStart : CardListIntent()
    object OnAddCardClicked : CardListIntent()
    object OnAddCardDialogDismissed : CardListIntent()
}