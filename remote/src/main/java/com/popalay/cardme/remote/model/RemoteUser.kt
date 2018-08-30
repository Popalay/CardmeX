package com.popalay.cardme.remote.model

internal data class RemoteUser(
    val uuid: String = "",
    val email: String = "",
    val photoUrl: String = "",
    val phoneNumber: String = "",
    val displayName: String = "",
    val card: RemoteCard? = null
)