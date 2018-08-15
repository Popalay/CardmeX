package com.popalay.cardme.addcard

import com.popalay.cardme.api.state.Intent

sealed class AddCardIntent : Intent {

    data class NumberChanged(val number: String, val name: String, val isPublic: Boolean) : AddCardIntent()
    data class NameChanged(val number: String, val name: String, val isPublic: Boolean) : AddCardIntent()
    data class SaveClicked(val number: String, val name: String, val isPublic: Boolean) : AddCardIntent()
    data class IsPublicChanged(val isPublic: Boolean) : AddCardIntent()
}