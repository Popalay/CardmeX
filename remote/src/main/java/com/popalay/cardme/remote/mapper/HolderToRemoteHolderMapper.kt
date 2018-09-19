package com.popalay.cardme.remote.mapper

import com.popalay.cardme.api.core.mapper.Mapper
import com.popalay.cardme.api.core.model.Holder
import com.popalay.cardme.remote.model.RemoteHolder

internal class HolderToRemoteHolderMapper : Mapper<Holder, RemoteHolder> {

    override fun apply(value: Holder): RemoteHolder = RemoteHolder(
        id = value.id,
        name = value.name
    )
}