package ua.gov.diia.analytics.crashlytics

import android.util.Log
import com.huawei.agconnect.AGConnectInstance
import com.huawei.agconnect.crash.AGConnectCrash
import com.huawei.agconnect.crash.ICrash
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.verify
import org.junit.Before
import org.junit.Test

class WithCrashlyticsImplTest {

    private lateinit var agConnectCrashMock: AGConnectCrash

    private val impl = WithCrashlyticsImpl()

    @Before
    fun setUp() {
        mockkStatic(Log::class)
        every { Log.w(any(), any() as String) } returns 0
        every { Log.w(any(), any() as Throwable?) } returns 0

        mockkStatic(AGConnectInstance::class)
        val agConnectMock: AGConnectInstance = mockk(relaxed = true)
        every { AGConnectInstance.getInstance() } returns agConnectMock
        every { agConnectMock.getService<ICrash>(any()) } returns mockk<ICrash>(relaxed = true)

        agConnectCrashMock = mockk(relaxed = true)

        mockkStatic(AGConnectCrash::class)
        every { AGConnectCrash.getInstance() } returns agConnectCrashMock
    }

    @Test
    fun sendNonFatalError() {
        val error = RuntimeException("test")
        impl.sendNonFatalError(error)
        verify { agConnectCrashMock.recordException(error) }
    }
}