package ua.gov.diia.core.util.extensions.context

import android.content.Context
import android.content.pm.PackageManager

fun Context.isChromeBrowserExist(): Boolean = try {
    packageManager?.getPackageInfo(
        "com.android.chrome",
        0
    )
        ?.applicationInfo?.enabled
        ?: false
} catch (e: PackageManager.NameNotFoundException) {
    false
}