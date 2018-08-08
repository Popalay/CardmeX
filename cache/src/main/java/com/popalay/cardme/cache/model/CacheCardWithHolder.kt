package com.popalay.cardme.cache.model

import java.util.Date

internal data class CacheCardWithHolder(
    val id: Long,
    val number: String,
    /*@Relation(parentColumn = "id", entityColumn = "holderId")*/ val holder: CacheHolder,
    val isPublic: Boolean,
    val createdDate: Date,
    val updatedDate: Date
)