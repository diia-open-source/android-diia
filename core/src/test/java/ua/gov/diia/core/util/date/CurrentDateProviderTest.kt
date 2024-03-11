package ua.gov.diia.core.util.date

import io.mockk.every
import io.mockk.mockkStatic
import io.mockk.unmockkStatic
import org.junit.After
import org.junit.Assert
import org.junit.Test
import ua.gov.diia.core.util.extensions.date_time.getCurrentDateUtc
import java.util.Date

class CurrentDateProviderTest {

    @Test
    fun `test get current date`() {
        mockkStatic(::getCurrentDateUtc)
        val date = Date()
        every { getCurrentDateUtc() } returns date
        Assert.assertEquals(date, CurrentDateProviderImpl().getDate())
    }

    @After
    fun after(){
        unmockkStatic(::getCurrentDateUtc)
    }
}