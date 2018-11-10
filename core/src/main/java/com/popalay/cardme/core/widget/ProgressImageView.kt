package com.popalay.cardme.core.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import androidx.core.content.ContextCompat
import com.popalay.cardme.R
import com.popalay.cardme.core.extensions.px

class ProgressImageView : ImageView {

    private var progressBar: CircularProgressDrawable? = null

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init()
    }

    var isProgress: Boolean = false
        set(isProgress) {
            field = isProgress
            progressBar?.run {
                setVisible(!isProgress, true)
                if (isProgress) {
                    drawable.alpha = 0
                    start()
                } else {
                    drawable.alpha = 255
                    stop()
                }
            }
        }

    override fun onVisibilityChanged(changedView: View, visibility: Int) {
        super.onVisibilityChanged(changedView, visibility)
        if (visibility == View.VISIBLE && isProgress) progressBar?.start() else progressBar?.stop()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        updateBounds()
    }

    override fun draw(canvas: Canvas) {
        super.draw(canvas)
        if (isProgress) progressBar?.draw(canvas)
    }

    override fun verifyDrawable(who: Drawable): Boolean = who === progressBar || super.verifyDrawable(who)

    private fun init() {
        progressBar = CircularProgressDrawable(
            ContextCompat.getColor(context, R.color.secondary),
            2.px.toFloat()
        ).apply {
            callback = this@ProgressImageView
            stop()
            setVisible(false, true)
        }
    }

    private fun updateBounds() {
        val progressHeight = 24.px
        val top = (height - progressHeight) / 2
        val bottom = (height + progressHeight) / 2
        val left = (width - progressHeight) / 2
        val right = (width + progressHeight) / 2
        progressBar?.setBounds(left, top, right, bottom)
    }
}