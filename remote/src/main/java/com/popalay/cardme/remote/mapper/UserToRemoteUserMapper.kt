package com.popalay.cardme.remote.mapper

import com.popalay.cardme.api.core.mapper.Mapper
import com.popalay.cardme.api.core.model.User
import com.popalay.cardme.remote.model.RemoteUser

internal class UserToRemoteUserMapper : Mapper<User, RemoteUser> {

    override fun apply(value: User): RemoteUser = RemoteUser(
        uuid = value.uuid,
        email = value.email,
        photoUrl = value.photoUrl,
        phoneNumber = value.phoneNumber,
        displayName = value.displayName.value,
        cardId = value.cardId
    )
}