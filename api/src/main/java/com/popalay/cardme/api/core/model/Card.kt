package com.popalay.cardme.api.core.model

import java.util.Date

data class Card(
    val id: String,
    val number: String,
    val holder: Holder,
    val isPublic: Boolean,
    val cardType: CardType,
    val userId: String,
    val createdDate: Date,
    val updatedDate: Date
)