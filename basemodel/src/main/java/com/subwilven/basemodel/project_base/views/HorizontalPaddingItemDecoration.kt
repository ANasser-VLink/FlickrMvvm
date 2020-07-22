package com.subwilven.basemodel.project_base.views

import android.content.Context
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
// used when try to ass padding fro the horzintal recycler view  which clip to padding not work
class HorizontalPaddingItemDecoration(
    private val paddingStart: Int = 0,
    private val paddingEnd: Int = 0
) : RecyclerView.ItemDecoration() {

    private var isRtl: Boolean? = null

    private fun isRTL(ctx: Context): Boolean {
        val config = ctx.resources.configuration
        return config.layoutDirection == View.LAYOUT_DIRECTION_RTL
    }

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        super.getItemOffsets(outRect, view, parent, state)

        if (isRtl == null) {
            isRtl = isRTL(view.context)
        }

        val position = parent.getChildAdapterPosition(view)

        if (position == 0) {
            if (!isRtl!!) {
                outRect.left += paddingStart
            } else {
                outRect.right += paddingStart
            }
        }
        if (position == parent.adapter?.itemCount?.minus(1)) {
            if (!isRtl!!) {
                outRect.right += paddingEnd
            } else {
                outRect.left += paddingEnd
            }
        }
    }
}