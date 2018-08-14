package com.popalay.cardme.cache.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "holders")
internal data class CacheHolder(
    @PrimaryKey(autoGenerate = true) val id: Long,
    val name: String
)