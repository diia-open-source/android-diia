package ua.gov.diia.core.util.extensions

import org.junit.Assert
import org.junit.Test
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.util.concurrent.TimeoutException

class ErrorHandlingExtTest {
    @Test
    fun `SocketTimeoutException should be recognized as no internet`() {
        val exception = SocketTimeoutException()
        Assert.assertEquals(
            true,
            exception.noInternetException()
        )
    }

    @Test
    fun `TimeoutException should be recognized as no internet`() {
        val exception = TimeoutException()
        Assert.assertEquals(
            true,
            exception.noInternetException()
        )
    }

    @Test
    fun `UnknownHostException should be recognized as no internet`() {
        val exception = UnknownHostException()
        Assert.assertEquals(
            true,
            exception.noInternetException()
        )
    }

    @Test
    fun `ConnectException should be recognized as no internet`() {
        val exception = ConnectException()
        Assert.assertEquals(
            true,
            exception.noInternetException()
        )
    }

    @Test
    fun `NullPointerException should not be recognized as no internet`() {
        val exception = NullPointerException()
        Assert.assertEquals(
            false,
            exception.noInternetException()
        )
    }

    @Test
    fun `IllegalArgumentException should not be recognized as no internet`() {
        val exception = IllegalArgumentException()
        Assert.assertEquals(
            false,
            exception.noInternetException()
        )
    }
}