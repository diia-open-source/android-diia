package ua.gov.diia.documents.util.datasource

import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.verify
import org.junit.Assert
import org.junit.Test
import ua.gov.diia.core.util.date.CurrentDateProvider
import ua.gov.diia.core.util.extensions.date_time.getUTCDate
import ua.gov.diia.documents.models.Expiring
import ua.gov.diia.documents.models.Preferences
import java.util.Date

class ExpirationStrategyTest {

    @Test
    fun `test reset`() {
        val currentDateProvider = mockk<CurrentDateProvider>(relaxed = true)
        val strategy = DateCompareExpirationStrategy(currentDateProvider)
        strategy.reset()
        verify(atLeast = 2) { currentDateProvider.getDate() }
    }

    @Test
    fun `test illegal class`() {
        val currentDateProvider = mockk<CurrentDateProvider>(relaxed = true)
        val strategy = DateCompareExpirationStrategy(currentDateProvider)
        Assert.assertThrows(IllegalArgumentException::class.java) {
            strategy.isExpired(
                Any()
            )
        }
    }

    @Test
    fun `test date Preferences DEF`() {
        val currentDateProvider = mockk<CurrentDateProvider>(relaxed = true)
        val strategy = DateCompareExpirationStrategy(currentDateProvider)
        val element = mockk<Expiring>()
        every { element.getDocExpirationDate() } returns Preferences.DEF
        assert(strategy.isExpired(element))
    }

    @Test
    fun `test not expired date`(){
        val currentDateProvider = mockk<CurrentDateProvider>(relaxed = true)
        every { currentDateProvider.getDate() } returns Date()
        val strategy = DateCompareExpirationStrategy(currentDateProvider)
        val element = mockk<Expiring>()
        every { element.getDocExpirationDate() } returns ""
        val date = Date(System.currentTimeMillis() + (1000 * 60 * 60 * 24))
        mockkStatic(::getUTCDate)
        every { getUTCDate(any()) } returns date
        assert(strategy.isExpired(element).not())
    }

    @Test
    fun `test expired date`(){
        val currentDateProvider = mockk<CurrentDateProvider>(relaxed = true)
        val date = Date(System.currentTimeMillis() + (1000 * 60 * 60 * 24))
        every { currentDateProvider.getDate() } returns date
        val strategy = DateCompareExpirationStrategy(currentDateProvider)
        val element = mockk<Expiring>()
        every { element.getDocExpirationDate() } returns ""
        mockkStatic(::getUTCDate)
        every { getUTCDate(any()) } returns Date()
        assert(strategy.isExpired(element))
    }
}