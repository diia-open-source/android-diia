package ua.gov.diia.core.util.extensions.activity

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import androidx.activity.ComponentActivity

fun Activity.setWindowBrightness(userDefaultBrightness: Boolean = false) {
    var userBrightness: Float = -1F
    window.attributes = window.attributes.apply {
        if (userDefaultBrightness) screenBrightness = userBrightness else {
            userBrightness = screenBrightness
            screenBrightness = 0.8f
        }
    }
}

fun Context.getActivity(): ComponentActivity? = when (this) {
    is ComponentActivity -> this
    is ContextWrapper -> baseContext.getActivity()
    else -> null
}