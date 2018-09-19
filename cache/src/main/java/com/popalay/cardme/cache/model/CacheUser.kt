package com.popalay.cardme.cache.model

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
internal data class CacheUser(
    @PrimaryKey val uuid: String,
    val email: String,
    val photoUrl: String,
    val phoneNumber: String,
    val displayName: String,
    @Embedded val card: CacheCard?
)