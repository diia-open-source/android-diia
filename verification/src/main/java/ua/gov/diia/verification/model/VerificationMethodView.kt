package ua.gov.diia.verification.model

import android.os.Parcelable
import androidx.annotation.DrawableRes
import kotlinx.parcelize.Parcelize

@Parcelize
data class VerificationMethodView(
    val code: String,
    @DrawableRes val iconRes: Int
): Parcelable