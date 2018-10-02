package com.popalay.cardme.cardlist.adapter

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.popalay.cardme.addcard.R
import com.popalay.cardme.cardlist.model.CardListItem
import com.popalay.cardme.core.adapter.BindableViewHolder
import com.popalay.cardme.core.adapter.IdentifiableListAdapter
import com.popalay.cardme.core.extensions.bindView
import com.popalay.cardme.core.extensions.icon
import com.popalay.cardme.core.extensions.loadImage
import com.popalay.cardme.core.extensions.securedNumber
import com.popalay.cardme.core.picasso.CircleImageTransformation

class CardListAdapter : IdentifiableListAdapter<CardListItem>(
    layoutRes = R.layout.item_card,
    createViewHolder = { ViewHolder(it) }
) {

    class ViewHolder(itemView: View) : BindableViewHolder<CardListItem>(itemView) {

        private val imagePhoto: ImageView by bindView(R.id.image_photo)
        private val textDisplayName: TextView by bindView(R.id.text_display_name)
        private val textCardNumber: TextView by bindView(R.id.text_card_number)

        override fun bind(item: CardListItem) {
            with(item.card) {
                imagePhoto.loadImage(holder.photoUrl, R.drawable.ic_holder_placeholder, CircleImageTransformation())
                textDisplayName.text = holder.name
                textCardNumber.text = securedNumber
                textCardNumber.setCompoundDrawablesWithIntrinsicBounds(cardType.icon, 0, 0, 0)
            }
        }
    }
}