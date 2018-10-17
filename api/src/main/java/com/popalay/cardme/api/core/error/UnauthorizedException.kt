package com.popalay.cardme.api.core.error

class UnauthorizedException(override val cause: Throwable? = null) : AppException("User is not authorized", cause)