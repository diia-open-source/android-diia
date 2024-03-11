package ua.gov.diia.notifications.store.datasource

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.nhaarman.mockitokotlin2.mock
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner
import ua.gov.diia.notifications.MainDispatcherRule
import ua.gov.diia.notifications.data.data_source.network.api.notification.ApiNotifications
import ua.gov.diia.notifications.models.notification.pull.PullNotificationsToModify
import ua.gov.diia.notifications.store.datasource.notifications.NetworkNotificationDataSource

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class NetworkNotificationDataSourceTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    @Mock
    lateinit var apiNotifications: ApiNotifications

    lateinit var networkNotificationDataSource: NetworkNotificationDataSource

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        networkNotificationDataSource =
            NetworkNotificationDataSource(apiNotifications)
    }

    @Test
    fun `test markNotificationsAsRead call api method`() {
        runBlocking {
            val modify = mock< PullNotificationsToModify>()
            networkNotificationDataSource.markNotificationsAsRead(modify)

            Mockito.verify(apiNotifications, Mockito.times(1))
                .markNotificationsAsRead(modify)
        }
    }

    @Test
    fun `test deleteNotifications call api method`() {
        runBlocking {
            val modify = mock< PullNotificationsToModify>()
            networkNotificationDataSource.deleteNotifications(modify)

            Mockito.verify(apiNotifications, Mockito.times(1))
                .deleteNotifications(modify)
        }
    }
}