package ua.gov.diia.doc_driver_license.utils

import ua.gov.diia.core.models.document.DocumentContextMenuAction

enum class DriverLicenseContextMenuActions(override val action: String) :
    DocumentContextMenuAction {
    REPLACE_DRIVER_LICENSE("replaceDriverLicense"),
}