package ua.gov.diia.address_search.models

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize
import ua.gov.diia.core.models.common.message.AttentionMessageMlc
import ua.gov.diia.core.models.dialogs.TemplateDialogModel

@Parcelize
@JsonClass(generateAdapter = true)
data class AddressFieldResponse(
    @Json(name = "title")
    val title: String?,
    @Json(name = "description")
    val description: String?,
    @Json(name = "attentionMessage")
    val attentionMessage: AttentionMessageMlc?,
    @Json(name = "parameters")
    val parameters: List<AddressParameter>?,
    @Json(name = "address")
    val address: AddressIdentifier?,
    @Json(name = "processCode")
    val processCode: Int?,
    @Json(name = "template")
    val template: TemplateDialogModel?
) : Parcelable{

    fun isEndForAddressSelection() : Boolean = address != null
}