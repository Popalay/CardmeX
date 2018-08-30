package com.popalay.cardme.cardlist.adapter

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.popalay.cardme.addcard.R
import com.popalay.cardme.api.model.CardType
import com.popalay.cardme.cardlist.model.CardListItem
import com.popalay.cardme.core.adapter.BindableViewHolder
import com.popalay.cardme.core.adapter.IdentifiableListAdapter
import com.popalay.cardme.core.extensions.bindView
import com.popalay.cardme.core.extensions.securedNumber

class CardListAdapter : IdentifiableListAdapter<CardListItem>(
    layoutRes = R.layout.item_card,
    createViewHolder = { ViewHolder(it) }
) {

    class ViewHolder(itemView: View) : BindableViewHolder<CardListItem>(itemView) {

        private val imageCardType: ImageView by bindView(R.id.image_card_type)
        private val textDisplayName: TextView by bindView(R.id.text_display_name)
        private val textCardNumber: TextView by bindView(R.id.text_card_number)

        override fun bind(item: CardListItem) {
            val cardTypeRes = when (item.card.cardType) {
                CardType.MASTER_CARD -> R.drawable.ic_mastercard
                CardType.VISA -> R.drawable.ic_visa
                CardType.UNKNOWN -> 0
            }
            imageCardType.setImageResource(cardTypeRes)
            textDisplayName.text = item.card.holder.name
            textCardNumber.text = item.card.securedNumber
        }
    }
}