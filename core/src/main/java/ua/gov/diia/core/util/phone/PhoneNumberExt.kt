package ua.gov.diia.core.util.phone

fun String.removePhoneCodeIfNeed(): String {
    val startIndex = when {
        startsWith("0") -> 1
        startsWith("80") -> 2
        startsWith("380") -> 3
        startsWith("+380") -> 4
        else -> 0
    }
    return if (startIndex > 0) {
        substring(startIndex)
    } else {
        this
    }
}

const val RAW_PHONE_NUMBER_PREFIX = "380"
const val PHONE_NUMBER_VALIDATION_PATTERN =
    "^38(039|050|063|066|067|068|073|091|092|093|094|095|096|097|098|099)\\d{7}\$"