package ua.gov.diia.doc_driver_license.utils

import ua.gov.diia.documents.ui.DocumentContextMenuAction

enum class DriverLicenseContextMenuActions(override val action: String) :
    DocumentContextMenuAction {
    REPLACE_DRIVER_LICENSE("replaceDriverLicense"),
}