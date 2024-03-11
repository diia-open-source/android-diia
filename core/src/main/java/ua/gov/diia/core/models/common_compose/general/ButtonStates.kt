package ua.gov.diia.core.models.common_compose.general

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
@Suppress("EnumEntryName")
enum class ButtonStates : Parcelable {
    enabled, disabled, invisible;
}