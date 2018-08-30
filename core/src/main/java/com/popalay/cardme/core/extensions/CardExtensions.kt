package com.popalay.cardme.core.extensions

import com.popalay.cardme.api.model.Card

val Card.formattedNumber: String
    get() {
        val result = StringBuilder()
        (0 until number.length).forEach {
            if (it % 4 == 0 && it != 0) {
                result.append(" ")
            }
            result.append(number[it])
        }
        return result.toString()
    }

val Card.securedNumber: String get() = "•••• ${number.takeLast(4)}"

