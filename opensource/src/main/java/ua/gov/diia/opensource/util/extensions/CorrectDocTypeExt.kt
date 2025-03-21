package ua.gov.diia.opensource.util.extensions

import ua.gov.diia.doc_driver_license.models.DocName.DRIVER_LICENSE

fun getCorrectDocTypeList(orig: List<String>): List<String> {
    return orig.map {
        mapCorrectDocNameType(it)
    }
}

fun mapCorrectDocNameType(name: String) = when (name) {
    "driverLicense" -> DRIVER_LICENSE
    else -> ""
}