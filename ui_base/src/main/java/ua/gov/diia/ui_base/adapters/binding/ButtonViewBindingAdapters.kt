package ua.gov.diia.ui_base.adapters.binding

import android.widget.Button
import androidx.annotation.StringRes
import androidx.databinding.BindingAdapter
import ua.gov.diia.core.util.extensions.validateResource

@BindingAdapter("buttonTextRes")
fun Button.setTextRes(@StringRes textRes: Int?) {
    textRes.validateResource { res -> text = context.getString(res) }
}
