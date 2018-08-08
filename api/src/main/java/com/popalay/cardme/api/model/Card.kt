package com.popalay.cardme.api.model

import java.util.Date

data class Card(
    val id: Long,
    val number: String,
    val holder: Holder,
    val isPublic: Boolean,
    val createdDate: Date,
    val updatedDate: Date
)