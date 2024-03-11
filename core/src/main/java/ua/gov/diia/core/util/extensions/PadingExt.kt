package ua.gov.diia.core.util.extensions

import android.app.Activity
import android.app.PendingIntent
import android.content.Context
import android.os.Build
import android.view.WindowManager
import dagger.hilt.android.internal.managers.FragmentComponentManager

fun addFlagKeepScreen(context: Context) {
    (FragmentComponentManager.findActivity(context) as Activity).
    window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
}

fun clearFlagKeepScreen(context: Context) {
    (FragmentComponentManager.findActivity(context) as Activity).
    window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
}


fun getPendingFlags(): Int {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        PendingIntent.FLAG_MUTABLE
    } else {
        0
    }
}

