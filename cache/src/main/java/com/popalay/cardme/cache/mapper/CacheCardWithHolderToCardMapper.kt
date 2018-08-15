package com.popalay.cardme.cache.mapper

import com.popalay.cardme.api.mapper.Mapper
import com.popalay.cardme.api.model.Card
import com.popalay.cardme.api.model.CardType
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
        createdDate = value.createdDate,
        updatedDate = value.updatedDate
    )
}