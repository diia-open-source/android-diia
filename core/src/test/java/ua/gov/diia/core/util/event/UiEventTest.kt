package ua.gov.diia.core.util.event

import io.mockk.clearAllMocks
import org.junit.After
import org.junit.Assert
import org.junit.Test

class UiEventTest {

    @Test
    fun `get content is handled`() {
        val event = UiEvent()
        Assert.assertEquals(event.notHandedYet, true)
        Assert.assertEquals(event.hasBeenHandled, false)
        event.handle()
        Assert.assertEquals(event.notHandedYet, false)
        Assert.assertEquals(event.hasBeenHandled, true)
    }
    @After
    fun after() {
        clearAllMocks()
    }
}