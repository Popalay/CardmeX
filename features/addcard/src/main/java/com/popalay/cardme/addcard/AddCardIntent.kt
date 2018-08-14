package com.popalay.cardme.addcard

import com.popalay.cardme.api.state.Intent

sealed class AddCardIntent : Intent {

    data class NumberChanged(val number: String) : AddCardIntent()
    data class NameChanged(val name: String) : AddCardIntent()
    data class IsPublicChanged(val isPublic: Boolean) : AddCardIntent()
    data class SaveClicked(val number: String, val name: String, val isPublic: Boolean) : AddCardIntent()
}