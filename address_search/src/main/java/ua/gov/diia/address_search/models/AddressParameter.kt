package ua.gov.diia.address_search.models


import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

@Parcelize
@JsonClass(generateAdapter = true)
data class AddressParameter(
    @Json(name = "type")
    val type: String?,
    @Json(name = "label")
    val label: String?,
    @Json(name = "hint")
    val hint: String?,
    @Json(name = "input")
    val input: AddressFieldInputType?,
    @Json(name = "mandatory")
    val mandatory: Boolean?,
    @Json(name = "validation")
    val validation: AddressValidation?,
    @Json(name = "source")
    val source: AddressSource?,
    @Json(name = "defaultListItem")
    val defaultListItem: AddressDefaultListItem?,
    @Json(name = "defaultText")
    val defaultTextItem: String?
) : Parcelable {


    fun getSearchType(): SearchType = when (input) {
        AddressFieldInputType.singleCheck -> SearchType.BULLET
        else -> SearchType.LIST
    }

    fun getFieldMode() : AddressFieldMode = when(input){
        AddressFieldInputType.textField -> AddressFieldMode.EDITABLE
        else -> AddressFieldMode.BUTTON
    }

    fun isEditableMode() : Boolean = input == AddressFieldInputType.textField

    fun getItems(): Array<AddressItem> = source?.items?.toTypedArray() ?: arrayOf()

    fun hasContent(): Boolean = source?.items?.size ?: 0 > 0

    fun hasDefault(): Boolean = defaultListItem != null || defaultTextItem != null

    fun isFieldVisible() : Boolean = hasContent() || hasDefault()

    fun getDefaultAddress(): AddressItem? =
        defaultListItem?.toAddressItem()
            ?: if (defaultTextItem != null) {
                AddressItem(id = null, name = defaultTextItem, errorMessage = null, disabled = false)
            } else {
                null
            }

}

enum class AddressFieldMode{
    BUTTON, EDITABLE
}