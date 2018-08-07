package com.popalay.cardme.cache.mapper

import com.popalay.cardme.api.mapper.Mapper
import com.popalay.cardme.api.model.Holder
import com.popalay.cardme.cache.model.CacheHolder

internal class RevertHolderMapper : Mapper<Holder, CacheHolder> {

    override fun apply(value: Holder): CacheHolder = CacheHolder(
        id = value.id,
        name = value.name
    )
}