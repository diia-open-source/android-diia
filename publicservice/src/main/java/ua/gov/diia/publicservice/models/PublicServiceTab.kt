package ua.gov.diia.publicservice.models

import androidx.annotation.DrawableRes
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import ua.gov.diia.publicservice.R

@JsonClass(generateAdapter = true)
data class PublicServiceTab(
    @Json(name = "code")
    val code: String,
    @Json(name = "name")
    val name: String,
    @Json(ignore = true)
    val isChecked: Boolean = false
) {

    @get:DrawableRes
    val background: Int
        get() = if (isChecked) {
            R.drawable.chip_light_selected
        } else {
            R.drawable.chip_light
        }
}
