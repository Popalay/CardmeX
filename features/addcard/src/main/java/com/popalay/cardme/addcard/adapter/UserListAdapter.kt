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
import com.popalay.cardme.core.extensions.loadImage
import com.popalay.cardme.core.picasso.CircleImageTransformation
import com.popalay.cardme.core.widget.ProgressImageButton

class UserListAdapter : IdentifiableListAdapter<UserListItem>(
    layoutRes = R.layout.item_user,
    diffCallback = UserItemDiffCallback(),
    createViewHolder = { ViewHolder(it) }
) {

    companion object {

        private const val KEY_IS_PROGRESS = "KEY_IS_PROGRESS"
    }

    class ViewHolder(itemView: View) : BindableViewHolder<UserListItem>(itemView) {

        private val imagePhoto: ImageView by bindView(R.id.image_photo)
        private val textDisplayName: TextView by bindView(R.id.text_display_name)
        private val buttonRequest: ProgressImageButton by bindView(R.id.button_request)

        override fun bind(item: UserListItem) {
            with(item.user) {
                imagePhoto.loadImage(photoUrl, R.drawable.ic_holder_placeholder, CircleImageTransformation())
                textDisplayName.text = displayName.value
            }
            buttonRequest.isProgress = item.isProgress
        }

        override fun bindPayloads(item: UserListItem, payloads: List<Any>) {
            (payloads[0] as? Bundle)?.keySet()?.forEach {
                if (it == KEY_IS_PROGRESS) {
                    buttonRequest.isProgress = item.isProgress
                }
            }
        }
    }

    class UserItemDiffCallback : IdentifiableDiffCallback<UserListItem>() {

        override fun getChangePayload(oldItem: UserListItem, newItem: UserListItem): Any? {
            val diffBundle = Bundle()
            if (newItem.isProgress != oldItem.isProgress) {
                diffBundle.putBoolean(KEY_IS_PROGRESS, newItem.isProgress)
            }
            return if (diffBundle.size() == 0) null else diffBundle
        }
    }
}