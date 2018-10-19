package com.popalay.cardme.authenticator

import android.content.Intent
import com.popalay.cardme.api.auth.AuthCredentials
import com.popalay.cardme.api.auth.AuthResult

sealed class CardmeAuthCredentials : AuthCredentials {
    data class Phone(val phoneNumber: String) : CardmeAuthCredentials()
    object Google : CardmeAuthCredentials()
}

sealed class CardmeAuthResult : AuthResult {
    data class Phone(val code: String) : CardmeAuthResult()
    data class Google(val success: Boolean, val requestCode: Int, val data: Intent) : CardmeAuthResult()
}