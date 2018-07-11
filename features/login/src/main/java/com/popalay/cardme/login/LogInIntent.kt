package com.popalay.cardme.login

import com.popalay.cardme.base.state.Intent

sealed class LogInIntent : Intent {

    data class PhoneNumberChanged(val phoneNumber: String) : LogInIntent()
    data class GetStartedClicked(val phoneNumber: String) : LogInIntent()
}