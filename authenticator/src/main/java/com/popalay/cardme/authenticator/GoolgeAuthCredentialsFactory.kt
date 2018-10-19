package com.popalay.cardme.authenticator

import com.popalay.cardme.api.auth.AuthCredentials
import com.popalay.cardme.api.auth.AuthCredentialsFactory

class GoolgeAuthCredentialsFactory : AuthCredentialsFactory {

    override fun build(): AuthCredentials = CardmeAuthCredentials.Google
}