package ua.gov.diia.publicservice.models

import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Parcelable
import androidx.core.content.ContextCompat
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize
import ua.gov.diia.publicservice.R

@Parcelize
@JsonClass(generateAdapter = true)
data class PublicServiceCategory(
    @Json(name = "code")
    val code: String,
    @Json(name = "sortOrder")
    val sortOrder: Int,
    @Json(name = "icon")
    val icon: String,
    @Json(name = "name")
    val name: String,
    @Json(name = "status")
    val status: CategoryStatus,
    @Json(name = "visibleSearch")
    val visibleSearch: Boolean,
    @Json(name = "publicServices")
    val publicServices: List<PublicService>,
    @Json(name = "tabCode")
    val tabCode: String?
) : Parcelable {

    val isSingleServiceCategory: Boolean
        get() = publicServices.size == 1

    val singleService: PublicService?
        get() = publicServices.firstOrNull()

    val hasServices: Boolean
        get() = publicServices.isNotEmpty()

    val nameForGrid: String
        get() = if (name == "COVID-сертифікати") {
            "COVID-\nсертифікати"
        } else {
            name
        }

    @Transient
    @IgnoredOnParcel
    private val iconResourceOverlay: Int = when (code) {
        "office-workspace" -> R.drawable.ic_google
        else -> 0
    }

    fun hasOverlayIcon(): Boolean = iconResourceOverlay != 0

    fun getOverlayIcon(context: Context): Drawable? = if (iconResourceOverlay != 0) {
        ContextCompat.getDrawable(context, iconResourceOverlay)
    } else {
        null
    }
}
