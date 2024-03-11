package ua.gov.diia.core.util.event

import io.mockk.clearAllMocks
import org.junit.After
import org.junit.Assert
import org.junit.Test

class UiDataEventTest {

    @Test
    fun `get content if not handled`() {
        val data = "data"
        val event = UiDataEvent(data)

        Assert.assertEquals(data, event.peekContent())
        Assert.assertEquals("UiDataEvent(content=$data)", event.toString())
        Assert.assertEquals(
            event.getContentIfNotHandled(),
            data
        )
        Assert.assertNull(event.getContentIfNotHandled())
    }

    @After
    fun after() {
        clearAllMocks()
    }
}