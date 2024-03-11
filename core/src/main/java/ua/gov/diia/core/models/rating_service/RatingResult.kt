package ua.gov.diia.core.models.rating_service

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class RatingResult(
    val item: RatingRequest?,
    val key: String
): Parcelable
