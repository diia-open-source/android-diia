package ua.gov.diia.opensource.util.extensions.activity

import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Build
import android.util.DisplayMetrics
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.annotation.RequiresApi
import androidx.core.app.ComponentActivity
import androidx.core.view.WindowCompat
import ua.gov.diia.core.util.extensions.context.getColorCompat

fun ComponentActivity.adjustFontScale(configuration: Configuration) {
    if (Build.VERSION.SDK_INT < 27) {
        configuration.fontScale = 1.0f
        val metrics: DisplayMetrics = resources.displayMetrics
        val wm = getSystemService(Context.WINDOW_SERVICE) as WindowManager
        wm.defaultDisplay.getMetrics(metrics)
        metrics.scaledDensity = configuration.fontScale * metrics.density
        baseContext.resources.updateConfiguration(configuration, metrics)
    }
}

fun ComponentActivity.overrideConfiguration(context: Context?) {
    if (Build.VERSION.SDK_INT >= 27) {
        val newOverride = Configuration(context?.resources?.configuration)
        newOverride.fontScale = 1.0f
        applyOverrideConfiguration(newOverride)
    }
}

fun ComponentActivity.setUpEdgeToEdge() {
    val impl = if (Build.VERSION.SDK_INT >= 26) {
        EdgeToEdgeApi26()
    } else {
        EdgeToEdgeApi23()
    }
    impl.setUp(window, findViewById(android.R.id.content), theme)
}

private interface EdgeToEdgeImpl {
    fun setUp(window: Window, view: View, theme: Resources.Theme)
}

@RequiresApi(26)
private class EdgeToEdgeApi26 : EdgeToEdgeImpl {

    override fun setUp(window: Window, view: View, theme: Resources.Theme) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        window.statusBarColor = view.context.getColorCompat(android.R.color.transparent, theme)
        window.navigationBarColor = view.context.getColorCompat(android.R.color.transparent, theme)
    }
}

private class EdgeToEdgeApi23 : EdgeToEdgeImpl {

    override fun setUp(window: Window, view: View, theme: Resources.Theme) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        window.statusBarColor = view.context.getColorCompat(android.R.color.transparent, theme)
    }
}