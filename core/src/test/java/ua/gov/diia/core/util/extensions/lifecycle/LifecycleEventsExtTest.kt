package ua.gov.diia.core.util.extensions.lifecycle

import io.mockk.mockk
import io.mockk.verify
import org.junit.Assert
import org.junit.Test
import ua.gov.diia.core.util.event.UiDataEvent
import ua.gov.diia.core.util.event.UiEvent

class LifecycleEventsExtTest {

    @Test
    fun `consumeEvent should call the provided function with correct parameter`() {
        val testEvent = UiDataEvent("test")

        val todo = mockk<(String) -> Unit>(relaxed = true)
        testEvent.consumeEvent(todo)

        verify { todo.invoke("test") }

        Assert.assertEquals(true, testEvent.hasBeenHandled)
    }

    @Test
    fun `UiDataEvent consumeEvent should not call the provided function when the event has already been handled`() {
        val testEvent = UiDataEvent("test")
        testEvent.handle()
        val todo = mockk<(String) -> Unit>(relaxed = true)

        testEvent.consumeEvent(todo)

        verify(exactly = 0) { todo.invoke(any()) }
        Assert.assertEquals(true, testEvent.hasBeenHandled)
    }

    @Test
    fun `UiEvent consumeEvent should call the provided function when the event has not been handled yet`() {
        val testEvent = UiEvent()
        val todo = mockk<() -> Unit>(relaxed = true)

        testEvent.consumeEvent(todo)

        verify { todo.invoke() }
        Assert.assertEquals(false, testEvent.notHandedYet)
    }

    @Test
    fun `UiEvent consumeEvent should not call the provided function when the event has already been handled`() {
        val testEvent = UiEvent()
        testEvent.handle()

        val todo = mockk<() -> Unit>(relaxed = true)
        testEvent.consumeEvent(todo)

        verify(exactly = 0) { todo.invoke() }
        Assert.assertEquals(false, testEvent.notHandedYet)
    }
}
