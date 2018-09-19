package com.popalay.cardme.api.core.mapper

import io.reactivex.functions.Function

interface Mapper<T, R> : Function<T, R> {

    operator fun invoke(value: T) = apply(value)

    override fun apply(value: T): R
}