package com.popalay.cardme.usercard

import com.popalay.cardme.api.ui.state.Intent

sealed class UserCardIntent : Intent {

    object OnEditClicked : UserCardIntent()
    object OnAddClicked : UserCardIntent()
    object OnSkipClicked : UserCardIntent()
    object OnAddCardDialogDismissed : UserCardIntent()
    object OnStart : UserCardIntent()
}