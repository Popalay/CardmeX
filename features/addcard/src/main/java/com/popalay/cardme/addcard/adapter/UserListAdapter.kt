package com.popalay.cardme.addcard.adapter

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.popalay.cardme.addcard.R
import com.popalay.cardme.addcard.model.UserListItem
import com.popalay.cardme.core.adapter.BindableViewHolder
import com.popalay.cardme.core.adapter.IdentifiableDiffCallback
import com.popalay.cardme.core.adapter.IdentifiableListAdapter
import com.popalay.cardme.core.extensions.bindView
import com.popalay.cardme.core.extensions.icon
import com.popalay.cardme.core.extensions.loadImage
import com.popalay.cardme.core.extensions.securedNumber
import com.popalay.cardme.core.picasso.CircleImageTransformation

class UserListAdapter : IdentifiableListAdapter<UserListItem>(
    layoutRes = R.layout.item_user,
    diffCallback = UserItemDiffCallback(),
    createViewHolder = { ViewHolder(it) }
) {

    companion object {

        private const val KEY_IS_SELECTED = "KEY_IS_SELECTED"
    }

    class ViewHolder(itemView: View) : BindableViewHolder<UserListItem>(itemView) {

        private val imagePhoto: ImageView by bindView(R.id.image_photo)
        private val textDisplayName: TextView by bindView(R.id.text_display_name)
        private val textCardNumber: TextView by bindView(R.id.text_card_number)

        override fun bind(item: UserListItem) {
            with(item.user) {
                imagePhoto.loadImage(photoUrl, R.drawable.ic_holder_placeholder, CircleImageTransformation())
                textDisplayName.text = displayName.value
                textCardNumber.text = card?.securedNumber
                textCardNumber.setCompoundDrawablesWithIntrinsicBounds(card?.cardType?.icon ?: 0, 0, 0, 0)
            }
            itemView.isSelected = item.isSelected
        }

        override fun bindPayloads(item: UserListItem, payloads: List<Any>) {
            (payloads[0] as? Bundle)?.keySet()?.forEach {
                if (it == KEY_IS_SELECTED) {
                    itemView.isSelected = item.isSelected
                }
            }
        }
    }

    class UserItemDiffCallback : IdentifiableDiffCallback<UserListItem>() {

        override fun getChangePayload(oldItem: UserListItem, newItem: UserListItem): Any? {
            val diffBundle = Bundle()
            if (newItem.isSelected != oldItem.isSelected) {
                diffBundle.putBoolean(KEY_IS_SELECTED, newItem.isSelected)
            }
            return if (diffBundle.size() == 0) null else diffBundle
        }
    }
}