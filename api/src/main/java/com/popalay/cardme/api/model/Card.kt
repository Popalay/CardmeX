package com.popalay.cardme.api.model

data class Card(
    val uuid: String,
    val number: String,
    val holder: Holder,
    val isPublic: Boolean,
    val createdDate: Long,
    val updatedDate: Long
)