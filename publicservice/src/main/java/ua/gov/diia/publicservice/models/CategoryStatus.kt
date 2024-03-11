package ua.gov.diia.publicservice.models

import androidx.annotation.ColorRes
import ua.gov.diia.publicservice.R

@Suppress("EnumEntryName")
enum class CategoryStatus {
    active, inactive;

    val enabled: Boolean
        get() = when (this) {
            active -> true
            inactive -> false
        }

    val textColor: Int
        @ColorRes
        get() = when (this) {
            active -> R.color.black
            inactive -> R.color.colorPrimaryDark
        }

    val showServiceDisabledTitle: Boolean
        get() = when (this) {
            active -> false
            inactive -> true
        }

}