package com.popalay.cardme.cache.mapper

import com.popalay.cardme.api.core.mapper.Mapper
import com.popalay.cardme.api.core.model.Holder
import com.popalay.cardme.cache.model.CacheHolder

internal class HolderToCacheHolderMapper : Mapper<Holder, CacheHolder> {

    override fun apply(value: Holder): CacheHolder = CacheHolder(
        id = value.id,
        name = value.name,
        photoUrl = value.photoUrl
    )
}