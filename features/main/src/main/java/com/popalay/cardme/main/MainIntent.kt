package com.popalay.cardme.main

import com.popalay.cardme.api.ui.state.Intent

internal sealed class MainIntent : Intent {

    object OnStarted : MainIntent()
    object OnUnsyncClicked : MainIntent()
    object OnSyncClicked : MainIntent()
    object OnUserClicked : MainIntent()
    data class OnActivityResult(val success: Boolean, val requestCode: Int, val data: android.content.Intent) : MainIntent()
}