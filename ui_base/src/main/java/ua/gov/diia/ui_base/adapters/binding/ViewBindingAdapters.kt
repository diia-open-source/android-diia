package ua.gov.diia.ui_base.adapters.binding

import android.view.View
import androidx.annotation.ColorRes
import androidx.databinding.BindingAdapter
import ua.gov.diia.core.util.extensions.context.getColorCompatSafe

@BindingAdapter("backgroundCompat")
fun View.setBackgroundCompat(@ColorRes colorRes: Int?) {
    context.getColorCompatSafe(colorRes)?.run(this::setBackgroundColor)
}