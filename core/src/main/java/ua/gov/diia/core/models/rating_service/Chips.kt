package ua.gov.diia.core.models.rating_service

import android.os.Parcelable
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize
import ua.gov.diia.core.R

@Parcelize
@JsonClass(generateAdapter = true)
data class Chips(
    @Json(name = "code")
    val code: String?,
    @Json(name = "name")
    val name: String?,
    val isSelected: Boolean = false
) : Parcelable {

    val chipsNameColor: Int
        @ColorRes
        get() = if (isSelected)  R.color.white else R.color.black

    val chipsBackground: Int
        @DrawableRes
        get() = if (isSelected)  R.drawable.chips_selected else R.drawable.chips_unselected
}