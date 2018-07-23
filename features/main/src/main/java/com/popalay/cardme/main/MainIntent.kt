package com.popalay.cardme.main

import com.popalay.cardme.api.state.Intent

internal sealed class MainIntent : Intent {

    object OnStarted : MainIntent()
    object OnUnsyncClicked : MainIntent()
}