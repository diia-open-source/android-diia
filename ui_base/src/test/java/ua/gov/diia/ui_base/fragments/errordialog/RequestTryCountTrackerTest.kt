package ua.gov.diia.ui_base.fragments.errordialog

import org.junit.Assert
import org.junit.Test

class RequestTryCountTrackerTest {

    @Test
    fun `increment should increase tryCount by 1`() {
        val tracker = RequestTryCountTracker(2)
        tracker.increment()
        Assert.assertEquals(tracker.tryCount, 3)
    }

    @Test
    fun `reset should set tryCount to 0`() {
        val tracker = RequestTryCountTracker(2)
        tracker.reset()
        Assert.assertEquals(tracker.tryCount, 0)
    }

    @Test
    fun `increment and reset should work together`() {
        val tracker = RequestTryCountTracker(2)
        tracker.increment()
        tracker.reset()
        Assert.assertEquals(tracker.tryCount, 0)
    }

    @Test
    fun `default value of tryCount should be 0`() {
        val tracker = RequestTryCountTracker()
        Assert.assertEquals(tracker.tryCount, 0)
    }
}
