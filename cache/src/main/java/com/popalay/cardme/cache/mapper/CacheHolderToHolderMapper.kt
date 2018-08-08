package com.popalay.cardme.cache.mapper

import com.popalay.cardme.api.mapper.Mapper
import com.popalay.cardme.api.model.Holder
import com.popalay.cardme.cache.model.CacheHolder

internal class CacheHolderToHolderMapper : Mapper<CacheHolder, Holder> {

    override fun apply(value: CacheHolder): Holder = Holder(
        id = value.id,
        name = value.name
    )
}