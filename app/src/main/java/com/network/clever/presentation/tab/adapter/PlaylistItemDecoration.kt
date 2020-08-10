package com.network.clever.presentation.tab.adapter

import android.content.Context
import android.graphics.Rect
import android.util.TypedValue
import android.view.View
import androidx.recyclerview.widget.RecyclerView


class PlaylistItemDecoration(val context: Context) :
    RecyclerView.ItemDecoration() {
    private var spanCount = 2
    private var spacing = 0
    private var outerMargin = 0

    init {
        spacing = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            12f,
            context.resources.displayMetrics
        ).toInt()

        outerMargin = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            50f,
            context.resources.displayMetrics
        ).toInt()
    }

    fun setSpanCount(spanCount: Int) {
        this.spanCount = spanCount
    }

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val maxCount = parent.adapter?.itemCount ?: 1
        val position = parent.getChildAdapterPosition(view)
        val column = position % spanCount
        val row = position / spanCount
        val lastRow = (maxCount - 1) / spanCount

        outRect.left = column * spacing / spanCount
        outRect.right = spacing - (column + 1) * spacing / spanCount
        outRect.top = spacing * 2

        if (row == lastRow) {
            outRect.bottom = outerMargin
        }
    }
}