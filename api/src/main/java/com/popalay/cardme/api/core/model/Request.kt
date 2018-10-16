package com.popalay.cardme.api.core.model

sealed class Request(
    open val id: String,
    open val fromUserUuid: String,
    open val toUserUuid: String,
    val type: String
) {

    data class AddCardRequest(
        override val id: String,
        override val fromUserUuid: String,
        override val toUserUuid: String
    ) : Request(id, fromUserUuid, toUserUuid, "ADD_CARD")
}