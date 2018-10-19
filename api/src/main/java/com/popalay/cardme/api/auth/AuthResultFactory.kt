package com.popalay.cardme.api.auth

interface AuthResultFactory {

    fun build(success: Boolean, requestCode: Int, data: Any): AuthResult
}