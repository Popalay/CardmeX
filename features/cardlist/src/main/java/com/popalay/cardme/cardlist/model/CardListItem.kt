package com.popalay.cardme.cardlist.model

import com.popalay.cardme.api.model.Card
import com.popalay.cardme.core.adapter.Identifiable

class CardListItem(
    val card: Card
) : Identifiable {
    override val id get() = card.id
}