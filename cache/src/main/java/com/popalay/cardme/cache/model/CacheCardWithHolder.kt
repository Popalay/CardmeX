package com.popalay.cardme.cache.model

import androidx.room.Embedded
import java.util.Date

internal data class CacheCardWithHolder(
    val id: String,
    val number: String,
    @Embedded(prefix = "holder_") val holder: CacheHolder,
    val isPublic: Boolean,
    val cardType: String,
    val userId: String,
    val createdDate: Date,
    val updatedDate: Date
)