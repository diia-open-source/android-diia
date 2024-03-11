package ua.gov.diia.core.util.system.application

import android.content.Context
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

data class ApplicationLauncherInfo(
    val packageName: String,
    val applicationName: String,
    val appIcon: Drawable
)

interface InstalledApplicationInfoProvider {

    fun applicationExists(packageName: String): Boolean

    fun getApplicationDetails(packageName: String): ApplicationLauncherInfo?
}

class InstalledApplicationInfoProviderImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : InstalledApplicationInfoProvider {

    private val packageManager: PackageManager = context.packageManager

    override fun applicationExists(packageName: String): Boolean =
        try {
            packageManager.getPackageInfo(packageName, 0)
            true
        } catch (e: PackageManager.NameNotFoundException) {
            false
        }

    override fun getApplicationDetails(packageName: String): ApplicationLauncherInfo? =
        if (applicationExists(packageName)) {
            val ai = packageManager.getApplicationInfo(packageName, 0)
            ApplicationLauncherInfo(
                packageName = packageName,
                applicationName = packageManager.getApplicationLabel(ai).toString(),
                appIcon = ai.loadIcon(packageManager)
            )
        } else {
            null
        }
}