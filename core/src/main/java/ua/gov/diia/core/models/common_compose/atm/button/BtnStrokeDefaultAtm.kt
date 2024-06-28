package ua.gov.diia.core.models.common_compose.atm.button

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize
import ua.gov.diia.core.models.common_compose.general.Action
import ua.gov.diia.core.models.common_compose.general.ButtonStates

@Parcelize
@JsonClass(generateAdapter = true)
data class BtnStrokeDefaultAtm(
    @Json(name = "label")
    val label: String,
    @Json(name = "state")
    val state: ButtonStates?,
    @Json(name = "action")
    val action: Action?,
    @Json(name = "actions")
    val actions: List<Action>?,
    @Json(name = "componentId")
    val componentId: String?
): Parcelable