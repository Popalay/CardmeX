package com.popalay.cardme.core.widget

import android.content.Context
import android.graphics.*
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.OvalShape
import android.util.TypedValue
import com.popalay.cardme.R

class TextDrawable(
    private val context: Context,
    private val text: String?
) : ShapeDrawable(OvalShape()) {

    private val textPaint: Paint = Paint()

    init {

        val typedValue = TypedValue()
        val a = context.obtainStyledAttributes(typedValue.data, intArrayOf(
            R.attr.colorPrimaryVariant,
            R.attr.colorOnPrimary
        ))
        val colorPrimaryVarinat = a.getColor(0, 0)
        val colorOnPrimary = a.getColor(1, 0)

        a.recycle()

        textPaint.apply {
            color = colorOnPrimary
            isAntiAlias = true
            style = Paint.Style.FILL
            textAlign = Paint.Align.CENTER
            textSize = context.resources.getDimensionPixelSize(R.dimen.abc_text_size_title_material).toFloat()
        }

        paint.apply {
            color = colorPrimaryVarinat
        }
    }

    override fun draw(canvas: Canvas) {
        super.draw(canvas)
        val r = bounds

        val count = canvas.save()
        canvas.translate(r.left.toFloat(), r.top.toFloat())

        // draw text
        val width = r.width()
        val height = r.height()
        canvas.drawText(text ?: "", (width / 2).toFloat(), height / 2 - (textPaint.descent() + textPaint.ascent()) / 2, textPaint)

        canvas.restoreToCount(count)
    }

    override fun setAlpha(alpha: Int) {
        textPaint.alpha = alpha
    }

    override fun setColorFilter(cf: ColorFilter?) {
        textPaint.colorFilter = cf
    }

    override fun getOpacity(): Int = PixelFormat.TRANSLUCENT

    override fun getIntrinsicWidth(): Int = -1

    override fun getIntrinsicHeight(): Int = -1
}