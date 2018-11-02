package com.popalay.cardme.notifications.adapter

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.popalay.cardme.core.adapter.BindableViewHolder
import com.popalay.cardme.core.adapter.IdentifiableListAdapter
import com.popalay.cardme.core.extensions.bindView
import com.popalay.cardme.core.extensions.loadImage
import com.popalay.cardme.core.picasso.CircleImageTransformation
import com.popalay.cardme.notifications.R
import com.popalay.cardme.notifications.model.NotificationListItem

class NotificationListAdapter : IdentifiableListAdapter<NotificationListItem>(
    layoutRes = R.layout.item_notification,
    createViewHolder = { ViewHolder(it) }
) {

    class ViewHolder(itemView: View) : BindableViewHolder<NotificationListItem>(itemView) {

        private val imagePhoto: ImageView by bindView(R.id.image_photo)
        private val textMessage: TextView by bindView(R.id.text_message)

        override fun bind(item: NotificationListItem) {
            with(item.notification) {
                imagePhoto.loadImage(fromUser.photoUrl, R.drawable.ic_holder_placeholder, CircleImageTransformation())
                textMessage.text = title
            }
        }
    }
}