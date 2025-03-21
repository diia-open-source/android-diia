package ua.gov.diia.core.models.common_compose.mlc.list


import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize
import ua.gov.diia.core.models.common_compose.atm.chip.ChipStatusAtm
import ua.gov.diia.core.models.common_compose.atm.text.AmountAtm
import ua.gov.diia.core.models.common_compose.general.Action
import ua.gov.diia.core.models.document.docgroups.v2.IconLeft
import ua.gov.diia.core.models.document.docgroups.v2.IconRight

@Parcelize
@JsonClass(generateAdapter = true)
data class ListItemMlc(
    @Json(name = "componentId")
    val componentId: String? = null,
    @Json(name = "action")
    val action: Action?,
    @Json(name = "description")
    val description: String?,
    @Json(name = "iconLeft")
    val iconLeft: IconLeft?,
    @Json(name = "bigIconLeft")
    val bigIconLeft: BigIconLeft?,
    @Json(name = "leftLogoLink")
    val leftLogoLink: String?,
    @Json(name = "iconRight")
    val iconRight: IconRight?,
    @Json(name = "id")
    val id: String?,
    @Json(name = "label")
    val label: String? = null,
    @Json(name = "logoLeft")
    val logoLeft: String?,
    @Json(name = "state")
    val state: String?,
    @Json(name = "chipStatusAtm")
    val chipStatusAtm: ChipStatusAtm?,
    @Json(name = "amountAtm")
    val amountAtm: AmountAtm?,
    @Json(name = "dataJson")
    val dataJson: String?,
) :Parcelable {

    @Parcelize
    @JsonClass(generateAdapter = true)
    data class BigIconLeft(
        @Json(name = "code")
        val code: String?
    ) : Parcelable

}
