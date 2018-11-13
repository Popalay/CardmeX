package com.popalay.cardme.core.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.constraintlayout.widget.Group
import androidx.core.view.isVisible
import com.google.android.material.card.MaterialCardView
import com.popalay.cardme.R
import com.popalay.cardme.api.core.model.Card
import com.popalay.cardme.core.extensions.bindView
import com.popalay.cardme.core.extensions.formattedNumber
import com.popalay.cardme.core.extensions.icon
import com.popalay.cardme.core.extensions.showHolder

class CreditCardView(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : MaterialCardView(context, attrs, defStyleAttr) {

    private val textCardNotFound: TextView by bindView(R.id.text_card_not_found)
    private val groupCardFields: Group by bindView(R.id.group_card_fields)
    private val imageUserAvatar: ImageView by bindView(R.id.image_user_avatar)
    private val textDisplayName: TextView by bindView(R.id.text_display_name)
    private val imageCardType: ImageView by bindView(R.id.image_card_type)
    private val textCardNumber: CharacterWrapTextView by bindView(R.id.text_card_number)

    var card: Card? = null
        set(value) {
            if (isInEditMode) return
            value?.run {
                textCardNumber.text = formattedNumber
                imageCardType.setImageResource(cardType.icon)
                textDisplayName.text = holder.name
                imageUserAvatar.showHolder(holder)
            }
            groupCardFields.isVisible = value != null
            textCardNotFound.isVisible = value == null
        }

    constructor(context: Context, attrs: AttributeSet) : this(context, attrs, 0) {
        init()
    }

    private fun init() {
        View.inflate(context, R.layout.layout_credit_card, this)
    }
}