package com.popalay.cardme.login

import com.popalay.cardme.base.state.Intent

internal sealed class LogInIntent : Intent {

    object GoogleLogInClicked : LogInIntent()
    data class OnActivityResult(val requestCode: Int, val data: android.content.Intent) : LogInIntent()
}