package ua.gov.diia.ui_base.adapters.binding

import androidx.annotation.ColorRes
import androidx.cardview.widget.CardView
import androidx.databinding.BindingAdapter
import com.google.android.material.card.MaterialCardView
import ua.gov.diia.core.util.extensions.context.getColorCompat

/**
 * Assigns the [MaterialCardView] background color attribute based on the [ColorRes] value.
 *
 * This method implementation excepts [ColorRes] (for example R.color.primary) and then
 * fetches [ColorRes] value based on this resource link and assigns this value to the
 * card background color.
 */
@BindingAdapter("cardBackgroundColorCompat")
fun CardView.setCardBackgroundColorCompat(@ColorRes colorRes: Int?) {
    if (colorRes != null && colorRes != 0){
        setCardBackgroundColor(context.getColorCompat(colorRes))
    }
}