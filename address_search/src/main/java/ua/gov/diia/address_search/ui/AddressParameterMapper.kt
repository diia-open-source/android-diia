package ua.gov.diia.address_search.ui

import ua.gov.diia.address_search.models.AddressFieldApproveRequest
import ua.gov.diia.address_search.models.AddressFieldMode
import ua.gov.diia.address_search.models.AddressFieldRequestValue
import ua.gov.diia.address_search.models.AddressItem
import ua.gov.diia.address_search.models.AddressParameter
import javax.inject.Inject

class AddressParameterMapper @Inject constructor() {

    fun toFieldApproveRequest(list: List<Any?>): AddressFieldApproveRequest {
        list.apply {
            val params: AddressParameter? = find { value -> value is AddressParameter }
                    as? AddressParameter

            //Gets data as per the button mode requested from the server
            //-If the field mode has been requested as BUTTON it means that user will select item
            //from the list and we should get an AddressItem.
            //-If the field mode has been requested as EDITABLE it means that user will input
            //free String text through two-way data binding
            val data = params?.getFieldMode()?.let { mode ->
                when (mode) {
                    AddressFieldMode.BUTTON -> find { value -> value is AddressItem }
                            as? AddressItem

                    AddressFieldMode.EDITABLE -> find { value -> value is String }
                            as? String
                }
            }

            val mandatory = params?.mandatory ?: false

            val validation = params?.validation?.regexp

            val flags = params?.validation?.flags


            return AddressFieldApproveRequest(mandatory, data, validation, flags)
        }
    }

    fun getViewMode(param: AddressParameter?): Int {
        return param?.getFieldMode()?.getViewMode() ?: return 0
    }

    private fun AddressFieldMode.getViewMode() = when (this) {
        AddressFieldMode.BUTTON -> 0
        AddressFieldMode.EDITABLE -> 1
    }


    fun getEditableModeFieldRequest(
        value: String?,
        params: AddressParameter?
    ): AddressFieldRequestValue? {
        return if (params != null && value != null) {
            if (params.isEditableMode()) {
                AddressFieldRequestValue(id = null, params.type, value)
            } else {
                null
            }
        } else {
            return null
        }
    }


    fun approveFiledData(value: Any?): Boolean = when (value) {
        is AddressFieldApproveRequest -> value.approved()
        //approve for nulls (values which won't be selected withing flow)
        else -> true
    }
}