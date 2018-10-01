package com.popalay.cardme.cache.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "holders")
internal data class CacheHolder(
    @PrimaryKey val id: String,
    val name: String,
    val photoUrl: String
)