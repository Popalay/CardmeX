package com.popalay.cardme.cache.mapper

import com.popalay.cardme.api.core.mapper.Mapper
import com.popalay.cardme.api.core.model.Card
import com.popalay.cardme.api.core.model.CardType
import com.popalay.cardme.cache.model.CacheCardWithHolder

internal class CacheCardWithHolderToCardMapper(
    private val holderMapper: CacheHolderToHolderMapper
) : Mapper<CacheCardWithHolder, Card> {

    override fun apply(value: CacheCardWithHolder): Card = Card(
        id = value.id,
        number = value.number,
        holder = holderMapper(value.holder),
        isPublic = value.isPublic,
        cardType = CardType.valueOf(value.cardType),
        userId = value.userId,
        createdDate = value.createdDate,
        updatedDate = value.updatedDate
    )
}