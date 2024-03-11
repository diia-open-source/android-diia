package ua.gov.diia.notifications.ui.fragments.notifications.compose

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import ua.gov.diia.notifications.MainDispatcherRule
import ua.gov.diia.notifications.models.notification.pull.PullNotification
import ua.gov.diia.notifications.models.notification.pull.PullNotificationSyncAction
import ua.gov.diia.notifications.ui.fragments.home.notifications.compose.NotificationsMapperCompose
import ua.gov.diia.notifications.ui.fragments.home.notifications.compose.NotificationsMapperComposeImpl
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiText
import ua.gov.diia.ui_base.components.molecule.message.MessageMoleculeData
import ua.gov.diia.ui_base.components.molecule.message.StubMessageMlcData

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class NotificationsMapperComposeTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()
    lateinit var notificationsMapperComposeTestObject: NotificationsMapperComposeTestObject

    @Before
    fun setUp() {
        notificationsMapperComposeTestObject = NotificationsMapperComposeTestObject(NotificationsMapperComposeImpl())
    }

    @Test
    fun `test remap StubMessageMlcData`() {
        runBlocking {
            val item = StubMessageMlcData(UiText.DynamicString("icon"), UiText.DynamicString("title"))
            val result = notificationsMapperComposeTestObject.mapStubMessageMlcData(item)

            assertEquals(item.icon, result.icon)
            assertEquals(item.title, result.title)
        }
    }

    @Test
    fun `test map PullNotification to MessageMoleculeData`() {
        runBlocking {
            val notificationOne = PullNotification("notId", "creationDate", false, null, PullNotificationSyncAction.NONE)
            val result = notificationsMapperComposeTestObject.mapPullNotificationToMessageMoleculeData(notificationOne)

            assertEquals(null, result.shortText)
            assertEquals(null, result.title)
        }
    }

    class NotificationsMapperComposeTestObject(private val composeMapper: NotificationsMapperCompose,): NotificationsMapperCompose by composeMapper {
        fun mapStubMessageMlcData(item: StubMessageMlcData): StubMessageMlcData {
            return item.toComposeEmptyStateErrorMoleculeData()
        }
        fun mapPullNotificationToMessageMoleculeData(item: PullNotification): MessageMoleculeData {
            return item.toComposeMessage()
        }
    }
}
