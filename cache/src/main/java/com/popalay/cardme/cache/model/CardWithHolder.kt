package com.popalay.cardme.cache.model

import android.arch.persistence.room.Relation
import java.util.Date

internal data class CardWithHolder(
    val id: String,
    val number: String,
    @Relation(parentColumn = "id", entityColumn = "holderId") val holder: Holder,
    val isPublic: Boolean,
    val createdDate: Date,
    val updatedDate: Date
)