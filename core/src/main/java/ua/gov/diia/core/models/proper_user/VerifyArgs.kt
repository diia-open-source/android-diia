package ua.gov.diia.core.models.proper_user

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class VerifyArgs(
    val otp: String,
    val type: Type
) : Parcelable {
    enum class Type {
        SHARE, CANCEL
    }
}