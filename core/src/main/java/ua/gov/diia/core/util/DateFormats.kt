package ua.gov.diia.core.util

import android.annotation.SuppressLint
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

@SuppressLint("SimpleDateFormat")
object DateFormats {

    private const val ISO8601 = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"
    val iso8601: SimpleDateFormat = SimpleDateFormat(ISO8601, Locale.UK)

    val uaDateFormat: SimpleDateFormat = SimpleDateFormat("dd.MM.yyyy")
    val fopDateFormat: SimpleDateFormat = SimpleDateFormat("dd MMMM yyyy", Locale("UK"))
    val penaltiesFormat: SimpleDateFormat = SimpleDateFormat("dd MMMM yyyy", Locale("UK"))
    val debtsFormat: SimpleDateFormat = SimpleDateFormat("dd MMMM yyyy", Locale("UK"))
    val criminalCertFileFormat: SimpleDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale("UK"))
    val calendarMonthFormat: SimpleDateFormat = SimpleDateFormat("MM.yyyy", Locale("UK"))

    @Throws(ParseException::class)
    fun iso8601ToLocalCalendar(iso8601string: String): Calendar {
        val calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat(ISO8601)
        dateFormat.timeZone = TimeZone.getTimeZone("UTC")
        val date = dateFormat.parse(iso8601string)
        calendar.time = date!!
        return calendar
    }

    init {
        iso8601.timeZone = TimeZone.getTimeZone("UTC")
    }
}