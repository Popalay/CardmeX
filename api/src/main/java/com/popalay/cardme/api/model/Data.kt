package com.popalay.cardme.api.model

data class Data<T>(
    val content: T,
    val source: Source
)