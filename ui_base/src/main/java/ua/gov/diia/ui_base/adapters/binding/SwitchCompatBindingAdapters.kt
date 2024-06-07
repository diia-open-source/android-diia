package ua.gov.diia.ui_base.adapters.binding

import androidx.appcompat.widget.SwitchCompat
import androidx.databinding.BindingAdapter

@BindingAdapter("buttonStyleListener")
fun SwitchCompat.setButtonStyleListener(buttonListener: () -> Unit){
    setOnClickListener {
        buttonListener.invoke()
        isChecked = false
    }
}