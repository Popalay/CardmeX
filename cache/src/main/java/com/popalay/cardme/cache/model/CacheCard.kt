package com.popalay.cardme.cache.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "cards")
internal data class CacheCard(
    @PrimaryKey val id: String,
    val number: String,
    val holderId: String,
    val isPublic: Boolean,
    val cardType: String,
    val userId: String,
    val createdDate: Date,
    val updatedDate: Date
)