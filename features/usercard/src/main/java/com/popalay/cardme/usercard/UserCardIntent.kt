package com.popalay.cardme.usercard

import com.popalay.cardme.api.state.Intent

sealed class UserCardIntent : Intent {

    object OnEditClicked : UserCardIntent()
}