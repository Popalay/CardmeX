package com.popalay.cardme.login

import com.popalay.cardme.api.state.Intent

internal sealed class LogInIntent : Intent {

    object GoogleLogInClicked : LogInIntent()
    data class OnActivityResult(val success: Boolean, val requestCode: Int, val data: android.content.Intent) : LogInIntent()
}