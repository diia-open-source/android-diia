package ua.gov.diia.ui_base.adapters.binding

import android.view.View
import android.widget.TextView
import androidx.core.view.ViewCompat
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat
import androidx.core.widget.doAfterTextChanged
import androidx.databinding.BindingAdapter

@BindingAdapter("onClickDescription")
fun View.setOnClickDescription(description: CharSequence?) {
    ViewCompat.replaceAccessibilityAction(
        this,
        AccessibilityNodeInfoCompat.AccessibilityActionCompat.ACTION_CLICK,
        description,
        null,
    )
}

@BindingAdapter("accessibilityPrefix")
fun TextView.setAccessibilityPrefix(prefix: String) {
    contentDescription = "$prefix $text"
    doAfterTextChanged {
        contentDescription = "$prefix $text"
    }
}