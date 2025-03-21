package ua.gov.diia.core.util.phone

const val RAW_PHONE_NUMBER_PREFIX = "380"

private const val COMMON_PATTERN =
    "38(039|050|063|066|067|068|073|075|077|091|092|093|094|095|096|097|098|099)\\d{7}$"
const val PHONE_NUMBER_VALIDATION_PATTERN = "^$COMMON_PATTERN"
const val PHONE_NUMBER_VALIDATION_PATTERN_WITH_ALLOWED_PLUS = "^\\+?$COMMON_PATTERN"

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