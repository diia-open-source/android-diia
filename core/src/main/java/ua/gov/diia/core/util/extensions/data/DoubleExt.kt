package ua.gov.diia.core.util.extensions.data

fun Double.toCurrency() = "%.2f".format(this).replace(",", ".")

fun Double.toCurrencyUah() = "%.2f грн".format(this).replace(",", ".")

fun Double.percent(p: Double) = this / 100 * p

fun String.toAmount() = filter { it.isDigit() || it == ','}.replace(",", ".").toDouble()

fun String.toPrice() = filter { it.isDigit() }.toIntOrNull()