package ua.gov.diia.core.util.extensions

inline fun Int?.validateResource(invalid: () -> Unit = {}, valid: (res: Int) -> Unit) {
    if (this != null && this != -1 && this != 0 && this != 0x0) {
        valid.invoke(this)
    } else {
        invalid.invoke()
    }
}

fun Int?.isResourceValid(): Boolean = this != null && this != -1 && this != 0 && this != 0x0

inline fun String?.validateString(invalid: () -> Unit = {}, valid: (string: String) -> Unit) {
    val validationString = this?.trim()
    if (validationString.isNullOrEmpty()) {
        invalid.invoke()
    } else {
        valid.invoke(validationString)
    }
}

fun String?.isStringValid(): Boolean {
    val validationString = this?.trim()
    return !validationString.isNullOrEmpty()
}

inline fun <reified T> List<T>?.validateList(invalid: () -> Unit = {}, valid: (List<T>) -> Unit) {
    if (isNullOrEmpty()) {
        invalid.invoke()
    } else {
        valid.invoke(this)
    }
}