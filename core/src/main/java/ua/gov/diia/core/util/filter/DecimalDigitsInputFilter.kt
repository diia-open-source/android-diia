package ua.gov.diia.core.util.filter

import android.text.InputFilter
import android.text.Spanned
import java.util.regex.Pattern

class DecimalDigitsInputFilter(digitsBeforeZero: Int, digitsAfterZero: Int) : InputFilter {

    private val pattern =
        Pattern.compile("(\\d{0,$digitsBeforeZero})|(\\d{0,$digitsBeforeZero}\\.\\d{0,$digitsAfterZero})")

    override fun filter(
        source: CharSequence,
        start: Int,
        end: Int,
        dest: Spanned,
        dstart: Int,
        dend: Int
    ): CharSequence? {
        return if (source.isEmpty()) {
            if (pattern.matcher(dest.removeRange(dstart, dend)).matches()) {
                null
            } else {
                dest.subSequence(dstart, dend)
            }
        } else {
            if (pattern.matcher(dest.replaceRange(dstart, dend, source)).matches()) {
                null
            } else {
                ""
            }
        }
    }
}