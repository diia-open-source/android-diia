package ua.gov.diia.core.util.extensions.date_time

import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockkStatic
import org.junit.After
import org.junit.Assert
import org.junit.Test
import java.time.Instant
import java.time.LocalDateTime
import java.time.Month
import java.time.ZoneId
import java.time.ZoneOffset
import java.util.*

class DateUtilsExtTest {

    @Test
    fun `getUTCDate should return null when given an invalid date`() {
        val invalidDate = "not a date"
        val result = getUTCDate(invalidDate)
        Assert.assertNull(result)
    }

    @Test
    fun `getUTCDate should return the correct date when given a valid date string`() {
        val dateString = "2022-03-22T12:00:00Z"
        val expectedDate = Date.from(Instant.parse(dateString))
        val result = getUTCDate(dateString)
        Assert.assertEquals(expectedDate, result)
    }

    @Test
    fun `getCurrentDateUtc should return the current date and time in UTC`() {
        val now = LocalDateTime.now(ZoneOffset.UTC)
        val expectedDate = Date.from(now.toInstant(ZoneOffset.UTC))
        val result = getCurrentDateUtc()
        Assert.assertEquals(expectedDate, result)
    }

    @Test
    fun `toLocalDateTime should return the correct LocalDateTime when given a valid date`() {
        mockkStatic(ZoneId::systemDefault)
        every { ZoneId.systemDefault() } returns ZoneId.of("UTC")

        val date = Date.from(Instant.parse("2022-03-22T12:00:00Z"))
        val expectedLocalDateTime =
            LocalDateTime.of(2022, Month.MARCH, 22, 12, 0, 0)

        val result = date.toLocalDateTime()
        Assert.assertEquals(expectedLocalDateTime, result)
    }

    @Test
    fun `toLocalDateTime should return null when throw error`() {
        mockkStatic(ZoneId::systemDefault)
        every { ZoneId.systemDefault() } throws java.lang.RuntimeException()

        val invalidDate = Date.from(Instant.EPOCH)
        val result = invalidDate.toLocalDateTime()
        Assert.assertNull(result)
    }
    @After
    fun after() {
        clearAllMocks()
    }
}
