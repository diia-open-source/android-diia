package ua.gov.diia.verification.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class BaseVerificationData(
    val schema: String,
    val method: String,
    val requestId: String
) : Parcelable
