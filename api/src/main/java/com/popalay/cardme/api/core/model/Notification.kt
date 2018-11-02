package com.popalay.cardme.api.core.model

import java.util.*

data class Notification(
    val id: String,
    val description: String,
    val title: String,
    val fromUser: User,
    val toUserUuid: String,
    val createdDate: Date,
    val type: String
)