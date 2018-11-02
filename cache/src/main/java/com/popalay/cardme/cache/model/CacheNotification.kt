package com.popalay.cardme.cache.model

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "notification")
internal data class CacheNotification(
    @PrimaryKey val id: String,
    val description: String,
    val title: String,
    @Embedded val fromUser: CacheUser,
    val toUserUuid: String,
    val createdDate: Date,
    val type: String
)