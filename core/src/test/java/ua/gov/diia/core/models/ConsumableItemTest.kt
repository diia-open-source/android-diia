package ua.gov.diia.core.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import org.junit.Assert
import org.junit.Test

class ConsumableItemTest {
    @Test
    fun `item should not be consumed by default`() {
        Assert.assertEquals(
            ConsumableItem(TestParcelable()).isNotConsumed(),
            true
        )
    }
    @Test
    fun `item should be consumed if set true`() {
        Assert.assertEquals(
            ConsumableItem(TestParcelable(), true).isNotConsumed(),
            false
        )
    }
    @Test
    fun `item should be consumed after call consumeEvent`() {
        val item = ConsumableItem(TestParcelable())
        Assert.assertEquals(
            item.isNotConsumed(),
            true
        )
        item.consumeEvent<TestParcelable> {  }
        Assert.assertEquals(
            item.isNotConsumed(),
            false
        )
    }
}

@Parcelize
private data class TestParcelable(val param: String? = null): Parcelable