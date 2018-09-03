package com.popalay.cardme.cardactions

import com.popalay.cardme.api.state.Intent

sealed class CardActionsIntent : Intent {

    object OnRemoveClicked : CardActionsIntent()
    object OnShareClicked : CardActionsIntent()
}