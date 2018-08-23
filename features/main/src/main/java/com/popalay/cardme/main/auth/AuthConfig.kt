package com.popalay.cardme.main.auth

import android.content.Intent
import com.popalay.cardme.api.auth.AuthCredentials
import com.popalay.cardme.api.auth.AuthResult

internal sealed class CardmeAuthCredentials : AuthCredentials {
    data class Phone(val phoneNumber: String) : CardmeAuthCredentials()
    object Google : CardmeAuthCredentials()
}

internal sealed class CardmeAuthResult : AuthResult {
    data class Phone(val code: String) : CardmeAuthResult()
    data class Google(val success: Boolean, val requestCode: Int, val data: Intent) : CardmeAuthResult()
}