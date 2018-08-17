package com.popalay.cardme.cache.mapper

import com.popalay.cardme.api.mapper.Mapper
import com.popalay.cardme.api.model.Card
import com.popalay.cardme.cache.model.CacheCard

internal class CardToCacheCardMapper : Mapper<Card, CacheCard> {

    override fun apply(value: Card): CacheCard = CacheCard(
        id = value.id,
        number = value.number,
        holderId = value.holder.id,
        isPublic = value.isPublic,
        cardType = value.cardType.name,
        userId = value.userId,
        createdDate = value.createdDate,
        updatedDate = value.updatedDate
    )
}