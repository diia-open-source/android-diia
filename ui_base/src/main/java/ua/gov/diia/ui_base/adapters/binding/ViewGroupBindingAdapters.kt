package ua.gov.diia.ui_base.adapters.binding

import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.annotation.DimenRes
import androidx.databinding.BindingAdapter
import kotlin.math.roundToInt

@BindingAdapter("layout_MarginTop")
fun ViewGroup.setTopMargin(@DimenRes dimenRes: Int?) {
    if (dimenRes != null) {
        val currentLayoutParams = layoutParams as LinearLayout.LayoutParams
        (layoutParams as LinearLayout.LayoutParams).setMargins(
            currentLayoutParams.leftMargin,
            resources.getDimension(dimenRes).roundToInt(),
            currentLayoutParams.rightMargin,
            currentLayoutParams.bottomMargin
        )
    }
}