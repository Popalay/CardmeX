package com.popalay.cardme.api.core.error

abstract class AppException(override val message: String, override val cause: Throwable? = null) : RuntimeException()