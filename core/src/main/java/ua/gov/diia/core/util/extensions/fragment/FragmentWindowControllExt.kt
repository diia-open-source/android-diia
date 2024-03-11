package ua.gov.diia.core.util.extensions.fragment

import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import ua.gov.diia.core.util.extensions.context.serviceInput

fun Fragment.setDarkStatusBarIcons() {
    val decor: View = requireActivity().window.decorView
    var flags = decor.systemUiVisibility
    flags = flags or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
    decor.systemUiVisibility = flags
}

fun Fragment.setLightStatusBarIcons() {
    val decor: View = requireActivity().window.decorView
    var flags = decor.systemUiVisibility
    flags = flags and View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv()
    decor.systemUiVisibility = flags
}

fun Fragment.hideKeyboard() {
    val windowToken = view?.windowToken ?: return
    context?.serviceInput?.hideSoftInputFromWindow(windowToken, 0)
}

fun Fragment.showKeyboard(requestView: View?) {
    requestView?.requestFocus()
    context?.serviceInput?.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
}