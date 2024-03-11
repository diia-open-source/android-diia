package ua.gov.diia.core.util.decorators

import android.content.Context
import android.graphics.Canvas
import androidx.annotation.DrawableRes
import androidx.recyclerview.widget.RecyclerView
import ua.gov.diia.core.util.extensions.context.getDrawableSafe

class ListDelimiterDecorator(
    context: Context,
    @DrawableRes dividerRes: Int,
    private val ignorePadding: Boolean = true,
    private val includeTopAge: Boolean = true,
    private val includeBottomAge: Boolean = true
) : RecyclerView.ItemDecoration() {

    private val dividerBottom = context.getDrawableSafe(dividerRes)
    private val dividerTop = context.getDrawableSafe(dividerRes)

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {

        val dividerBottomHeight = dividerBottom?.intrinsicHeight ?: 0
        val dividerTopHeight = dividerTop?.intrinsicHeight ?: 0

        for (i in 0 until parent.childCount) {
            val child = parent.getChildAt(i)

            val left = if (ignorePadding) child.left else child.left + child.paddingStart
            val right = if (ignorePadding) child.right else child.right - child.paddingEnd

            val isFirstItem = i == 0
            val isLastItem = i == parent.childCount - 1

            if (isFirstItem) {
                if (includeTopAge) {
                    val tTop = child.top
                    val tBottom = tTop + dividerTopHeight

                    dividerTop?.setBounds(left, tTop, right, tBottom)
                    dividerTop?.draw(c)
                }
            }

            val bTop = child.bottom - dividerBottomHeight
            val bBottom = child.bottom

            if (!isLastItem) {
                dividerBottom?.setBounds(left, bTop, right, bBottom)
                dividerBottom?.draw(c)
            } else {
                if (includeBottomAge) {
                    dividerBottom?.setBounds(left, bTop, right, bBottom)
                    dividerBottom?.draw(c)
                }
            }
        }
    }
}