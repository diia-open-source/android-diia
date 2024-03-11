package ua.gov.diia.core.util.extensions.context

import android.app.ActivityManager
import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import ua.gov.diia.core.util.CommonConst

fun Context.vibrate(millis: Long = 500L) {
    val v = getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator ?: return
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        v.vibrate(
            VibrationEffect.createOneShot(
                millis,
                VibrationEffect.DEFAULT_AMPLITUDE
            )
        )
    } else {
        @Suppress("DEPRECATION")
        v.vibrate(millis)
    }
}


fun Context.isDiiaAppRunning(): Boolean {
    val activityManager =
        getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
    val processDetails = activityManager.runningAppProcesses
    if (processDetails != null) {
        for (info in processDetails) {
            if (info.processName == CommonConst.DIIA_HOST) {
                return info.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND || info.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_VISIBLE
            }
        }
    }
    return false
}