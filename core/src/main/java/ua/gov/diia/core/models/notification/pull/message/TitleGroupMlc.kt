package ua.gov.diia.core.models.notification.pull.message


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class TitleGroupMlc(
    @Json(name = "heroText")
    val heroText: String?,
    @Json(name = "mediumIconRight")
    val mediumIconRight: MediumIconRight?,
    @Json(name = "label")
    val label: String?,
    @Json(name = "leftNavIcon")
    val leftNavIcon: LeftNavIcon?
)