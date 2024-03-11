package ua.gov.diia.ps_criminal_cert.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Birth(
    val country: String,
    val city: String
) : Parcelable