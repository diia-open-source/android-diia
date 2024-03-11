package ua.gov.diia.core.util.extensions.data

import java.text.DecimalFormat
import java.text.DecimalFormatSymbols

private val decimalFormatSymbols = DecimalFormatSymbols().apply {
    groupingSeparator = ' '
}
private val formatter = DecimalFormat("###,###,###", decimalFormatSymbols)

fun Int?.formatWithSpaces(): String {
    return this?.let {
        return try {
            formatter.format(it)
        } catch (e: java.lang.IllegalArgumentException) {
            ""
        }
    } ?: ""
}
