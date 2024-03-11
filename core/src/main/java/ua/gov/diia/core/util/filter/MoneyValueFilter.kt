package ua.gov.diia.core.util.filter

import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.method.DigitsKeyListener


class MoneyValueFilter(
    private val digits: Int = 2,
    private val digitsSigned: Int = 7
) : DigitsKeyListener(false, true) {

    override fun filter(
        source: CharSequence, start: Int, end: Int,
        dest: Spanned, dstart: Int, dend: Int
    ): CharSequence {
        var sourceValue = source

        if (digits == 0) {
            if (source.length > 1) {
                if (source.any { it in "," }) {
                    sourceValue = SpannableString(source.toString().replace(",", ""))
                }
                if (source.any { it in "." }) {
                    sourceValue = SpannableString(source.toString().replace(".", ""))
                }
            } else {
                if (source.any { it in listOf(',', '.') }) {
                    return ""
                }
            }
        } else {
            if (source.any { it in "," }) {
                sourceValue = SpannableString(source.toString().replace(",", "."))
            }
        }

        var startValue = start
        var endValue = end
        val out = super.filter(sourceValue, startValue, endValue, dest, dstart, dend)

        // if changed, replace the source
        if (out != null) {
            sourceValue = out
            startValue = 0
            endValue = out.length
        }
        val len = endValue - startValue

        // if deleting, source is empty
        // and deleting can't break anything
        if (len == 0) {
            return sourceValue
        }

        if (dest.any { it in "." }) {
            val signed = filterSigned(dest.toString())
            val decimalPlace = filterDecimalPlace(dest.toString())
            return if (signed.length >= digitsSigned) {
                if (dstart == digitsSigned + 1 || dstart == digitsSigned + 2) {
                    SpannableStringBuilder(
                        sourceValue,
                        startValue,
                        endValue
                    )
                } else {
                    ""
                }
            } else {
                if (source != ".") {
                    if (dstart <= signed.length) {
                        SpannableStringBuilder(
                            sourceValue,
                            startValue,
                            endValue
                        )
                    } else {
                        if (decimalPlace.length >= digits) {
                            ""
                        } else {
                            SpannableStringBuilder(
                                sourceValue,
                                startValue,
                                endValue
                            )
                        }
                    }
                } else {
                    ""
                }
            }
        } else {
            return if (dest.length >= digitsSigned && source != ".") {
                ""
            } else {
                SpannableStringBuilder(
                    sourceValue,
                    startValue,
                    endValue
                )
            }
        }
    }

    private fun filterSigned(input: String): String {
        return input.substring(0, input.indexOf("."))
    }

    private fun filterDecimalPlace(input: String): String {
        return input.substring(input.indexOf(".") + 1, input.length)
    }
}