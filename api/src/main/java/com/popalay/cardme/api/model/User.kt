package com.popalay.cardme.api.model

data class User(
    val uuid: String,
    val email: String,
    val photoUrl: String,
    val phoneNumber: String,
    val displayName: String,
    val card: Card?
)