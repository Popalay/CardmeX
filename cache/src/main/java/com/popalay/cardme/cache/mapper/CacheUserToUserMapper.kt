package com.popalay.cardme.cache.mapper

import com.popalay.cardme.api.mapper.Mapper
import com.popalay.cardme.api.model.User
import com.popalay.cardme.cache.model.CacheCardWithHolder
import com.popalay.cardme.cache.model.CacheHolder
import com.popalay.cardme.cache.model.CacheUser

internal class CacheUserToUserMapper(
    private val cacheCardWithHolderToCardMapper: CacheCardWithHolderToCardMapper
) : Mapper<CacheUser, User> {

    override fun apply(value: CacheUser): User = User(
        uuid = value.uuid,
        email = value.email,
        photoUrl = value.photoUrl,
        phoneNumber = value.phoneNumber,
        displayName = value.displayName,
        card = value.card?.let {
            cacheCardWithHolderToCardMapper(
                CacheCardWithHolder(
                    id = it.id,
                    number = it.number,
                    holder = CacheHolder(value.uuid, value.displayName),
                    isPublic = it.isPublic,
                    cardType = it.cardType,
                    userId = it.userId,
                    createdDate = it.createdDate,
                    updatedDate = it.updatedDate
                )
            )
        }
    )
}