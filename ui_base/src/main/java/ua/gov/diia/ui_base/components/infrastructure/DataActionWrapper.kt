package ua.gov.diia.ui_base.components.infrastructure

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class DataActionWrapper(
    val type: String,
    val subtype: String? = null,
    val subresource: String? = null,
    val resource: String? = null,
    val condition: String? = null
): Parcelable