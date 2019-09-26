package android.com.xlab

import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup
import androidx.core.view.ViewCompat

class AutoAdjustLayout @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
): ViewGroup(context, attrs, defStyleAttr) {

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val availableWidth = MeasureSpec.getSize(widthMeasureSpec - paddingLeft - paddingRight)

        var maxWidth = 0
        var maxHeight = 0
        var currMaxWidth = 0
        var currMaxHeight = 0
        for (i in 0 until childCount) {
            val childView = getChildAt(i)

            measureChild(childView, widthMeasureSpec - paddingLeft - paddingRight, heightMeasureSpec - paddingTop - paddingBottom)
            val childWidth = childView.measuredWidth
            val childHeight = childView.measuredHeight

            if (currMaxWidth + childWidth > availableWidth) {
                maxWidth = Math.max(maxWidth, currMaxWidth)
                maxHeight += currMaxHeight

                currMaxWidth = 0
                currMaxHeight = 0
            }

            currMaxWidth += childWidth
            currMaxHeight = Math.max(currMaxHeight, childHeight)

            if (i == childCount - 1) {
                maxWidth = Math.max(maxWidth, currMaxWidth)
                maxHeight += currMaxHeight
            }
        }

        setMeasuredDimension(availableWidth + paddingLeft + paddingRight, maxHeight + paddingTop + paddingBottom)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        val maxWidth = this.measuredWidth
        val isRTLEnabled = ViewCompat.getLayoutDirection(this) == ViewCompat.LAYOUT_DIRECTION_RTL

        if (isRTLEnabled) {
            var currMaxHeight = 0
            var top = paddingTop
            var right = maxWidth - paddingRight
            for (i in 0 until childCount) {
                val childView = getChildAt(i)

                val childWidth = childView.measuredWidth
                val childHeight = childView.measuredHeight

                if (right - childWidth < paddingLeft) {
                    right = maxWidth - paddingRight
                    top += currMaxHeight
                }

                childView.layout(right - childWidth, top, right, top + childHeight)

                right -= childWidth
                currMaxHeight = Math.max(currMaxHeight, childHeight)
            }
        } else {
            var currMaxHeight = 0
            var top = paddingTop
            var left = paddingLeft
            for (i in 0 until childCount) {
                val childView = getChildAt(i)

                val childWidth = childView.measuredWidth
                val childHeight = childView.measuredHeight

                if (left + childWidth > maxWidth - paddingRight) {
                    left = paddingLeft
                    top += currMaxHeight
                }

                childView.layout(left, top, left + childWidth, top + childHeight)

                left += childWidth
                currMaxHeight = Math.max(currMaxHeight, childHeight)
            }
        }
    }
}