package com.popalay.cardme.remote.mapper

import com.popalay.cardme.api.core.mapper.Mapper
import com.popalay.cardme.api.core.model.DisplayName
import com.popalay.cardme.api.core.model.User
import com.popalay.cardme.remote.model.RemoteUser

internal class RemoteUserToUserMapper : Mapper<RemoteUser, User> {

    override fun apply(value: RemoteUser): User = User(
        uuid = value.uuid,
        email = value.email,
        photoUrl = value.photoUrl,
        phoneNumber = value.phoneNumber,
        displayName = DisplayName(value.displayName),
        cardId = value.cardId
    )
}