package com.popalay.cardme.cache.mapper

import com.popalay.cardme.api.core.mapper.Mapper
import com.popalay.cardme.api.core.model.User
import com.popalay.cardme.cache.model.CacheUser

internal class UserToCacheUserMapper : Mapper<User, CacheUser> {

    override fun apply(value: User): CacheUser = CacheUser(
        uuid = value.uuid,
        email = value.email,
        photoUrl = value.photoUrl,
        phoneNumber = value.phoneNumber,
        displayName = value.displayName.value,
        cardId = value.cardId
    )
}