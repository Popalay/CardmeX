package com.popalay.cardme.cache.model

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import java.util.Date

@Entity
internal data class CacheCard(
    @PrimaryKey(autoGenerate = true) val id: Long,
    val number: String,
    val holderId: Long,
    val isPublic: Boolean,
    val createdDate: Date,
    val updatedDate: Date
)