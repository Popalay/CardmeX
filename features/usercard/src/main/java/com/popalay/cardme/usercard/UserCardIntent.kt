package com.popalay.cardme.usercard

import com.popalay.cardme.api.core.model.Card
import com.popalay.cardme.api.ui.state.Intent

sealed class UserCardIntent : Intent {

    object OnEditClicked : UserCardIntent()
    data class OnShareClicked(val card: Card?) : UserCardIntent()
    data class OnCardClicked(val card: Card?) : UserCardIntent()
    object OnAddClicked : UserCardIntent()
    object OnStart : UserCardIntent()
}