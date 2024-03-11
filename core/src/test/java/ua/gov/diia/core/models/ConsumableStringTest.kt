package ua.gov.diia.core.models

import org.junit.Assert
import org.junit.Test

class ConsumableStringTest {
    @Test
    fun `item should not be consumed by default`() {
        Assert.assertEquals(
            ConsumableString("").isNotConsumed(),
            true
        )
    }

    @Test
    fun `item should be consumed if set true`() {
        Assert.assertEquals(
            ConsumableString("", true).isNotConsumed(),
            false
        )
    }

    @Test
    fun `item should be consumed after call consumeEvent`() {
        val item = ConsumableString("")
        Assert.assertEquals(
            item.isNotConsumed(),
            true
        )
        item.consumeEvent { }
        Assert.assertEquals(
            item.isNotConsumed(),
            false
        )
    }
}