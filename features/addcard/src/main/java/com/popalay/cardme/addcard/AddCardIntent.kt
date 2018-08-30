package com.popalay.cardme.addcard

import com.popalay.cardme.api.model.CardType
import com.popalay.cardme.api.state.Intent

sealed class AddCardIntent : Intent {

    object OnStart: AddCardIntent()
    data class NumberChanged(val number: String, val name: String, val isPublic: Boolean) : AddCardIntent()
    data class NameChanged(val number: String, val name: String, val isPublic: Boolean) : AddCardIntent()
    data class SaveClicked(val number: String, val name: String, val isPublic: Boolean, val cardType: CardType) : AddCardIntent()
    data class IsPublicChanged(val isPublic: Boolean) : AddCardIntent()
}