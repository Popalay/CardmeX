package com.popalay.cardme.remote.model

import com.google.firebase.Timestamp

class RemoteRequest(
    val id: String = "",
    val fromUserUuid: String = "",
    val toUserUuid: String = "",
    val type: String = "",
    val createdDate: Timestamp? = null
)