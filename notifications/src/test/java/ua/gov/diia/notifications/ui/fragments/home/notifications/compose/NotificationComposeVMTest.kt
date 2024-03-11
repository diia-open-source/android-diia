package ua.gov.diia.notifications.ui.fragments.home.notifications.compose

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavDirections
import app.cash.turbine.test
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import ua.gov.diia.core.models.notification.pull.PullNotificationItemSelection
import ua.gov.diia.core.models.notification.push.PushAction
import ua.gov.diia.ui_base.navigation.BaseNavigation
import ua.gov.diia.core.util.DispatcherProvider
import ua.gov.diia.core.util.delegation.WithErrorHandlingOnFlow
import ua.gov.diia.core.util.delegation.WithRetryLastAction
import ua.gov.diia.core.util.event.UiDataEvent
import ua.gov.diia.core.util.event.UiEvent
import ua.gov.diia.notifications.MainDispatcherRule
import ua.gov.diia.notifications.TestDispatcherProvider
import ua.gov.diia.notifications.data.data_source.network.api.notification.ApiNotifications
import ua.gov.diia.notifications.helper.NotificationHelper
import ua.gov.diia.notifications.models.notification.pull.PullNotification
import ua.gov.diia.notifications.models.notification.pull.PullNotificationMessage
import ua.gov.diia.notifications.store.datasource.notifications.NotificationDataRepository
import ua.gov.diia.ui_base.components.infrastructure.DataActionWrapper
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.event.UIActionKeysCompose
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiText
import ua.gov.diia.ui_base.components.organism.header.TopGroupOrgData

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class NotificationComposeVMTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    lateinit var notificationsDataSource: NotificationDataRepository
    lateinit var apiNotifications: ApiNotifications
    lateinit var dispatcherProvider: DispatcherProvider
    lateinit var composeMapper: NotificationsMapperCompose
    lateinit var retryLastAction: WithRetryLastAction
    lateinit var errorHandling: WithErrorHandlingOnFlow
    lateinit var actionNotificationReceived: MutableLiveData<UiEvent>
    lateinit var actionNotificationRead: MutableLiveData<UiDataEvent<String>>
    lateinit var notificationHelper: NotificationHelper

    lateinit var notificationComposeVM: NotificationComposeVM


    @Before
    fun setUp() {
        notificationsDataSource = mockk(relaxed = true)
        apiNotifications = mockk()
        dispatcherProvider = TestDispatcherProvider()
        composeMapper = NotificationsMapperComposeImpl()
        errorHandling = mockk(relaxed = true)
        retryLastAction = mockk(relaxed = true)
        actionNotificationReceived = MutableLiveData<UiEvent>()
        actionNotificationRead = MutableLiveData<UiDataEvent<String>>()
        notificationHelper = mockk()

        notificationComposeVM = NotificationComposeVM(
            notificationsDataSource,
            dispatcherProvider,
            composeMapper,
            retryLastAction,
            errorHandling,
            notificationHelper
        )
    }

    @Test
    fun `test onUIAction navigate back`() {
        runTest {
            notificationComposeVM.navigation.test {
                notificationComposeVM.onUIAction(UIAction(actionKey = UIActionKeysCompose.TOOLBAR_NAVIGATION_BACK))
                advanceUntilIdle()
                Assert.assertEquals(BaseNavigation.Back, awaitItem())
            }
        }
    }

    @Test
    fun `test onUIAction remove notification`() {
        runTest {
            val notId = "notId"
            notificationComposeVM.onUIAction(
                UIAction(
                    actionKey = NotificationsActionKey.REMOVE_NOTIFICATION,
                    data = notId
                )
            )
            advanceUntilIdle()
            coVerify(exactly = 1) { notificationsDataSource.removeNotification(notId) }
        }
    }

    @Test
    fun `test onUIAction open notification settings`() {
        runTest {
            notificationComposeVM.navigation.test {
                notificationComposeVM.onUIAction(
                    UIAction(
                        actionKey = UIActionKeysCompose.TITLE_GROUP_MLC,
                        action = DataActionWrapper(type = NotificationsActionKey.OPEN_NOTIFICATION_SETTINGS)
                    )
                )
                advanceUntilIdle()
                assertEquals(NotificationsNavigation.NavigateToNotificationsSettings, awaitItem())
            }
        }
    }

    @Test
    fun `test onUIAction select message notification`() {
        runTest {
            val notId = "notId"
            val resourceId = "resourceId"
            val type = "mesage"
            val subtype = "subtype"
            val pullNotification = PullNotification(
                notId, null, null,
                PullNotificationMessage(
                    "icon", "title", "shortText",
                    PushAction(resourceId, type, subtype)
                )
            )

            coEvery { notificationsDataSource.getPullNotificationById(notId) } returns pullNotification
            coEvery { notificationHelper.isMessageNotification(type) } returns true
            notificationComposeVM.onMessageNotificationSelected.test {
                notificationComposeVM.onUIAction(
                    UIAction(
                        actionKey = NotificationsActionKey.SELECT_NOTIFICATION,
                        data = notId
                    )
                )

                coVerify(exactly = 1) { notificationsDataSource.getPullNotificationById(notId) }
                coVerify(exactly = 1) { notificationsDataSource.markNotificationAsRead(notId) }

                val messageItem = awaitItem().peekContent()
                assertEquals(notId, messageItem.notificationId)
                assertEquals(resourceId, messageItem.resourceId)
                assertEquals(type, messageItem.resourceType)
                assertEquals(subtype, messageItem.resourceSubtype)
            }
        }
    }

    @Test
    fun `test onUIAction select non message notification`() {
        runTest {
            val notId = "notId"
            val resourceId = "resourceId"
            val type = "sometype"
            val subtype = "subtype"
            val pullNotification = PullNotification(
                notId, null, null,
                PullNotificationMessage(
                    "icon", "title", "shortText",
                    PushAction(resourceId, type, subtype)
                )
            )

            coEvery { notificationsDataSource.getPullNotificationById(notId) } returns pullNotification
            coEvery { notificationHelper.isMessageNotification(type) } returns false
            notificationComposeVM.openResource.test {
                notificationComposeVM.onUIAction(
                    UIAction(
                        actionKey = NotificationsActionKey.SELECT_NOTIFICATION,
                        data = notId
                    )
                )

                coVerify(exactly = 1) { notificationsDataSource.getPullNotificationById(notId) }
                coVerify(exactly = 1) { notificationsDataSource.markNotificationAsRead(notId) }

                val messageItem = awaitItem().peekContent()
                assertEquals(notId, messageItem.notificationId)
                assertEquals(resourceId, messageItem.resourceId)
                assertEquals(type, messageItem.resourceType)
                assertEquals(subtype, messageItem.resourceSubtype)
            }
        }
    }

    @Test
    fun `test configureTopBar`() {
        notificationComposeVM.configureTopBar()
        assertTrue(notificationComposeVM.topBarData[0] is TopGroupOrgData)
        val titleGroupMlcData = notificationComposeVM.topBarData[0] as TopGroupOrgData
        assertEquals(UiText.DynamicString("Повідомлення"), titleGroupMlcData.titleGroupMlcData!!.heroText)
    }

    @Test
    fun `test navigateToDirection`() {
        runTest {
            val notId = "notId"
            val resourceId = "resourceId"
            val type = "sometype"
            val subtype = "subtype"
            val item = PullNotificationItemSelection(notId, resourceId, type, subtype)
            val navDirections = mockk<NavDirections>()

            coEvery { notificationHelper.navigateToDocument(item) } returns navDirections
            notificationComposeVM.navigateTo.test {
                notificationComposeVM.navigateToDirection(item)

                coVerify(exactly = 1) { notificationHelper.navigateToDocument(item) }

                assertEquals(navDirections, awaitItem())
            }
        }
    }

    @Test
    fun `test configureBody`() {
        runTest {
            val notId = "notId"
            val resourceId = "resourceId"
            val type = "sometype"
            val subtype = "subtype"
            val item = PullNotificationItemSelection(notId, resourceId, type, subtype)
            val navDirections = mockk<NavDirections>()

            coEvery { notificationHelper.navigateToDocument(item) } returns navDirections
            notificationComposeVM.navigateTo.test {
                notificationComposeVM.navigateToDirection(item)

                coVerify(exactly = 1) { notificationHelper.navigateToDocument(item) }

                assertEquals(navDirections, awaitItem())
            }
        }
    }
}