package ua.gov.diia.ui_base.util.view

import android.content.ClipData
import android.graphics.Typeface
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.StringRes
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.ContextCompat
import androidx.core.view.children
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import ua.gov.diia.ui_base.R
import ua.gov.diia.core.util.extensions.context.dpToPx
import ua.gov.diia.core.util.extensions.context.getColorCompat
import ua.gov.diia.core.util.extensions.context.serviceClipboard

fun View.showCopyDeviceUuidClipedSnackBar(uuid: String, topPadding: Float = 24f, bottomPadding: Float = 16f) {
    val clip = ClipData.newPlainText("androidId", uuid)

    context.serviceClipboard?.setPrimaryClip(clip)

    showTopSnackBar(
        R.string.num_copied,
        Snackbar.LENGTH_LONG,
        topPadding,
        bottomPadding,
    )
}

fun View.showTopSnackBar(@StringRes res: Int, length: Int, topPadding: Float) {
    with(Snackbar.make(this, res, length)) {
        animationMode = BaseTransientBottomBar.ANIMATION_MODE_FADE
        textAlignment = TextView.TEXT_ALIGNMENT_CENTER
        view.setBackgroundColor(ContextCompat.getColor(context, R.color.green_snack))
        view.setPadding(0, context.dpToPx(topPadding), 0, 0)
        view.updateGravity(Gravity.TOP)
        (view.findViewById(com.google.android.material.R.id.snackbar_text) as? TextView)?.let { textV ->
            textV.setTypeface(textV.typeface, Typeface.BOLD)
            textV.setPadding(0, 0, 0, 0)
            textV.textSize = 12f
            textV.gravity = Gravity.CENTER
            textV.isAllCaps = false
        }
        setTextColor(context.getColorCompat(R.color.black))
        setText(res)
        show()
    }
}

fun View.showTopSnackBar(@StringRes res: Int, length: Int, topPadding: Boolean = false) {
    showTopSnackBar(res, length, if (topPadding) 24f else 0f)
}
fun View.showTopSnackBar(@StringRes res: Int, length: Int, topPadding: Float, bottomPadding: Float) {
    with(Snackbar.make(this, res, length)) {
        animationMode = BaseTransientBottomBar.ANIMATION_MODE_FADE
        textAlignment = TextView.TEXT_ALIGNMENT_CENTER
        view.setBackgroundColor(ContextCompat.getColor(context, R.color.green_snack))
        view.setPadding(0, context.dpToPx(topPadding), 0, context.dpToPx(bottomPadding))
        view.updateGravity(Gravity.TOP)
        (view.findViewById(com.google.android.material.R.id.snackbar_text) as? TextView)?.let { textV ->
            textV.setTypeface(textV.typeface, Typeface.BOLD)
            textV.setPadding(0, 0, 0, 0)
            textV.textSize = 12f
            textV.gravity = Gravity.CENTER
            textV.isAllCaps = false
        }
        setTextColor(context.getColorCompat(R.color.black))
        setText(res)
        show()
    }
}

fun View.updateGravity(gravity: Int) {
    when(val params = layoutParams) {
        is FrameLayout.LayoutParams -> {
            params.gravity = gravity
            layoutParams = params
        }
        is CoordinatorLayout.LayoutParams -> {
            params.gravity = gravity
            layoutParams = params
        }
        is LinearLayout.LayoutParams -> {
            params.gravity = gravity
            layoutParams = params
        }
    }
}

fun View.setEnabledRecursive(isEnabled: Boolean) {
    setEnabled(isEnabled)
    (this as? ViewGroup)?.children?.forEach { child ->
        child.setEnabledRecursive(isEnabled)
    }
}