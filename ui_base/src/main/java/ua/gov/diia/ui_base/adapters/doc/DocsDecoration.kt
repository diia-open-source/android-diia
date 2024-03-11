package ua.gov.diia.ui_base.adapters.doc

import android.content.Context
import android.graphics.Rect
import android.view.View
import androidx.annotation.DimenRes
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.RecyclerView

class HorizontalMarginItemDecoration(context: Context, @DimenRes horizontalMarginInDp: Int, @DimenRes shadowDimens: Int) :
    RecyclerView.ItemDecoration() {

    private val horizontalMarginInPx: Int =
        context.resources.getDimension(horizontalMarginInDp).toInt()
    private val shadowDimens: Int =
        context.resources.getDimension(shadowDimens).toInt()

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        ViewCompat.setElevation(view, shadowDimens.toFloat())
        ViewCompat.setTranslationZ(view, shadowDimens.toFloat())
        ViewCompat.offsetLeftAndRight(view, horizontalMarginInPx)
    }

}
