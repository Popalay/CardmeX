package com.popalay.cardme.api.core.model

import java.util.*

sealed class Request(
    open val id: String,
    open val fromUserUuid: String,
    open val toUserUuid: String,
    open val createdDate: Date,
    val type: String
) {

    data class AddCardRequest(
        override val id: String,
        override val fromUserUuid: String,
        override val toUserUuid: String,
        override val createdDate: Date
    ) : Request(id, fromUserUuid, toUserUuid, createdDate, "ADD_CARD")
}