package com.popalay.cardme.core.extensions

fun <T> tryOrNull(block: () -> T): T? {
    return try {
        block()
    } catch (ex: Exception) {
        null
    }
}