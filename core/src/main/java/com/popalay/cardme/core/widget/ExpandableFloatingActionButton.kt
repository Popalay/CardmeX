package com.popalay.cardme.core.widget

import android.content.Context
import android.content.res.TypedArray
import android.graphics.PorterDuff
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.ContextCompat
import androidx.core.content.res.getResourceIdOrThrow
import androidx.core.content.res.getStringOrThrow
import androidx.core.view.ViewCompat
import androidx.transition.ChangeBounds
import androidx.transition.Transition
import androidx.transition.TransitionListenerAdapter
import androidx.transition.TransitionManager
import com.popalay.cardme.core.extensions.bindView
import com.popalay.cardme.core.R

class ExpandableFloatingActionButton @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyle: Int = 0
) : ConstraintLayout(context, attributeSet, defStyle), CoordinatorLayout.AttachedBehavior {

    private val image: ImageView by bindView(R.id.image)
    private val cardTextView: TextView by bindView(R.id.card_textView)
    private val constraintLayout: ConstraintLayout by bindView(R.id.constraint_Layout)

    private val animationDuration: Long = 150
    private val constraintSet1 = ConstraintSet()
    private val constraintSet2 = ConstraintSet()

    private var isAnimationRunning = false

    var isExpanded: Boolean = true
        set(expanded) {
            if (isAnimationRunning) return
            val constraint: ConstraintSet = if (expanded) constraintSet1 else constraintSet2
            TransitionManager.beginDelayedTransition(
                this,
                ChangeBounds().setDuration(animationDuration)
                    .addListener(object : TransitionListenerAdapter() {

                        override fun onTransitionStart(transition: Transition) {
                            isAnimationRunning = true
                        }

                        override fun onTransitionEnd(transition: Transition) {
                            isAnimationRunning = false
                        }
                    })
            )
            constraint.applyTo(constraintLayout)
            field = expanded
        }

    var fabIcon: Int = 0
        set(resId) {
            image.setImageResource(resId)
        }

    var fabText: String = ""
        set(text) {
            cardTextView.text = text
        }

    var fabTextColor: Int = 0
        set(@ColorInt color) {
            cardTextView.setTextColor(color)
        }

    var fabBackgroundColor: Int = 0
        set(@ColorInt color) {
            constraintLayout.background.mutate().setColorFilter(color, PorterDuff.Mode.SRC_ATOP)
        }

    init {
        inflate(context, R.layout.layout_expanded_fab, this).apply {
            constraintSet1.clone(constraintLayout)
            constraintSet2.clone(context, R.layout.layout_expanded_fab_alt)
            isClickable = true
            isFocusable = true
        }
        attributeSet?.let {
            getAttributeSet(
                context = context,
                attrs = it
            )
        }
    }

    private fun getAttributeSet(context: Context, attrs: AttributeSet) {
        val typedArray: TypedArray = context.obtainStyledAttributes(attrs, R.styleable.ExpandableFloatingActionButton)
        fabIcon = typedArray.getResourceIdOrThrow(R.styleable.ExpandableFloatingActionButton_fabIcon)
        fabText = typedArray.getStringOrThrow(R.styleable.ExpandableFloatingActionButton_fabText)
        isExpanded = typedArray.getBoolean(R.styleable.ExpandableFloatingActionButton_isExpanded, true)
        fabTextColor = typedArray.getColor(
            R.styleable.ExpandableFloatingActionButton_fabTextColor,
            ContextCompat.getColor(context, android.R.color.white)
        )
        fabBackgroundColor = typedArray.getColor(
            R.styleable.ExpandableFloatingActionButton_fabBackgroundColor,
            ContextCompat.getColor(context, R.color.colorAccent)
        )
        typedArray.recycle()
    }

    override fun getBehavior(): CoordinatorLayout.Behavior<*> = ExpandableFloatingActionButton.Behavior()

    class Behavior(context: Context? = null, attrs: AttributeSet? = null) : CoordinatorLayout.Behavior<View>(context, attrs) {

        override fun onNestedScroll(
            coordinatorLayout: CoordinatorLayout,
            child: View,
            target: View,
            dxConsumed: Int,
            dyConsumed: Int,
            dxUnconsumed: Int,
            dyUnconsumed: Int,
            type: Int
        ) {
            super.onNestedScroll(coordinatorLayout, child, target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, type)
            if (dyConsumed > 0) {
                (child as ExpandableFloatingActionButton).isExpanded = false
            } else if (dyConsumed < 0) {
                (child as ExpandableFloatingActionButton).isExpanded = true
            }
        }

        override fun onStartNestedScroll(
            coordinatorLayout: CoordinatorLayout,
            child: View,
            directTargetChild: View,
            target: View,
            axes: Int,
            type: Int
        ): Boolean = axes == ViewCompat.SCROLL_AXIS_VERTICAL ||
                super.onStartNestedScroll(coordinatorLayout, child, directTargetChild, target, axes, type)
    }
}