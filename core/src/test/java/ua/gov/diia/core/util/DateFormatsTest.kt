package ua.gov.diia.core.util

import org.junit.Assert.assertEquals
import org.junit.Test
import java.text.ParseException
import java.util.Calendar
import java.util.TimeZone

class DateFormatsTest {

    @Test
    fun iso8601ToLocalCalendar_validInput_returnsCalendar() {
        val iso8601String = "2022-01-01T12:34:56.789Z"

        val utcCalendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
        utcCalendar.set(2022, Calendar.JANUARY, 1, 12, 34, 56)
        utcCalendar.set(Calendar.MILLISECOND, 789)

        val resultCalendar = DateFormats.iso8601ToLocalCalendar(iso8601String)

        assertEquals(utcCalendar.time, resultCalendar.time)
    }

    @Test(expected = ParseException::class)
    fun `iso8601ToLocalCalendar throws ParseException if input is invalid`() {
        val iso8601String = "invalid_date"

        DateFormats.iso8601ToLocalCalendar(iso8601String)
    }
}