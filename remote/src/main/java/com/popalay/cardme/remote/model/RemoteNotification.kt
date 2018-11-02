package com.popalay.cardme.remote.model

import com.google.firebase.Timestamp

internal data class RemoteNotification(
    val id: String = "",
    val description: String = "",
    val title: String = "",
    val fromUser: RemoteUser = RemoteUser(),
    val toUserUuid: String = "",
    val createdDate: Timestamp? = null,
    val type: String = ""
)