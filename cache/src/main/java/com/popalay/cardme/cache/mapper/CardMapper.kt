package com.popalay.cardme.cache.mapper

import com.popalay.cardme.api.mapper.Mapper
import com.popalay.cardme.cache.model.CardWithHolder
import com.popalay.cardme.api.model.Card as ApiCard
import com.popalay.cardme.api.model.Holder as ApiHolder

internal class CardMapper(
    private val holderMapper: HolderMapper
) : Mapper<CardWithHolder, ApiCard> {

    override fun apply(value: CardWithHolder): ApiCard = ApiCard(
        id = value.id,
        number = value.number,
        holder = holderMapper(value.holder),
        isPublic = value.isPublic,
        createdDate = value.createdDate,
        updatedDate = value.updatedDate
    )
}