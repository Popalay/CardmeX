package com.popalay.cardme.usercard

import com.popalay.cardme.api.state.Intent

sealed class UserCardIntent : Intent {

    object OnEditClicked : UserCardIntent()
    object OnAddClicked : UserCardIntent()
    object OnSkipClicked : UserCardIntent()
    data class OnAddCardDialogDismissed(val isCardSaved: Boolean) : UserCardIntent()
    object OnStart : UserCardIntent()
}