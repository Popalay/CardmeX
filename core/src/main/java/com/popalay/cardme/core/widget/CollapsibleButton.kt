package com.popalay.cardme.core.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.ViewGroup
import androidx.transition.ChangeBounds
import androidx.transition.TransitionManager
import com.google.android.material.button.MaterialButton
import com.popalay.cardme.core.extensions.px

class CollapsibleButton : MaterialButton {

    private var savedText: String = ""
    private var iconDrawable: Drawable? = null

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init()
    }

    var isCollapsed: Boolean = false
        set(value) {
            field = value
            if (value) {
                if (text.isNotBlank()) {
                    layoutParams = layoutParams.apply {
                        width = (this@CollapsibleButton.height * 0.75).toInt()
                    }
                    TransitionManager.beginDelayedTransition(parent as ViewGroup, ChangeBounds())
                    savedText = text.toString()
                    text = ""
                }
            } else {
                layoutParams = layoutParams.apply {
                    width = ViewGroup.LayoutParams.WRAP_CONTENT
                }
                text = if (text.isNotBlank()) text else savedText
                savedText = ""
            }
            iconDrawable?.alpha = value.takeIf { it }?.let { 255 } ?: 0
        }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        updateBounds()
    }

    override fun draw(canvas: Canvas) {
        super.draw(canvas)
        iconDrawable?.draw(canvas)
    }

    override fun verifyDrawable(who: Drawable): Boolean =
        who === iconDrawable || super.verifyDrawable(who)

    private fun init() {
        iconDrawable = icon?.apply {
            callback = this@CollapsibleButton
            alpha = isCollapsed.takeIf { it }?.let { 255 } ?: 0
        }
        icon = null
    }

    private fun updateBounds() {
        val iconSize = 24.px
        val top = (height - iconSize) / 2
        val bottom = top + iconSize
        val left = (width - iconSize) / 2
        val right = left + iconSize
        iconDrawable?.setBounds(left, top, right, bottom)
    }
}