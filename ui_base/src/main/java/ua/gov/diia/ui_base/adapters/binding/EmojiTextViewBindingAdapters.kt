package ua.gov.diia.ui_base.adapters.binding

import android.widget.TextView
import androidx.annotation.StringRes
import androidx.databinding.BindingAdapter
import ua.gov.diia.core.util.extensions.context.getStringSafe

@BindingAdapter("emojiTextRes")
fun TextView.setEmojiRes(@StringRes res: Int?) {
    text = context.getStringSafe(res)
}