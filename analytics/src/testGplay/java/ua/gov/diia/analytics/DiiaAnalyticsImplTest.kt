package ua.gov.diia.analytics

import com.google.firebase.analytics.FirebaseAnalytics
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.verify
import org.junit.Before
import org.junit.Test

class DiiaAnalyticsImplTest {

    private lateinit var firebaseAnalytics: FirebaseAnalytics
    private lateinit var analytics: DiiaAnalytics

    @Before
    fun setUp() {
        firebaseAnalytics = mockk(relaxed = true)
        mockkStatic(FirebaseAnalytics::class)
        every { FirebaseAnalytics.getInstance(any()) } returns firebaseAnalytics

        analytics = DiiaAnalyticsImpl(mockk(relaxed = true))
    }

    @Test
    fun setUserId() {
        analytics.setUserId("test")
        verify { firebaseAnalytics.setUserId("test") }
    }

    @Test
    fun setPushToken() {
        analytics.setPushToken("test")
        verify { firebaseAnalytics.setUserProperty(DiiaAnalytics.PUSH_TOKEN_PROPERTY, "test") }
    }
}
