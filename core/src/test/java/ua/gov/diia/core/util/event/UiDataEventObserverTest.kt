package ua.gov.diia.core.util.event

import io.mockk.*
import org.junit.After
import org.junit.Test

private fun onEventUnhandledContent(data: String) = run { }

class UiDataEventObserverTest {

    @Test
    fun `UiEventObserver onchange`() {
        val data = "data"
        mockkStatic(::onEventUnhandledContent)
        justRun { onEventUnhandledContent(data) }
        val observer = UiDataEventObserver(::onEventUnhandledContent)
        observer.onChanged(UiDataEvent(data))
        verify { onEventUnhandledContent(data) }
        unmockkStatic(::onEventUnhandledContent)
    }

    @Test
    fun `UiEventObserver onchange not called if was handled`() {
        val data = "data"
        mockkStatic(::onEventUnhandledContent)
        justRun { onEventUnhandledContent(data) }
        val observer = UiDataEventObserver(::onEventUnhandledContent)
        val event = UiDataEvent(data)
        event.handle()
        observer.onChanged(event)
        verify(exactly = 0) { onEventUnhandledContent(data) }
        unmockkStatic(::onEventUnhandledContent)
    }
    @After
    fun after() {
        clearAllMocks()
    }
}