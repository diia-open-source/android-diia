package ua.gov.diia.core.util.event

import io.mockk.*
import org.junit.After
import org.junit.Test

private fun onEventUnhandledContent() = run { }

class UiEventObserverTest {

    @Test
    fun `UiEventObserver onchange`() {
        mockkStatic(::onEventUnhandledContent)
        justRun { onEventUnhandledContent() }
        val observer = UiEventObserver(::onEventUnhandledContent)
        observer.onChanged(UiEvent())
        verify { onEventUnhandledContent() }
        unmockkStatic(::onEventUnhandledContent)
    }

    @Test
    fun `UiEventObserver onchange not call if event was processed`() {
        mockkStatic(::onEventUnhandledContent)
        justRun { onEventUnhandledContent() }
        val observer = UiEventObserver(::onEventUnhandledContent)
        val event = UiEvent()
        event.handle()
        observer.onChanged(event)
        verify(exactly = 0) { onEventUnhandledContent() }
        unmockkStatic(::onEventUnhandledContent)
    }
    @After
    fun after() {
        clearAllMocks()
    }
}