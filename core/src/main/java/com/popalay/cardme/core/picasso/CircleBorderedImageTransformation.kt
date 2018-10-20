package com.popalay.cardme.core.picasso

import android.graphics.*
import androidx.annotation.ColorInt
import com.squareup.picasso.Transformation

class CircleBorderedImageTransformation(
    private val strokeWidth: Float,
    @ColorInt private val strokeColor: Int
) : Transformation {

    companion object {

        private const val KEY = "CircleBorderedImageTransformation"
    }

    override fun transform(source: Bitmap): Bitmap {
        val minEdge = Math.min(source.width, source.height).toFloat()
        val dx = (source.width - minEdge) / 2
        val dy = (source.height - minEdge) / 2

        val shader = BitmapShader(source, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
        val matrix = Matrix()
        matrix.setTranslate((-dx), (-dy))
        shader.setLocalMatrix(matrix)

        val paint = Paint(Paint.ANTI_ALIAS_FLAG)
        paint.shader = shader

        val output = Bitmap.createBitmap(minEdge.toInt(), minEdge.toInt(), source.config)
        val canvas = Canvas(output)
        canvas.drawOval(RectF(0f, 0f, minEdge, minEdge), paint)

        val paint1 = Paint()
        paint1.color = strokeColor
        paint1.style = Paint.Style.STROKE
        paint1.isAntiAlias = true
        paint1.strokeWidth = strokeWidth
        canvas.drawCircle(minEdge / 2, minEdge / 2, (minEdge - strokeWidth) / 2, paint1)

        source.recycle()

        return output
    }

    override fun key() = KEY
}