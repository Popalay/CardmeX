package com.popalay.cardme.core.extensions

import com.popalay.cardme.api.core.model.Card
import com.popalay.cardme.api.core.model.CardType
import com.popalay.cardme.core.R

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

val CardType.icon: Int
    get() = when (this) {
        CardType.MASTER_CARD -> R.drawable.ic_mastercard
        CardType.VISA -> R.drawable.ic_visa
        else -> 0
    }