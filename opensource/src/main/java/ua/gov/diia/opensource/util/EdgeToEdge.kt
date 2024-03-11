package ua.gov.diia.opensource.util

import android.content.res.Resources
import android.os.Build
import android.view.View
import android.view.Window
import androidx.annotation.RequiresApi
import androidx.core.app.ComponentActivity
import androidx.core.view.WindowCompat
import ua.gov.diia.core.util.extensions.context.getColorCompat

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