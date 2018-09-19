package com.popalay.cardme.remote.mapper

import com.popalay.cardme.api.core.mapper.Mapper
import com.popalay.cardme.api.core.model.Holder
import com.popalay.cardme.remote.model.RemoteHolder

internal class RemoteHolderToHolderMapper : Mapper<RemoteHolder, Holder> {

    override fun apply(value: RemoteHolder): Holder = Holder(
        id = value.id,
        name = value.name
    )
}