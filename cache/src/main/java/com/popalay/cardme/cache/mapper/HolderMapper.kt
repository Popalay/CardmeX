package com.popalay.cardme.cache.mapper

import com.popalay.cardme.api.mapper.Mapper
import com.popalay.cardme.cache.model.Holder
import com.popalay.cardme.api.model.Card as ApiCard
import com.popalay.cardme.api.model.Holder as ApiHolder

internal class HolderMapper : Mapper<Holder, ApiHolder> {

    override fun apply(value: Holder): ApiHolder = ApiHolder(
        id = value.id,
        name = value.name
    )
}