package ua.gov.diia.core.models.common_compose.atm.button

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize
import ua.gov.diia.core.models.common_compose.general.Action
import ua.gov.diia.core.models.common_compose.general.ButtonStates

@Parcelize
@JsonClass(generateAdapter = true)
data class BtnPrimaryAdditionalAtm(
    @Json(name = "componentId")
    val componentId: String? = null,
    @Json(name = "label")
    val label: String,
    @Json(name = "state")
    val state: ButtonStates?,
    @Json(name = "action")
    val action: Action?,
): Parcelable