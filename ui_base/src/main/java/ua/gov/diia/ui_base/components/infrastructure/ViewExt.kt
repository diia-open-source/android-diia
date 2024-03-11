package ua.gov.diia.ui_base.components.infrastructure

import android.view.View
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding

fun View.handleKeyboardInsets(){
    ViewCompat.setOnApplyWindowInsetsListener(this) { view, insets ->
        val imeInset = insets.getInsets(WindowInsetsCompat.Type.ime()).bottom
        val systemBarInset = insets.getInsets(WindowInsetsCompat.Type.systemBars()).bottom
        if (imeInset == 0) {
            view.updatePadding(bottom = imeInset)
        } else {
            view.updatePadding(bottom = imeInset - systemBarInset)
        }
        insets
    }
}