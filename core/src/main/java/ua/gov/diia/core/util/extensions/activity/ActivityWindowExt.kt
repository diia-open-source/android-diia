package ua.gov.diia.core.util.extensions.activity

import android.app.Activity

fun Activity.setWindowBrightness(userDefaultBrightness: Boolean = false) {
    var userBrightness: Float = -1F
    window.attributes = window.attributes.apply {
        if (userDefaultBrightness) screenBrightness = userBrightness else {
            userBrightness = screenBrightness
            screenBrightness = 0.8f
        }
    }
}