package ua.gov.diia.core.models

import android.content.Context
import android.os.Parcelable
import androidx.annotation.ColorRes
import ua.gov.diia.core.R

interface ContextMenuField : Parcelable {

    fun getActionType() : String

    fun getSubType(): String?

    fun getDisplayName(c: Context): String

    @ColorRes
    fun getTintColor(): Int = R.color.colorPrimary
}