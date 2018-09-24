package com.popalay.cardme.core.behavior

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.ViewCompat
import com.popalay.cardme.core.widget.CollapsibleButton

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
            //HIDE
            (child as CollapsibleButton).isCollapsed = true
/*            child.animate()
                .setDuration(300L)
                .setInterpolator(AccelerateDecelerateInterpolator())
                .alpha(0F)
                .translationY(child.top.toFloat())
                .start()*/
        } else if (dyConsumed < 0) {
            //SHOW
            (child as CollapsibleButton).isCollapsed = false
/*            child.animate()
                .setDuration(300L)
                .setInterpolator(AccelerateDecelerateInterpolator())
                .alpha(255F)
                .translationY(0F)
                .start()*/
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