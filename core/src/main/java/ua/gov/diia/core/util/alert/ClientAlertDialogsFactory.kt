package ua.gov.diia.core.util.alert

import ua.gov.diia.core.models.dialogs.TemplateDialogModel

interface ClientAlertDialogsFactory {

    fun showCustomAlert(keyAlert: String, isClosable: Boolean = false): TemplateDialogModel

}