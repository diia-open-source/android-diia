package ua.gov.diia.ui_base.components.infrastructure.utils.resource

import androidx.annotation.DrawableRes

sealed class UiIcon {
    data class DynamicIconBase64(val value: String) : UiIcon()
    data class DrawableResource(val code: String) : UiIcon()
    data class PlainString(val value: String) : UiIcon()
    data class DrawableResInt(@DrawableRes val res: Int) : UiIcon()
    data class URLIcon(val link: String) : UiIcon()
}

fun String?.toDrawableResourceOrNull(): UiIcon? {
    if (this == null) return null
    return UiIcon.DrawableResource(this)
}
fun String.toDrawableResource(): UiIcon {
    return UiIcon.DrawableResource(this)
}

