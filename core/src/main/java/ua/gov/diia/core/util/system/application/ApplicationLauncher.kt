package ua.gov.diia.core.util.system.application

import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.net.Uri
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

interface ApplicationLauncher {

    fun launch(uri: String)
}

class ApplicationLauncherImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : ApplicationLauncher {

    override fun launch(uri: String) {
        val launcher = Intent().apply {
            action = Intent.ACTION_VIEW
            data = Uri.parse(uri)
            flags = FLAG_ACTIVITY_NEW_TASK
        }
        context.startActivity(launcher)
    }
}