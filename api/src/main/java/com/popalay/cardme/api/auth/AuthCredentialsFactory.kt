package com.popalay.cardme.api.auth

interface AuthCredentialsFactory {

    fun build(): AuthCredentials
}