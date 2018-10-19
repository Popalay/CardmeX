package com.popalay.cardme.authenticator

import android.content.Intent
import com.popalay.cardme.api.auth.AuthResult
import com.popalay.cardme.api.auth.AuthResultFactory

class GoogleAuthResultFactory : AuthResultFactory {

    override fun build(success: Boolean, requestCode: Int, data: Any): AuthResult =
        CardmeAuthResult.Google(success, requestCode, data as Intent)
}