package com.popalay.cardme.core.behavior

import android.content.Context
import android.support.design.widget.CoordinatorLayout
import android.support.v4.view.ViewCompat
import android.util.AttributeSet
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator

class ScrollAwareBehavior(context: Context, attrs: AttributeSet) : CoordinatorLayout.Behavior<View>(context, attrs) {

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
            child.animate()
                .setDuration(300L)
                .setInterpolator(AccelerateDecelerateInterpolator())
                .alpha(0F)
                .translationY(child.top.toFloat())
                .start()
        } else if (dyConsumed < 0) {
            child.animate()
                .setDuration(300L)
                .setInterpolator(AccelerateDecelerateInterpolator())
                .alpha(255F)
                .translationY(0F)
                .start()
        }
    }

    override fun onStartNestedScroll(
        coordinatorLayout: CoordinatorLayout,
        child: View,
        directTargetChild: View,
        target: View,
        axes: Int,
        type: Int
    ): Boolean = axes == ViewCompat.SCROLL_AXIS_VERTICAL
        || super.onStartNestedScroll(coordinatorLayout, child, directTargetChild, target, axes, type)
}