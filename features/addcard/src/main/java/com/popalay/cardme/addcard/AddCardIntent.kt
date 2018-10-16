package com.popalay.cardme.addcard

import com.popalay.cardme.api.core.model.CardType
import com.popalay.cardme.api.core.model.User
import com.popalay.cardme.api.ui.state.Intent

sealed class AddCardIntent : Intent {

    object OnStart : AddCardIntent()
    object CameraClicked : AddCardIntent()
    object CrossClicked : AddCardIntent()
    data class NumberChanged(val number: String) : AddCardIntent()
    data class NameChanged(val name: String) : AddCardIntent()
    data class IsPublicChanged(val isPublic: Boolean) : AddCardIntent()
    data class SaveClicked(
        val number: String,
        val name: String,
        val isPublic: Boolean,
        val cardType: CardType
    ) : AddCardIntent()

    data class OnUserClicked(val user: User) : AddCardIntent()
}