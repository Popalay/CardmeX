package com.popalay.cardme.remote.mapper

import com.popalay.cardme.api.mapper.Mapper
import com.popalay.cardme.api.model.User
import com.popalay.cardme.remote.model.RemoteUser

internal class RemoteUserToUserMapper(
    private val cardMapper: RemoteCardToCardMapper
) : Mapper<RemoteUser, User> {

    override fun apply(value: RemoteUser): User = User(
        uuid = value.uuid,
        email = value.email,
        photoUrl = value.photoUrl,
        phoneNumber = value.phoneNumber,
        displayName = value.displayName,
        card = value.card?.let { cardMapper(it) }
    )
}