package ua.gov.diia.core.models.common_compose.mlc.checkbox

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize
import ua.gov.diia.core.models.common_compose.atm.text.TextLabelAtm

@Parcelize
@JsonClass(generateAdapter = true)
data class TableItemCheckboxMlc(
    @Json(name = "componentId")
    val componentId: String?,
    @Json(name = "inputCode")
    val inputCode: String?,
    @Json(name = "mandatory")
    val mandatory: Boolean?,
    @Json(name = "rows")
    val items: List<TextLabelAtm>,
    @Json(name = "isSelected")
    val isSelected: Boolean?,
    @Json(name = "isEnabled")
    val isEnabled: Boolean?,
): Parcelable