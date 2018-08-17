package com.popalay.cardme.remote.model

import com.google.firebase.Timestamp
import com.popalay.cardme.api.model.CardType

internal data class RemoteCard(
    val id: Long = -1L,
    val number: String = "",
    val holder: RemoteHolder = RemoteHolder(),
    val isPublic: Boolean = true,
    val cardType: String = CardType.UNKNOWN.name,
    val createdDate: Timestamp? = null,
    val updatedDate: Timestamp? = null
)