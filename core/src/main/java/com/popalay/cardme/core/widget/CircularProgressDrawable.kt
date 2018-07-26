package com.popalay.cardme.core.widget

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.graphics.Canvas
import android.graphics.ColorFilter
import android.graphics.Paint
import android.graphics.PixelFormat
import android.graphics.Rect
import android.graphics.RectF
import android.graphics.drawable.Animatable
import android.graphics.drawable.Drawable
import android.util.Property
import android.view.animation.DecelerateInterpolator
import android.view.animation.LinearInterpolator
import androidx.core.animation.doOnRepeat
import kotlin.properties.Delegates

class CircularProgressDrawable(
    colorInt: Int,
    private var mBorderWidth: Float
) : Drawable(), Animatable {

    companion object {

        private val ANGLE_INTERPOLATOR = LinearInterpolator()
        private val SWEEP_INTERPOLATOR = DecelerateInterpolator()
        private const val ANGLE_ANIMATOR_DURATION = 2000
        private const val SWEEP_ANIMATOR_DURATION = 600
        private const val MIN_SWEEP_ANGLE = 30
    }

    private val fBounds = RectF()
    private var objectAnimatorSweep: ObjectAnimator by Delegates.notNull()
    private var objectAnimatorAngle: ObjectAnimator by Delegates.notNull()
    private var paint: Paint by Delegates.notNull()
    private var modeAppearing: Boolean = false
    private var isRunning: Boolean = false
    private var currentGlobalAngleOffset: Float = 0F

    private var currentGlobalAngle: Float = 0F
        set(currentGlobalAngle) {
            field = currentGlobalAngle
            invalidateSelf()
        }

    private var currentSweepAngle: Float = 0F
        set(currentSweepAngle) {
            field = currentSweepAngle
            invalidateSelf()
        }

    private val angleProperty = object : Property<CircularProgressDrawable, Float>(Float::class.java, "angle") {
        override fun get(field: CircularProgressDrawable): Float = field.currentGlobalAngle

        override fun set(field: CircularProgressDrawable, value: Float) {
            field.currentGlobalAngle = value
        }
    }

    private val sweepProperty = object : Property<CircularProgressDrawable, Float>(Float::class.java, "arc") {
        override fun get(field: CircularProgressDrawable): Float = field.currentSweepAngle

        override fun set(field: CircularProgressDrawable, value: Float) {
            field.currentSweepAngle = value
        }
    }

    init {
        paint = Paint().apply {
            isAntiAlias = true
            style = Paint.Style.STROKE
            strokeWidth = mBorderWidth
            color = colorInt
        }
        setupAnimations()
    }

    override fun draw(canvas: Canvas) {
        var startAngle = currentGlobalAngle - currentGlobalAngleOffset
        var sweepAngle = currentSweepAngle
        if (!modeAppearing) {
            startAngle += sweepAngle
            sweepAngle = 360f - sweepAngle - MIN_SWEEP_ANGLE.toFloat()
        } else {
            sweepAngle += MIN_SWEEP_ANGLE.toFloat()
        }
        canvas.drawArc(fBounds, startAngle, sweepAngle, false, paint)
    }

    override fun setAlpha(alpha: Int) {
        paint.alpha = alpha
    }

    override fun setColorFilter(cf: ColorFilter?) {
        paint.colorFilter = cf
    }

    override fun getOpacity(): Int = PixelFormat.TRANSPARENT

    override fun onBoundsChange(bounds: Rect) {
        super.onBoundsChange(bounds)
        fBounds.apply {
            left = bounds.left.toFloat() + mBorderWidth / 2f + .5f
            right = bounds.right.toFloat() - mBorderWidth / 2f - .5f
            top = bounds.top.toFloat() + mBorderWidth / 2f + .5f
            bottom = bounds.bottom.toFloat() - mBorderWidth / 2f - .5f
        }
    }

    override fun start() {
        if (isRunning) return
        isRunning = true
        objectAnimatorAngle.start()
        objectAnimatorSweep.start()
        invalidateSelf()
    }

    override fun stop() {
        if (!isRunning) return
        isRunning = false
        objectAnimatorAngle.cancel()
        objectAnimatorSweep.cancel()
        invalidateSelf()
    }

    override fun isRunning(): Boolean = isRunning

    private fun toggleAppearingMode() {
        modeAppearing = !modeAppearing
        if (modeAppearing) currentGlobalAngleOffset = (currentGlobalAngleOffset + MIN_SWEEP_ANGLE * 2) % 360
    }

    private fun setupAnimations() {
        objectAnimatorAngle = ObjectAnimator.ofFloat(this, angleProperty, 360f).apply {
            interpolator = ANGLE_INTERPOLATOR
            duration = ANGLE_ANIMATOR_DURATION.toLong()
            repeatMode = ValueAnimator.RESTART
            repeatCount = ValueAnimator.INFINITE
        }

        objectAnimatorSweep = ObjectAnimator.ofFloat(this, sweepProperty, 360f - MIN_SWEEP_ANGLE * 2).apply {
            interpolator = SWEEP_INTERPOLATOR
            duration = SWEEP_ANIMATOR_DURATION.toLong()
            repeatMode = ValueAnimator.RESTART
            repeatCount = ValueAnimator.INFINITE
            doOnRepeat { toggleAppearingMode() }
        }
    }
}