package com.popalay.cardme.cache.model

import android.arch.persistence.room.PrimaryKey

internal data class Holder(
    @PrimaryKey(autoGenerate = true) val id: String,
    val name: String
)