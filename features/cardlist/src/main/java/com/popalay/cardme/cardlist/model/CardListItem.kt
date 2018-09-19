package com.popalay.cardme.cardlist.model

import com.popalay.cardme.api.core.model.Card
import com.popalay.cardme.core.adapter.Identifiable

data class CardListItem(
    val card: Card
) : Identifiable {
    override val id get() = card.id.hashCode().toLong()
}