package ua.gov.diia.menu.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.MutableLiveData
import app.cash.turbine.test
import com.nhaarman.mockitokotlin2.any
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.stubbing.Answer
import ua.gov.diia.core.controller.NotificationController
import ua.gov.diia.ui_base.navigation.BaseNavigation
import ua.gov.diia.core.util.delegation.WithBuildConfig
import ua.gov.diia.core.util.delegation.WithErrorHandlingOnFlow
import ua.gov.diia.core.util.delegation.WithRetryLastAction
import ua.gov.diia.core.util.event.UiDataEvent
import ua.gov.diia.core.util.event.UiEvent
import ua.gov.diia.diia_storage.DiiaStorage
import ua.gov.diia.diia_storage.store.datasource.itn.ItnDataRepository
import ua.gov.diia.menu.MainDispatcherRule
import ua.gov.diia.menu.MenuContentController
import ua.gov.diia.ui_base.components.DiiaResourceIcon
import ua.gov.diia.ui_base.components.infrastructure.DataActionWrapper
import ua.gov.diia.ui_base.components.infrastructure.UIElementData
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.event.UIActionKeysCompose
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiText
import ua.gov.diia.ui_base.components.molecule.list.ListItemMlcData
import ua.gov.diia.ui_base.components.organism.header.TopGroupOrgData
import ua.gov.diia.ui_base.components.organism.list.ListItemGroupOrgData


@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class MenuComposeVMTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    lateinit var actionLogout: MutableLiveData<UiEvent>
    @Mock
    lateinit var errorHandling: WithErrorHandlingOnFlow
    @Mock
    lateinit var itnDataRepository: ItnDataRepository
    @Mock
    lateinit var retryLastAction: WithRetryLastAction
    @Mock
    lateinit var diiaStorage: DiiaStorage
    @Mock
    lateinit var withBuildConfig: WithBuildConfig
    @Mock
    lateinit var notificationController: NotificationController
    @Mock
    lateinit var menuContentController: MenuContentController
    lateinit var globalActionDocLoadingIndicator: MutableSharedFlow<UiDataEvent<Boolean>>

    lateinit var menuComposeVM: MenuComposeVM

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        actionLogout = MutableLiveData<UiEvent>()
        globalActionDocLoadingIndicator = MutableSharedFlow()

        menuComposeVM = MenuComposeVM(actionLogout, globalActionDocLoadingIndicator, errorHandling, retryLastAction,
            diiaStorage, itnDataRepository, withBuildConfig, menuContentController, notificationController)
    }

    @Test
    fun `test logout approve call logout action`() {
        menuComposeVM.logoutApprove()
        Assert.assertNotNull(actionLogout.value)
    }

    @Test
    fun `test emin back navigation on ui action`() {
        runTest {
            menuComposeVM.navigation.test {

                menuComposeVM.onUIAction(UIAction(UIActionKeysCompose.TOOLBAR_NAVIGATION_BACK))
                Assert.assertEquals(BaseNavigation.Back, awaitItem())
            }
        }
    }

    @Test
    fun `test emin open notification on ui action`() {
        runTest {
            menuComposeVM.navigation.test {
                menuComposeVM.onUIAction(UIAction(UIActionKeysCompose.LIST_ITEM_GROUP_ORG, action = DataActionWrapper(MenuActionsKey.OPEN_NOTIFICATION)))
                Assert.assertEquals(MenuTabNavigation.NavigateToNotifications, awaitItem())
            }
        }
    }
    @Test
    fun `test emin menu setting action on ui action function`() {
        runTest {
            menuComposeVM.settingsAction.test {
                menuComposeVM.onUIAction(UIAction(MenuActionsKey.LOGOUT))
                Assert.assertEquals(MenuAction.Logout, awaitItem().peekContent())

                menuComposeVM.onUIAction(UIAction(UIActionKeysCompose.LIST_ITEM_GROUP_ORG, action = DataActionWrapper(MenuActionsKey.OPEN_PLAY_MARKET)))
                Assert.assertEquals(MenuAction.OpenPlayMarketAction, awaitItem().peekContent())

                menuComposeVM.onUIAction(UIAction(UIActionKeysCompose.LIST_ITEM_GROUP_ORG, action = DataActionWrapper(MenuActionsKey.OPEN_HELP)))
                Assert.assertEquals(MenuAction.OpenHelpAction, awaitItem().peekContent())

                menuComposeVM.onUIAction(UIAction(UIActionKeysCompose.LIST_ITEM_GROUP_ORG, action = DataActionWrapper(MenuActionsKey.OPEN_DIIA_ID)))
                Assert.assertEquals(MenuAction.OpenDiiaId, awaitItem().peekContent())

                menuComposeVM.onUIAction(UIAction(UIActionKeysCompose.LIST_ITEM_GROUP_ORG, action = DataActionWrapper(MenuActionsKey.OPEN_SIGNE_HISTORY)))
                Assert.assertEquals(MenuAction.OpenSignHistory, awaitItem().peekContent())

                menuComposeVM.onUIAction(UIAction(UIActionKeysCompose.LIST_ITEM_GROUP_ORG, action = DataActionWrapper(MenuActionsKey.OPEN_APP_SESSIONS)))
                Assert.assertEquals(MenuAction.OpenAppSessions, awaitItem().peekContent())

                menuComposeVM.onUIAction(UIAction(UIActionKeysCompose.LIST_ITEM_GROUP_ORG, action = DataActionWrapper(MenuActionsKey.OPEN_SUPPORT)))
                Assert.assertEquals(MenuAction.OpenSupportAction, awaitItem().peekContent())

                menuComposeVM.onUIAction(UIAction(UIActionKeysCompose.LIST_ITEM_GROUP_ORG, action = DataActionWrapper(MenuActionsKey.OPEN_FAQ)))
                Assert.assertEquals(MenuAction.OpenFAQAction, awaitItem().peekContent())

                menuComposeVM.onUIAction(UIAction(UIActionKeysCompose.LIST_ITEM_GROUP_ORG, action = DataActionWrapper(MenuActionsKey.SHARE_APP)))
                Assert.assertEquals(MenuAction.ShareApp, awaitItem().peekContent())

                menuComposeVM.onUIAction(UIAction(UIActionKeysCompose.LIST_ITEM_GROUP_ORG, action = DataActionWrapper(MenuActionsKey.OPEN_SETTINGS)))
                Assert.assertEquals(MenuAction.OpenSettings, awaitItem().peekContent())

                menuComposeVM.onUIAction(UIAction(UIActionKeysCompose.LIST_ITEM_GROUP_ORG, action = DataActionWrapper(MenuActionsKey.OPEN_ABOUT_DIIA)))
                Assert.assertEquals(MenuAction.AboutDiia, awaitItem().peekContent())

                menuComposeVM.onUIAction(UIAction(UIActionKeysCompose.LIST_ITEM_GROUP_ORG, action = DataActionWrapper(MenuActionsKey.OPEN_POLICY)))
                Assert.assertEquals(MenuAction.OpenPolicyLink, awaitItem().peekContent())
            }
        }
    }

    @Test
    fun `test ui action pass device id in setting actions for coping`() {
        runTest {
            menuComposeVM.settingsAction.test {
                val mobileUuid = "mobile_id"
                `when`(diiaStorage.getMobileUuid()).thenReturn(mobileUuid)
                menuComposeVM.onUIAction(UIAction(UIActionKeysCompose.LIST_ITEM_GROUP_ORG, action = DataActionWrapper(MenuActionsKey.COPY_DEVICE_UID)))

                val result = awaitItem().peekContent()
                Assert.assertTrue(result is MenuAction.DoCopyDeviceUid)
                Assert.assertEquals(mobileUuid, (result as MenuAction.DoCopyDeviceUid).deviceUid)
            }
        }
    }
    @Test
    fun `test configureTopBar`() {
        runTest {
            menuComposeVM.configureTopBar()
            val topBarData = menuComposeVM.topBarData[0]
            assertTrue(topBarData is TopGroupOrgData)
            val result = topBarData as TopGroupOrgData
            assertEquals(result.titleGroupMlcData!!.heroText, UiText.DynamicString("Меню"))
        }
    }

    @Test
    fun `test updateMenuMessageItemData updates body show badge value if notification has OPEN_NOTIFICATION id`() {
        runTest {
            var notificationCallback: ((amount: Int) -> Unit)? = null
            `when`(
                notificationController.collectUnreadNotificationCounts(any())
            ).thenAnswer(
                Answer<Any?> { invocation ->
                    notificationCallback = (invocation.arguments[0] as (amount: Int) -> Unit)
                    null
                })
            menuComposeVM = MenuComposeVM(actionLogout, globalActionDocLoadingIndicator, errorHandling, retryLastAction,
                diiaStorage, itnDataRepository, withBuildConfig, menuContentController, notificationController)
            val menuList = mutableListOf<UIElementData>()
            val itemLabel = UiText.DynamicString("some_label")
            val listItemMlcData = ListItemMlcData(id = MenuActionsKey.OPEN_NOTIFICATION, label = itemLabel)
            val snapshotList = SnapshotStateList<ListItemMlcData>()
            snapshotList.add(listItemMlcData)
            menuList.add(ListItemGroupOrgData(MenuActionsKey.OPEN_NOTIFICATION, itemsList = snapshotList))

            `when`(menuContentController.configureBody(any(), any())).thenReturn(menuList)
            menuComposeVM.configureBody()
            notificationCallback!!.invoke(10)

            assertEquals(itemLabel, (menuComposeVM.bodyData[0] as ListItemGroupOrgData).itemsList[0].label)
        }
    }

    @Test
    fun `test updateMenuMessageItemData not update body show badge value if notification has OPEN_NOTIFICATION id`() {
        runTest {
            var notificationCallback: ((amount: Int) -> Unit)? = null
            `when`(
                notificationController.collectUnreadNotificationCounts(any())
            ).thenAnswer(
                Answer<Any?> { invocation ->
                    notificationCallback = (invocation.arguments[0] as (amount: Int) -> Unit)
                    null
                })
            menuComposeVM = MenuComposeVM(actionLogout, globalActionDocLoadingIndicator, errorHandling, retryLastAction,
                diiaStorage, itnDataRepository, withBuildConfig, menuContentController, notificationController)
            val menuList = mutableListOf<UIElementData>()
            val itemLabel = UiText.DynamicString("some_label")
            val listItemMlcData = ListItemMlcData(id = MenuActionsKey.OPEN_FAQ, label = itemLabel)
            val snapshotList = SnapshotStateList<ListItemMlcData>()
            snapshotList.add(listItemMlcData)
            menuList.add(ListItemGroupOrgData(MenuActionsKey.OPEN_FAQ, itemsList = snapshotList))

            `when`(menuContentController.configureBody(any(), any())).thenReturn(menuList)
            menuComposeVM.configureBody()
            notificationCallback!!.invoke(10)

            assertEquals(itemLabel, (menuComposeVM.bodyData[0] as ListItemGroupOrgData).itemsList[0].label)
        }
    }


    @Test
    fun `test updateMenuMessageItemData update left icon if no unread notification and notification has OPEN_NOTIFICATION id`() {
        runTest {
            var notificationCallback: ((amount: Int) -> Unit)? = null
            `when`(
                notificationController.collectUnreadNotificationCounts(any())
            ).thenAnswer(
                Answer<Any?> { invocation ->
                    notificationCallback = (invocation.arguments[0] as (amount: Int) -> Unit)
                    null
                })
            menuComposeVM = MenuComposeVM(actionLogout, globalActionDocLoadingIndicator, errorHandling, retryLastAction,
                diiaStorage, itnDataRepository, withBuildConfig, menuContentController, notificationController)
            val menuList = mutableListOf<UIElementData>()
            val itemLabel = UiText.DynamicString("some_label")
            val listItemMlcData = ListItemMlcData(id = MenuActionsKey.OPEN_NOTIFICATION, label = itemLabel)
            val snapshotList = SnapshotStateList<ListItemMlcData>()
            snapshotList.add(listItemMlcData)
            menuList.add(ListItemGroupOrgData(MenuActionsKey.OPEN_FAQ, itemsList = snapshotList))

            `when`(menuContentController.configureBody(any(), any())).thenReturn(menuList)
            menuComposeVM.configureBody()
            notificationCallback!!.invoke(10)
            notificationCallback!!.invoke(0)

            assertEquals(DiiaResourceIcon.NOTIFICATION_MESSAGE.code, (menuComposeVM.bodyData[0] as ListItemGroupOrgData).itemsList[0].iconLeft!!.code)
        }
    }
}