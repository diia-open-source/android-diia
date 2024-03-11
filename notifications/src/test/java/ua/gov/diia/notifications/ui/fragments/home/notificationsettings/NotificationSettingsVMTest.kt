package ua.gov.diia.notifications.ui.fragments.home.notificationsettings

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import ua.gov.diia.core.models.dialogs.TemplateDialogModel
import ua.gov.diia.core.util.alert.ClientAlertDialogsFactory
import ua.gov.diia.notifications.MainDispatcherRule
import ua.gov.diia.notifications.data.data_source.network.api.notification.ApiNotifications
import ua.gov.diia.notifications.models.notification.SubscribeResponse
import ua.gov.diia.notifications.models.notification.Subscriptions
import java.net.ConnectException

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class NotificationSettingsVMTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    lateinit var notificationSettingsVM: NotificationSettingsVM
    lateinit var apiNotifications: ApiNotifications
    lateinit var clientAlertDialogsFactory: ClientAlertDialogsFactory
    lateinit var subscriptions: Subscriptions

    @Before
    fun setUp() {
        apiNotifications = mockk()
        clientAlertDialogsFactory = mockk()

        subscriptions = mockk()
        coEvery { apiNotifications.getSubscriptions() } returns subscriptions
        notificationSettingsVM = NotificationSettingsVM(apiNotifications, clientAlertDialogsFactory)
    }

    @Test
    fun `test init get subs`() {

        coVerify(exactly = 1) { apiNotifications.getSubscriptions() }
        assertEquals(subscriptions, notificationSettingsVM.subscriptions.value)
    }

    @Test
    fun `test consume exception on get subs with unknown error`() {
        runBlocking {
            val exception = RuntimeException("error")
            val mockTemplate = mockk<TemplateDialogModel>()
            coEvery { apiNotifications.getSubscriptions() } throws exception
            coEvery { clientAlertDialogsFactory.unknownErrorAlert(any(), e= any()) } returns mockTemplate
            notificationSettingsVM.getSubs()

            coVerify(exactly = 1) { clientAlertDialogsFactory.unknownErrorAlert(false, e= exception) }
            assertEquals(mockTemplate, notificationSettingsVM.error.value!!.peekContent())
        }
    }

    @Test
    fun `test consume exception on get subs with no internet error`() {
        runBlocking {
            val exception = ConnectException()
            val mockTemplate = mockk<TemplateDialogModel>()
            coEvery { apiNotifications.getSubscriptions() } throws exception
            coEvery { clientAlertDialogsFactory.alertNoInternet() } returns mockTemplate
            notificationSettingsVM.getSubs()

            coVerify(exactly = 1) { clientAlertDialogsFactory.alertNoInternet() }
            assertEquals(mockTemplate, notificationSettingsVM.error.value!!.peekContent())
        }
    }

    @Test
    fun `test success subscribe`() {
        runBlocking {
            val code = "code"
            val subscribeResponse = mockk<SubscribeResponse>()
            every { subscribeResponse.success } returns true
            every { subscribeResponse.template } returns null
            coEvery {  apiNotifications.subscribe(code) } returns subscribeResponse
            notificationSettingsVM.subscribe(code)

            coVerify(exactly = 1) {  apiNotifications.subscribe(code) }
            assertEquals(true, notificationSettingsVM.getSubs.value)
        }
    }

    @Test
    fun `test error subscribe`() {
        runBlocking {
            val code = "code"
            val subscribeResponse = mockk<SubscribeResponse>()
            val mockTemplate = mockk<TemplateDialogModel>()
            every { subscribeResponse.template } returns mockTemplate
            coEvery {  apiNotifications.subscribe(code) } returns subscribeResponse
            notificationSettingsVM.subscribe(code)

            coVerify(exactly = 1) {  apiNotifications.subscribe(code) }
            assertEquals(mockTemplate, notificationSettingsVM.error.value!!.peekContent())
        }
    }

    @Test
    fun `test subscribe throw exception`() {
        runBlocking {
            val code = "code"
            val exception = RuntimeException()
            val mockTemplate = mockk<TemplateDialogModel>()
            coEvery {  apiNotifications.subscribe(code) } throws exception
            coEvery { clientAlertDialogsFactory.unknownErrorAlert(false, e = exception) } returns mockTemplate
            notificationSettingsVM.subscribe(code)

            coVerify(exactly = 1) {  apiNotifications.subscribe(code) }
            assertEquals(mockTemplate, notificationSettingsVM.error.value!!.peekContent())
        }
    }

    @Test
    fun `test success unsubscribe`() {
        runBlocking {
            val code = "code"
            val subscribeResponse = mockk<SubscribeResponse>()
            every { subscribeResponse.success } returns true
            every { subscribeResponse.template } returns null
            coEvery {  apiNotifications.unsubscribe(code) } returns subscribeResponse
            notificationSettingsVM.unsubscribe(code)

            coVerify(exactly = 1) {  apiNotifications.unsubscribe(code) }
            assertEquals(true, notificationSettingsVM.getSubs.value)
        }
    }

    @Test
    fun `test error unsubscribe`() {
        runBlocking {
            val code = "code"
            val subscribeResponse = mockk<SubscribeResponse>()
            val mockTemplate = mockk<TemplateDialogModel>()
            every { subscribeResponse.template } returns mockTemplate
            coEvery {  apiNotifications.unsubscribe(code) } returns subscribeResponse
            notificationSettingsVM.unsubscribe(code)

            coVerify(exactly = 1) {  apiNotifications.unsubscribe(code) }
            assertEquals(mockTemplate, notificationSettingsVM.error.value!!.peekContent())
        }
    }

    @Test
    fun `test unsubscribe throw exception`() {
        runBlocking {
            val code = "code"
            val exception = RuntimeException()
            val mockTemplate = mockk<TemplateDialogModel>()
            coEvery {  apiNotifications.unsubscribe(code) } throws exception
            coEvery { clientAlertDialogsFactory.unknownErrorAlert(false, e = exception) } returns mockTemplate
            notificationSettingsVM.unsubscribe(code)

            coVerify(exactly = 1) {  apiNotifications.unsubscribe(code) }
            assertEquals(mockTemplate, notificationSettingsVM.error.value!!.peekContent())
        }
    }
}