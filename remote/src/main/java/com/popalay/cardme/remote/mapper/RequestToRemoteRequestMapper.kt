package com.popalay.cardme.remote.mapper

import com.google.firebase.Timestamp
import com.popalay.cardme.api.core.mapper.Mapper
import com.popalay.cardme.api.core.model.Request
import com.popalay.cardme.remote.model.RemoteRequest

internal class RequestToRemoteRequestMapper : Mapper<Request, RemoteRequest> {

    override fun apply(value: Request): RemoteRequest = RemoteRequest(
        id = value.id,
        fromUserUuid = value.fromUserUuid,
        toUserUuid = value.toUserUuid,
        type = value.type,
        createdDate = Timestamp(value.createdDate)
    )
}