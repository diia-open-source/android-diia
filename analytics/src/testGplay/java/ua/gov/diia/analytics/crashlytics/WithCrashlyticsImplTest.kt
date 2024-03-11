package ua.gov.diia.analytics.crashlytics

import com.google.firebase.crashlytics.FirebaseCrashlytics
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.verify
import org.junit.Before
import org.junit.Test
import java.io.IOException

class WithCrashlyticsImplTest {

    private lateinit var crashlyticsMock: FirebaseCrashlytics

    private val impl = WithCrashlyticsImpl()

    @Before
    fun setUp() {
        crashlyticsMock = mockk(relaxed = true)
        mockkStatic(FirebaseCrashlytics::class)
        every { FirebaseCrashlytics.getInstance() } returns crashlyticsMock
    }

    @Test
    fun `error with message`() {
        val error = RuntimeException("test")
        impl.sendNonFatalError(error)
        verify { crashlyticsMock.setCustomKey("message", "test") }
        verify { crashlyticsMock.recordException(error) }
    }

    @Test
    fun `error no message`() {
        val error = IOException()
        impl.sendNonFatalError(error)
        verify(inverse = true) { crashlyticsMock.setCustomKey(any<String>(), any<String>()) }
        verify { crashlyticsMock.recordException(error) }
    }
}
