package com.popalay.cardme.remote.mapper

import com.google.firebase.Timestamp
import com.popalay.cardme.api.mapper.Mapper
import com.popalay.cardme.api.model.Card
import com.popalay.cardme.remote.model.RemoteCard

internal class CardToRemoteCardMapper(
    private val holderToRemoteHolderMapper: HolderToRemoteHolderMapper
) : Mapper<Card, RemoteCard> {

    override fun apply(value: Card): RemoteCard = RemoteCard(
        id = value.id,
        number = value.number,
        holder = holderToRemoteHolderMapper(value.holder),
        isPublic = value.isPublic,
        cardType = value.cardType.name,
        userId = value.userId,
        createdDate = Timestamp(value.createdDate),
        updatedDate = Timestamp(value.updatedDate)
    )
}