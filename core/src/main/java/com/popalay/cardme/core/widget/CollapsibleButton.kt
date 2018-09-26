package com.popalay.cardme.core.widget

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import androidx.core.animation.doOnEnd
import androidx.core.animation.doOnStart
import com.google.android.material.button.MaterialButton
import com.popalay.cardme.core.extensions.px

class CollapsibleButton : MaterialButton {

    private var iconDrawable: Drawable? = null

    private val expandedHeight = 48.px
    private val collapsedHeight = 56.px

    private var expandedWidth = 0

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init()
    }

    var isCollapsed: Boolean = false
        set(value) {
            field = value
            if (expandedWidth == 0) expandedWidth = width
            animateMorph().apply {
                doOnStart {
                    if (isCollapsed) {
                        setTextColor(Color.TRANSPARENT)
                        iconDrawable?.alpha = 255
                        icon = null
                    }
                }
                doOnEnd {
                    if (!isCollapsed) {
                        setTextColor(Color.WHITE)
                        iconDrawable?.constantState?.newDrawable()?.mutate()?.let { drawable -> icon = drawable.apply { alpha = 255 } }
                        iconDrawable?.alpha = 0
                    }
                }
                duration = 100
            }.start()
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

    //override fun getBehavior(): CoordinatorLayout.Behavior<*> = ScrollAwareBehavior()

    private fun init() {
        maxLines = 1
        iconSize = 24.px
        iconDrawable = icon?.constantState?.newDrawable()?.mutate()?.apply {
            callback = this@CollapsibleButton
            setTint(Color.WHITE)
            alpha = 0
        }
    }

    private fun updateBounds() {
        val top = (height - iconSize) / 2
        val bottom = top + iconSize
        val left = if (isCollapsed) (width - iconSize) / 2 else paddingStart
        val right = left + iconSize
        iconDrawable?.setBounds(left, top, right, bottom)
    }

    private fun animateMorph(): Animator {
        val endHeight = if (isCollapsed) collapsedHeight else expandedHeight
        val endWidth = if (isCollapsed) (endHeight * 0.8).toInt() else expandedWidth
        val heightAnimator = ValueAnimator.ofInt(height, endHeight).apply {
            addUpdateListener { animation ->
                layoutParams = layoutParams.apply {
                    height = animation.animatedValue as Int
                }
            }
        }
        val widthAnimator = ValueAnimator.ofInt(width, endWidth).apply {
            addUpdateListener { animation ->
                layoutParams = layoutParams.apply {
                    width = animation.animatedValue as Int
                }
            }
        }

        return AnimatorSet().apply {
            playTogether(heightAnimator, widthAnimator)
            setTarget(this@CollapsibleButton)
        }
    }
}