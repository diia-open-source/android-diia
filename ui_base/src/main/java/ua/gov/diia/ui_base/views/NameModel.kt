package ua.gov.diia.ui_base.views

import ua.gov.diia.ui_base.views.common.card_item.DiiaCardInputField

data class NameModel(
    val id: String,
    val name: String,
    val title: String,
    val hint: String,
    val fieldMode: DiiaCardInputField.FieldMode = DiiaCardInputField.FieldMode.EDITABLE,
    val withRemove: Boolean = true,
    val isValid: Boolean = true,
)