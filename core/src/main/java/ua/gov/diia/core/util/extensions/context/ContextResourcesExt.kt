package ua.gov.diia.core.util.extensions.context

import android.content.Context
import android.content.res.Resources
import android.graphics.drawable.Drawable
import android.util.TypedValue
import androidx.annotation.ColorRes
import androidx.annotation.DimenRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import kotlin.math.roundToInt

fun Context.getMarginFromDimenId(@DimenRes dimension: Int) =
    resources.getDimension(dimension).roundToInt()

fun Context.dpToPx(dip: Float): Int {
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        dip,
        this.resources.displayMetrics
    ).toInt()
}

fun Context.getDrawableSafe(@DrawableRes drawableId: Int?): Drawable? = try {
    ContextCompat.getDrawable(this, drawableId!!)
} catch (e: Exception) {
    null
}

/**
 * Wrapper for [ResourcesCompat] to prevent building long method signature every time.
 * Also this call compatible with all android versions.
 */
fun Context.getColorCompat(@ColorRes id: Int, colorTheme : Resources.Theme = theme): Int {
    return ResourcesCompat.getColor(resources, id, colorTheme)
}

fun Context.getColorCompatSafe(@ColorRes id: Int?): Int? = try {
    ResourcesCompat.getColor(resources, id!!, theme)
} catch (e: Exception) {
    null
}

fun Context.getStringSafe(@StringRes res: Int?): String = try {
    getString(res!!)
} catch (e: Exception) {
    ""
}

fun Context.getDimensionPixelSizeSafe(@DimenRes res: Int?): Int = try {
    resources.getDimensionPixelSize(res!!)
} catch (e: Exception) {
    0
}
