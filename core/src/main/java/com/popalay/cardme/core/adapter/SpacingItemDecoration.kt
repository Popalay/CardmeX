package com.popalay.cardme.core.adapter

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.OrientationHelper
import androidx.recyclerview.widget.RecyclerView

class SpacingItemDecoration(
    private val dividerSize: Int,
    private val betweenItems: Boolean = false,
    private val onSides: Boolean = true,
    private val onTop: Boolean = true,
    private val isNeedApply: (position: Int, parent: RecyclerView) -> Boolean = { _, _ -> true }
) : RecyclerView.ItemDecoration() {

    private var orientation = -1

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        super.getItemOffsets(outRect, view, parent, state)

        val position = parent.getChildLayoutPosition(view)
        if (position == RecyclerView.NO_POSITION) return

        getOrientation(parent)

        if (isNeedApply(position, parent)) {
            if (orientation == OrientationHelper.HORIZONTAL) {
                applyToHorizontalList(outRect)
            } else {
                applyToVerticalList(outRect)
            }
        }
    }

    private fun applyToVerticalList(outRect: Rect) {
        if (betweenItems) {
            outRect.top = if (onTop) dividerSize / 2 else 0
            outRect.bottom = dividerSize / 2
        }
        if (onSides) {
            outRect.left = dividerSize
            outRect.right = dividerSize
        }
    }

    private fun applyToHorizontalList(outRect: Rect) {
        if (betweenItems) {
            outRect.left = dividerSize / 2
            outRect.right = dividerSize / 2
        }
        if (onSides) {
            outRect.top = if (onTop) dividerSize else 0
            outRect.bottom = dividerSize
        }
    }

    private fun getOrientation(parent: RecyclerView) {
        if (orientation != -1) return
        if (parent.layoutManager is LinearLayoutManager) {
            val layoutManager = parent.layoutManager as LinearLayoutManager
            orientation = layoutManager.orientation
        } else {
            throw IllegalStateException("SpacingItemDecoration can only be used with a LinearLayoutManager.")
        }
    }
}