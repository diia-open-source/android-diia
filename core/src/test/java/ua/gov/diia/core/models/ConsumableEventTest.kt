package ua.gov.diia.core.models

import org.junit.Assert
import org.junit.Test

class ConsumableEventTest {
    @Test
    fun `item should be consumed after call consumeEvent`() {
        val item = ConsumableEvent()
        Assert.assertEquals(
            item.isConsumed,
            false
        )
        item.consumeEvent {  }
        Assert.assertEquals(
            item.isConsumed,
            true
        )
    }
}