package com.popalay.cardme.cache.mapper

import com.popalay.cardme.api.core.mapper.Mapper
import com.popalay.cardme.api.core.model.DisplayName
import com.popalay.cardme.api.core.model.User
import com.popalay.cardme.cache.model.CacheUser

internal class CacheUserToUserMapper : Mapper<CacheUser, User> {

    override fun apply(value: CacheUser): User = User(
        uuid = value.uuid,
        email = value.email,
        photoUrl = value.photoUrl,
        phoneNumber = value.phoneNumber,
        displayName = DisplayName(value.displayName),
        cardId = value.cardId
    )
}