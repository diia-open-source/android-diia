package ua.gov.diia.ui_base.components.infrastructure.utils.resource

import android.content.Context
import android.util.TypedValue

fun Context.dpToPx(dip: Float): Int {
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        dip,
        resources.displayMetrics
    ).toInt()
}

fun Context.spToPx(sp: Float): Int {
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_SP,
        sp,
        resources.displayMetrics
    ).toInt()
}