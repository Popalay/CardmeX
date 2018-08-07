package com.popalay.cardme.cache.model

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import java.util.Date

@Entity
internal data class Card(
    @PrimaryKey(autoGenerate = true) val id: String,
    val number: String,
    val holderId: String,
    val isPublic: Boolean,
    val createdDate: Date,
    val updatedDate: Date
)