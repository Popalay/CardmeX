package com.popalay.cardme.core.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.View
import com.google.android.material.button.MaterialButton
import com.popalay.cardme.core.extensions.px

class ProgressMaterialButton : MaterialButton {

    private var savedText: String = ""
    private var progressBar: CircularProgressDrawable? = null

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init()
    }

    var isProgress: Boolean = false
        set(value) {
            field = value
            isClickable = !value
            progressBar?.run {
                setVisible(!value, true)
                if (value) {
                    start()
                    savedText = text.toString()
                    text = ""
                } else {
                    text = if (text.isNotBlank()) text else savedText
                    savedText = ""
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
        progressBar = CircularProgressDrawable(Color.WHITE, 2.px.toFloat()).apply {
            callback = this@ProgressMaterialButton
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