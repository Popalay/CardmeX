package com.popalay.cardme.cache.model

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "holders")
internal data class CacheHolder(
    @PrimaryKey(autoGenerate = true) val id: Long,
    val name: String
)