package ua.gov.diia.notifications.service

import android.content.Context
import android.net.Uri
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.work.WorkManager
import com.nhaarman.mockitokotlin2.mock
import io.mockk.every
import io.mockk.justRun
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.mockkStatic
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner
import ua.gov.diia.analytics.DiiaAnalytics
import ua.gov.diia.core.util.deeplink.DeepLinkActionFactory
import ua.gov.diia.core.util.event.UiEvent
import ua.gov.diia.core.util.extensions.context.isDiiaAppRunning
import ua.gov.diia.diia_storage.DiiaStorage
import ua.gov.diia.notifications.action.ActionConstants
import ua.gov.diia.notifications.helper.NotificationHelper
import ua.gov.diia.notifications.store.NotificationsPreferences
import ua.gov.diia.notifications.util.notification.manager.DiiaNotificationManager
import ua.gov.diia.notifications.work.SendPushTokenWork
import ua.gov.diia.notifications.work.SilentPushWork

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class PushServiceTest {

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    lateinit var context: Context

    lateinit var notificationHelper: NotificationHelper

    lateinit var deepLinkActionFactory: DeepLinkActionFactory

    @Mock
    lateinit var analytics: DiiaAnalytics

    lateinit var globalActionNotificationReceived: MutableLiveData<UiEvent>

    @Mock
    lateinit var diiaStorage: DiiaStorage

    @Mock
    lateinit var workManager: WorkManager

    lateinit var notificationManager: DiiaNotificationManager

    lateinit var pushService: PushService

    @Before
    fun setUp() {
        context = mockk()
        notificationManager = mockk(relaxed = true)
        deepLinkActionFactory = mockk(relaxed = true)
        notificationHelper = mockk(relaxed = true)
        MockitoAnnotations.initMocks(this)
        globalActionNotificationReceived = MutableLiveData<UiEvent>()
        pushService = PushService(
            context,
            notificationHelper,
            deepLinkActionFactory,
            analytics,
            globalActionNotificationReceived,
            diiaStorage,
            workManager,
            notificationManager
        )
    }

    @Test
    fun `test onNewToken`() {
        val token = "token"
        mockkObject(SendPushTokenWork)
        justRun { SendPushTokenWork.enqueue(workManager, token) }

        pushService.onNewToken(token)

        Mockito.verify(diiaStorage, Mockito.times(1)).set(NotificationsPreferences.IsPushTokenSynced, false)
        Mockito.verify(diiaStorage, Mockito.times(1)).set(NotificationsPreferences.PushToken, token)

        Mockito.verify(analytics, Mockito.times(1))
            .setPushToken(token)
        verify { SendPushTokenWork.enqueue(workManager, token) }
    }

    @Test
    fun `test processNotification`() {

        mockkStatic(LocalBroadcastManager::class)

        val localBroadcastManager = mockk<LocalBroadcastManager>()
        every { LocalBroadcastManager.getInstance(context) } returns localBroadcastManager
        every { localBroadcastManager.sendBroadcast(any()) } returns false
        `when`(
            diiaStorage.getBoolean(
                NotificationsPreferences.AllowNotifications,
                true
            )
        ).thenReturn(false)

        val notificationJson = "{" +
                "\"action\": {" +
                "\"resourceId\": \"resourceId\"," +
                "\"type\": \"type\"," +
                "\"subtype\": \"subtype\"" +
                "}," +
                "\"needAuth\": false," +
                "\"notificationId\": \"1\"," +
                "\"shortText\": \"shortText\"," +
                "\"title\": \"title\"," +
                "\"unread\": 1" +
                "}"

        pushService.processNotification(notificationJson)

        Mockito.verify(analytics, Mockito.times(1))
            .notificationReceived(notificationJson)
        Mockito.verify(analytics, Mockito.times(1))
            .pushReceived("resourceId")

        Mockito.verify(diiaStorage, Mockito.times(1))
            .getBoolean(NotificationsPreferences.AllowNotifications, true)

    }

    @Test
    fun `test processNotification start SilentPushWork for PushAccessibility type`() {

        mockkStatic(LocalBroadcastManager::class)
        val localBroadcastManager = mockk<LocalBroadcastManager>()
        every { LocalBroadcastManager.getInstance(context) } returns localBroadcastManager
        every { localBroadcastManager.sendBroadcast(any()) } returns false

        mockkObject(SilentPushWork)
        justRun { SilentPushWork.enqueue(workManager) }

        val notificationJson = "{" +
                "\"action\": {" +
                "\"resourceId\": \"resourceId\"," +
                "\"type\": \"${ActionConstants.NOTIFICATION_TYPE_PUSH_ACCESSIBILITY}\"," +
                "\"subtype\": \"subtype\"" +
                "}," +
                "\"needAuth\": false," +
                "\"notificationId\": \"1\"," +
                "\"shortText\": \"shortText\"," +
                "\"title\": \"title\"," +
                "\"unread\": 1" +
                "}"

        pushService.processNotification(notificationJson)

        verify { SilentPushWork.enqueue(workManager) }
    }

    @Test
    fun `test processNotification start activity for DocumentSharing type if app running`() {

        mockkStatic(Context::isDiiaAppRunning)
        mockkStatic(Uri::class)
        every { Uri.parse(any()) } returns mockk()
        mockkStatic(LocalBroadcastManager::class)
        val localBroadcastManager = mockk<LocalBroadcastManager>()
        every { LocalBroadcastManager.getInstance(context) } returns localBroadcastManager
        every { localBroadcastManager.sendBroadcast(any()) } returns false

        mockkObject(SilentPushWork)
        justRun { SilentPushWork.enqueue(workManager) }

        every { context.isDiiaAppRunning() } returns true
        justRun { context.startActivity(any()) }
        every { notificationHelper.getMainActivityIntent()} returns mock()
        every { deepLinkActionFactory.buildPathFromPushNotification(any()) } returns "result"

        val notificationJson = "{" +
                "\"action\": {" +
                "\"resourceId\": \"resourceId\"," +
                "\"type\": \"documentsSharing\"," +
                "\"subtype\": \"subtype\"" +
                "}," +
                "\"needAuth\": false," +
                "\"notificationId\": \"1\"," +
                "\"shortText\": \"shortText\"," +
                "\"title\": \"title\"," +
                "\"unread\": 1" +
                "}"

        pushService.processNotification(notificationJson)

        verify(exactly = 1) { context.startActivity(any()) }
    }

    @Test
    fun `test processNotification displayNotification for DocumentSharing type if app is not running`() {

        mockkStatic(Context::isDiiaAppRunning)

        mockkStatic(LocalBroadcastManager::class)
        val localBroadcastManager = mockk<LocalBroadcastManager>()
        every { LocalBroadcastManager.getInstance(context) } returns localBroadcastManager
        every { localBroadcastManager.sendBroadcast(any()) } returns false

        mockkObject(SilentPushWork)
        justRun { SilentPushWork.enqueue(workManager) }

        every { context.isDiiaAppRunning() } returns false
        justRun { context.startActivity(any()) }

        `when`(
            diiaStorage.getBoolean(NotificationsPreferences.AllowNotifications, true)
        ).thenReturn(false)

        val notificationJson = "{" +
                "\"action\": {" +
                "\"resourceId\": \"resourceId\"," +
                "\"type\": \"documentsSharing\"," +
                "\"subtype\": \"subtype\"" +
                "}," +
                "\"needAuth\": false," +
                "\"notificationId\": \"1\"," +
                "\"shortText\": \"shortText\"," +
                "\"title\": \"title\"," +
                "\"unread\": 1" +
                "}"

        pushService.processNotification(notificationJson)

        Mockito.verify(diiaStorage, Mockito.times(1))
            .getBoolean(NotificationsPreferences.AllowNotifications, true)
    }

}