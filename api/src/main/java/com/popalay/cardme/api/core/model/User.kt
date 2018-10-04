package com.popalay.cardme.api.core.model

data class User(
    val uuid: String,
    val email: String,
    val photoUrl: String,
    val phoneNumber: String,
    val displayName: DisplayName,
    val card: Card?
)

inline class DisplayName(val value: String)