package com.popalay.cardme.usercard.adapter

import android.text.format.DateUtils
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.popalay.cardme.core.adapter.BindableViewHolder
import com.popalay.cardme.core.adapter.IdentifiableListAdapter
import com.popalay.cardme.core.extensions.bindView
import com.popalay.cardme.core.extensions.loadImage
import com.popalay.cardme.core.picasso.CircleImageTransformation
import com.popalay.cardme.usercard.R
import com.popalay.cardme.usercard.model.NotificationListItem

class NotificationListAdapter : IdentifiableListAdapter<NotificationListItem>(
    layoutRes = R.layout.item_notification,
    createViewHolder = { ViewHolder(it) }
) {

    class ViewHolder(itemView: View) : BindableViewHolder<NotificationListItem>(itemView) {

        private val imagePhoto: ImageView by bindView(R.id.image_photo)
        private val textDisplayName: TextView by bindView(R.id.text_display_name)
        private val textDate: TextView by bindView(R.id.text_date)
        private val textMessage: TextView by bindView(R.id.text_message)

        override fun bind(item: NotificationListItem) {
            with(item.notification) {
                imagePhoto.loadImage(fromUser.photoUrl, R.drawable.ic_holder_placeholder, CircleImageTransformation())
                textDisplayName.text = fromUser.displayName.value
                textMessage.text = title
                textDate.text = DateUtils.getRelativeDateTimeString(
                    itemView.context,
                    createdDate.time,
                    DateUtils.DAY_IN_MILLIS,
                    DateUtils.DAY_IN_MILLIS,
                    DateUtils.FORMAT_SHOW_TIME
                )
            }
        }
    }
}