package com.popalay.cardme.cache.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "cards")
internal data class CacheCard(
    @PrimaryKey(autoGenerate = true) val id: Long,
    val number: String,
    val holderId: Long,
    val isPublic: Boolean,
    val cardType: String,
    val createdDate: Date,
    val updatedDate: Date
)