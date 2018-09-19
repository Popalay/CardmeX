package com.popalay.cardme.remote.mapper

import com.popalay.cardme.api.core.mapper.Mapper
import com.popalay.cardme.api.core.model.Card
import com.popalay.cardme.api.core.model.CardType
import com.popalay.cardme.remote.model.RemoteCard
import java.util.Date

internal class RemoteCardToCardMapper(
    private val holderMapper: RemoteHolderToHolderMapper
) : Mapper<RemoteCard, Card> {

    override fun apply(value: RemoteCard): Card = Card(
        id = value.id,
        number = value.number,
        holder = holderMapper(value.holder),
        isPublic = value.isPublic,
        cardType = CardType.valueOf(value.cardType),
        userId = value.userId,
        createdDate = value.createdDate?.toDate() ?: Date(),
        updatedDate = value.updatedDate?.toDate() ?: Date()
    )
}