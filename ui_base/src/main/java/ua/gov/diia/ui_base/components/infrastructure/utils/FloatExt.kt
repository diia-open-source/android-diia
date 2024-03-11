package ua.gov.diia.ui_base.components.infrastructure.utils

import android.content.res.Resources

fun Float.dp(): Float = this * density + 0.5f

val density: Float
    get() = Resources.getSystem().displayMetrics.density