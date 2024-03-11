package ua.gov.diia.ps_criminal_cert.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class PreviousNames(
    val previousFirstNameList: List<String>? = null,
    val previousMiddleNameList: List<String>? = null,
    val previousLastNameList: List<String>? = null
) : Parcelable