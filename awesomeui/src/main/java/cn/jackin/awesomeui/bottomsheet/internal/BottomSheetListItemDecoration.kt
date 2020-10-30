package cn.jackin.awesomeui.bottomsheet.internal

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import cn.jackin.awesomeui.R

class BottomSheetListItemDecoration(val context: Context) : ItemDecoration() {
    private val mSeparatorPaint: Paint = Paint()

    override fun onDrawOver(
        c: Canvas,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.onDrawOver(c, parent, state)
        val adapter = parent.adapter
        val layoutManager = parent.layoutManager
        if (adapter == null || layoutManager == null) {
            return
        }
        for (i in 0 until parent.childCount) {
            val view = parent.getChildAt(i)
            val position = parent.getChildAdapterPosition(view)
                if (position > 0 &&
                    adapter.getItemViewType(position - 1) != BottomSheetListAdapter.ITEM_TYPE_NORMAL
                ) {
                    val top = layoutManager.getDecoratedTop(view)
                    c.drawLine(
                        0f,
                        top.toFloat(),
                        parent.width.toFloat(),
                        top.toFloat(),
                        mSeparatorPaint
                    )
                }
                if (position + 1 < adapter.itemCount &&
                    adapter.getItemViewType(position + 1) == BottomSheetListAdapter.ITEM_TYPE_NORMAL
                ) {
                    val bottom = layoutManager.getDecoratedBottom(view)
                    c.drawLine(
                        0f,
                        bottom.toFloat(),
                        parent.width.toFloat(),
                        bottom.toFloat(),
                        mSeparatorPaint
                    )
                }
        }
    }

    init {
        mSeparatorPaint.strokeWidth = 1f
        mSeparatorPaint.style = Paint.Style.STROKE
        @Suppress("DEPRECATION")
        mSeparatorPaint.color = context.resources.getColor(R.color.awesomeui_color_separator)
    }
}