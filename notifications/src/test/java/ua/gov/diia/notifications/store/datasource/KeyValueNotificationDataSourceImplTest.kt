package ua.gov.diia.notifications.store.datasource

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.nhaarman.mockitokotlin2.doThrow
import com.squareup.moshi.JsonAdapter
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
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
import ua.gov.diia.core.util.delegation.WithCrashlytics
import ua.gov.diia.diia_storage.DiiaStorage
import ua.gov.diia.diia_storage.store.Preferences
import ua.gov.diia.notifications.MainDispatcherRule
import ua.gov.diia.notifications.models.notification.pull.PullNotification
import ua.gov.diia.notifications.models.notification.pull.PullNotificationSyncAction
import ua.gov.diia.notifications.store.NotificationsPreferences
import ua.gov.diia.notifications.store.datasource.notifications.KeyValueNotificationDataSourceImpl

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class KeyValueNotificationDataSourceImplTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    @Mock
    lateinit var keyValueStore: DiiaStorage

    @Mock
    lateinit var withCrashlytics: WithCrashlytics

    @Mock
    lateinit var jsonAdapter: JsonAdapter<List<PullNotification>>

    lateinit var keyValueNotificationDataSourceImpl: KeyValueNotificationDataSourceImpl

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        keyValueNotificationDataSourceImpl =
            KeyValueNotificationDataSourceImpl(keyValueStore, withCrashlytics, jsonAdapter)
    }

    @Test
    fun `test save push token`() {
        runBlocking {
            val token = "token"
            keyValueNotificationDataSourceImpl.setPushToken(token)

            Mockito.verify(keyValueStore, Mockito.times(1))
                .set(NotificationsPreferences.PushToken, token)
        }
    }

    @Test
    fun `test set push token synced status`() {
        runBlocking {
            keyValueNotificationDataSourceImpl.setIsPushTokenSynced(true)

            Mockito.verify(keyValueStore, Mockito.times(1))
                .set(NotificationsPreferences.IsPushTokenSynced, true)
        }
    }

    @Test
    fun `test isNotificationRequested call store for data`() {
        runBlocking {
            `when`(
                keyValueStore.getBoolean(
                    NotificationsPreferences.NotificationsRequested,
                    false
                )
            ).thenReturn(true)
            val result = keyValueNotificationDataSourceImpl.isNotificationRequested()

            Mockito.verify(keyValueStore, Mockito.times(1))
                .getBoolean(NotificationsPreferences.NotificationsRequested, false)
            assertEquals(true, result)
        }
    }

    @Test
    fun `test denyNotifications should set NotificationsRequested as true`() {
        runBlocking {
            keyValueNotificationDataSourceImpl.denyNotifications()

            Mockito.verify(keyValueStore, Mockito.times(1))
                .set(NotificationsPreferences.NotificationsRequested, true)
        }
    }

    @Test
    fun `test requesting isPushTokenSynced from store`() {
        runBlocking {
            `when`(
                keyValueStore.getBoolean(
                    NotificationsPreferences.IsPushTokenSynced,
                    false
                )
            ).thenReturn(true)
            val result = keyValueNotificationDataSourceImpl.isPushTokenSynced()

            Mockito.verify(keyValueStore, Mockito.times(1))
                .getBoolean(NotificationsPreferences.IsPushTokenSynced, false)
            assertEquals(true, result)
        }
    }

    @Test
    fun `test allow notifications set AllowNotifications and NotificationsRequested data in store`() {
        runBlocking {
            keyValueNotificationDataSourceImpl.allowNotifications()

            Mockito.verify(keyValueStore, Mockito.times(1))
                .set(NotificationsPreferences.AllowNotifications, true)
            Mockito.verify(keyValueStore, Mockito.times(1))
                .set(NotificationsPreferences.NotificationsRequested, true)
        }
    }

    @Test
    fun `test updateUnreadCount save NotificationsUnreadCount in store`() {
        runBlocking {
            val unreadCount = 10
            keyValueNotificationDataSourceImpl.updateUnreadCount(unreadCount)

            Mockito.verify(keyValueStore, Mockito.times(1))
                .set(NotificationsPreferences.NotificationsUnreadCount, unreadCount)
        }
    }

    @Test
    fun `test fetchUnreadCount save NotificationsUnreadCount to 0`() {
        runBlocking {
            keyValueNotificationDataSourceImpl.fetchUnreadCount()

            Mockito.verify(keyValueStore, Mockito.times(1))
                .getInt(NotificationsPreferences.NotificationsUnreadCount, 0)
        }
    }

    @Test
    fun `test fetchData return empty store does not have data `() {
        runBlocking {
            `when`(keyValueStore.containsKey(NotificationsPreferences.NotificationsList)).thenReturn(
                false
            )
            val list = keyValueNotificationDataSourceImpl.fetchData()

            assertEquals(0, list.size)
        }
    }

    @Test
    fun `test fetchData return store parsed store data`() {
        runBlocking {
            val result = mutableListOf<PullNotification>()
            result.add(
                PullNotification(
                    "notid",
                    "createdate",
                    false,
                    null,
                    PullNotificationSyncAction.NONE
                )
            )

            val storedData = "stored_data";
            `when`(keyValueStore.containsKey(NotificationsPreferences.NotificationsList)).thenReturn(
                true
            )
            `when`(
                keyValueStore.getString(
                    NotificationsPreferences.NotificationsList,
                    Preferences.DEF
                )
            ).thenReturn(storedData)
            `when`(jsonAdapter.fromJson(storedData)).thenReturn(result)
            val list = keyValueNotificationDataSourceImpl.fetchData()

            assertEquals(1, list.size)
            assertEquals(result[0], list[0])
        }
    }


    @Test
    fun `test calling crashlytics and clear data if load data throw error`() {
        runBlocking {

            val storedData = "stored_data";
            `when`(keyValueStore.containsKey(NotificationsPreferences.NotificationsList)).thenReturn(
                true
            )
            `when`(
                keyValueStore.getString(
                    NotificationsPreferences.NotificationsList,
                    Preferences.DEF
                )
            ).thenReturn(storedData)
            val exception = RuntimeException()
            `when`(jsonAdapter.fromJson(storedData)).doThrow(exception)
            val list = keyValueNotificationDataSourceImpl.fetchData()

            assertEquals(0, list.size)
            Mockito.verify(keyValueStore, Mockito.times(1))
                .set(NotificationsPreferences.NotificationsList, "")
            Mockito.verify(withCrashlytics, Mockito.times(1))
                .sendNonFatalError(exception)
        }
    }
}