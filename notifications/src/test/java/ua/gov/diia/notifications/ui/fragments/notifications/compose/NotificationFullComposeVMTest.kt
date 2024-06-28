package ua.gov.diia.notifications.ui.fragments.notifications.compose

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import app.cash.turbine.test
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.justRun
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import ua.gov.diia.core.data.data_source.network.api.notification.ApiNotificationsPublic
import ua.gov.diia.core.models.common.message.TextParameter
import ua.gov.diia.core.models.common_compose.general.Action
import ua.gov.diia.core.models.common_compose.general.Body
import ua.gov.diia.core.models.common_compose.general.BottomGroup
import ua.gov.diia.core.models.common_compose.general.TopGroup
import ua.gov.diia.core.models.common_compose.mlc.header.TitleGroupMlc
import ua.gov.diia.core.models.common_compose.mlc.list.ListItemMlc
import ua.gov.diia.core.models.common_compose.mlc.text.TextLabelContainerMlc
import ua.gov.diia.core.models.common_compose.org.header.ChipTabsOrg
import ua.gov.diia.core.models.common_compose.mlc.header.NavigationPanelMlc
import ua.gov.diia.core.models.common_compose.org.header.TopGroupOrg
import ua.gov.diia.core.models.common_compose.org.list.ListItemGroupOrg
import ua.gov.diia.core.models.notification.pull.message.ArticlePicAtm
import ua.gov.diia.core.models.notification.pull.message.ArticleVideoMlc
import ua.gov.diia.core.models.notification.pull.message.MessageActions
import ua.gov.diia.core.models.notification.pull.message.NotificationFull
import ua.gov.diia.core.network.connectivity.ConnectivityObserver
import ua.gov.diia.ui_base.navigation.BaseNavigation
import ua.gov.diia.core.util.delegation.WithDeeplinkHandling
import ua.gov.diia.core.util.delegation.WithErrorHandlingOnFlow
import ua.gov.diia.core.util.delegation.WithRetryLastAction
import ua.gov.diia.core.util.event.UiEvent
import ua.gov.diia.notifications.MainDispatcherRule
import ua.gov.diia.notifications.data.data_source.network.api.notification.ApiNotifications
import ua.gov.diia.notifications.models.notification.pull.MessageIdentification
import ua.gov.diia.ui_base.components.atom.media.ArticlePicAtmData
import ua.gov.diia.ui_base.components.infrastructure.DataActionWrapper
import ua.gov.diia.ui_base.components.infrastructure.UIElementData
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.event.UIActionKeysCompose
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiIcon
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiText
import ua.gov.diia.ui_base.components.molecule.media.ArticleVideoMlcData
import ua.gov.diia.ui_base.components.molecule.text.TextLabelContainerMlcData
import ua.gov.diia.ui_base.components.organism.header.TopGroupOrgData
import ua.gov.diia.ui_base.components.organism.list.ListItemGroupOrgData

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class NotificationFullComposeVMTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    lateinit var notificationFullComposeVM: NotificationFullComposeVM

    lateinit var apiNotifications: ApiNotifications
    lateinit var apiNotificationsPublic: ApiNotificationsPublic
    lateinit var connectivityObserver: ConnectivityObserver
    lateinit var actionLogout: MutableLiveData<UiEvent>
    lateinit var errorHandling: WithErrorHandlingOnFlow
    lateinit var retryLastAction: WithRetryLastAction
    lateinit var deepLinkDelegate: WithDeeplinkHandling

    @Before
    fun setUp() {
        apiNotifications = mockk()
        apiNotificationsPublic = mockk()
        connectivityObserver = mockk()
        actionLogout = mockk()
        errorHandling = mockk(relaxed = true)
        retryLastAction = mockk(relaxed = true)
        deepLinkDelegate = mockk()
//        composeMapper = NotificationFullComposeMapperImpl()

        every { connectivityObserver.observe() } returns MutableSharedFlow()

        notificationFullComposeVM = NotificationFullComposeVM(
            apiNotifications,
            apiNotificationsPublic,
            connectivityObserver,
            actionLogout,
            errorHandling,
            retryLastAction,
            deepLinkDelegate
        )
    }

    @Test
    fun `test onUIAction call logout`() {
        justRun { actionLogout.postValue(any()) }
        notificationFullComposeVM.onUIAction(UIAction(actionKey = UIActionKeysCompose.LIST_ITEM_GROUP_ORG,
            action = DataActionWrapper(MessageActions.logout.name)))
        verify(exactly = 1) { actionLogout.postValue(any()) }
    }

    @Test
    fun `test onUIAction call open internal link`() {
        runTest {
            notificationFullComposeVM.openInternalLink.test {

                val url = "internallink"
                notificationFullComposeVM.onUIAction(UIAction(actionKey = UIActionKeysCompose.LIST_ITEM_GROUP_ORG,
                    action = DataActionWrapper(MessageActions.internalLink.name, null, url)))

                advanceUntilIdle()
                assertEquals(url, awaitItem())
            }
        }
    }

    @Test
    fun `test onUIAction open link for externalLink, default, downloadLink actions`() {
        runTest {
            notificationFullComposeVM.openLink.test {
                val data = "event_externalLink"
                notificationFullComposeVM.onUIAction(UIAction(actionKey = UIActionKeysCompose.LIST_ITEM_GROUP_ORG,
                    action = DataActionWrapper(MessageActions.externalLink.name, null, data)))

                assertEquals(data, awaitItem())
            }
            notificationFullComposeVM.openLink.test {
                val data = "event_default"
                notificationFullComposeVM.onUIAction(UIAction(actionKey = UIActionKeysCompose.LIST_ITEM_GROUP_ORG,
                    action = DataActionWrapper(MessageActions.default.name, null, data)))

                assertEquals(data, awaitItem())
            }
            notificationFullComposeVM.openLink.test {
                val data = "event_downloadLink"
                notificationFullComposeVM.onUIAction(UIAction(actionKey = UIActionKeysCompose.LIST_ITEM_GROUP_ORG,
                    action = DataActionWrapper(MessageActions.downloadLink.name, null, data)))

                assertEquals(data, awaitItem())
            }
        }
    }

    @Test
    fun `test onUIAction navigate back`() {
        runTest {
            notificationFullComposeVM.navigation.test {
                notificationFullComposeVM.onUIAction(UIAction(actionKey = UIActionKeysCompose.TOOLBAR_NAVIGATION_BACK))
                advanceUntilIdle()
                assertEquals(BaseNavigation.Back, awaitItem())
            }
        }
    }

    @Test
    fun `test loadNotification trigger pull notification api`() {
        runTest {
            val resId = "resid"
            val notId = "notid"

            notificationFullComposeVM.loadNotification(MessageIdentification(true, resId, notId))

            notificationFullComposeVM.contentLoaded.test {
                val item = awaitItem()
                assertEquals(UIActionKeysCompose.PAGE_LOADING_TRIDENT_WITH_BACK_NAVIGATION, item.first)
            }
            coVerify(exactly = 1) { apiNotifications.getPullNotification(notId) }
            coVerify(exactly = 0) { apiNotifications.getNotificationByMessageId(resId) }
            coVerify(exactly = 0) { apiNotificationsPublic.getMessage(resId) }

            clearAllMocks()
            notificationFullComposeVM.loadNotification(MessageIdentification(true, resId, ""))
            coVerify(exactly = 1) { apiNotifications.getNotificationByMessageId(resId) }
            coVerify(exactly = 0) { apiNotificationsPublic.getMessage(resId) }
            coVerify(exactly = 0) { apiNotifications.getPullNotification(notId) }

            clearAllMocks()
            notificationFullComposeVM.loadNotification(MessageIdentification(false, resId, ""))
            coVerify(exactly = 1) { apiNotificationsPublic.getMessage(resId) }
            coVerify(exactly = 0) { apiNotifications.getPullNotification(notId) }
            coVerify(exactly = 0) { apiNotifications.getNotificationByMessageId(resId) }

            clearAllMocks()
            notificationFullComposeVM.loadNotification(MessageIdentification(false, null, ""))
            coVerify(exactly = 0) { apiNotificationsPublic.getMessage(resId) }
            coVerify(exactly = 0) { apiNotifications.getPullNotification(notId) }
            coVerify(exactly = 0) { apiNotifications.getNotificationByMessageId(resId) }
        }
    }

    @Test
    fun `test loadNotification check mapping of top bar data`() {
        runTest {
            val topGroupOrg = TopGroup(
                TopGroupOrg(
                    ChipTabsOrg(listOf(), "label", "preselectedCode"),
                    NavigationPanelMlc(listOf(), "label"),
                    TitleGroupMlc(
                        heroText = "heroText",
                        leftNavIcon = TitleGroupMlc.LeftNavIcon(
                            "accessibilityDescription",
                            Action("type", "subtype", "resource"),
                            "code"
                        ),
                        mediumIconRight = TitleGroupMlc.MediumIconRight(
                            action = Action(
                                "type",
                                "subtype",
                                "resourse"
                            ),
                            "code"
                        ),
                        label = "label"
                    )
                ), null
            )
            coEvery { apiNotifications.getPullNotification(any()) } returns NotificationFull(
                null,
                null,
                listOf(topGroupOrg)
            )
            notificationFullComposeVM.loadNotification(MessageIdentification(true, "resId", "notId"))
            advanceUntilIdle()

            //Check top bar data
            val uiElemenet: UIElementData = notificationFullComposeVM.topBarData[0]
            assertTrue(uiElemenet is TopGroupOrgData)
            val titleGroupMlcData = uiElemenet as TopGroupOrgData
            assertEquals(UiText.DynamicString(topGroupOrg.topGroupOrg!!.titleGroupMlc!!.heroText!!), titleGroupMlcData.titleGroupMlcData!!.heroText)
            assertEquals(topGroupOrg.topGroupOrg!!.titleGroupMlc!!.mediumIconRight!!.action!!.resource!!, titleGroupMlcData.titleGroupMlcData!!.mediumIconRight!!.action!!.resource)
            assertEquals(UiText.DynamicString(topGroupOrg.topGroupOrg!!.titleGroupMlc!!.label!!), titleGroupMlcData.titleGroupMlcData!!.label)
        }
    }
    @Test
    fun `test loadNotification check mapping of body data`() {
        runTest {
            val body = mutableListOf<Body>()
            val textParamData = TextParameter.Data("alt", "name", "resource")
            val textParam = TextParameter(
                data = textParamData,
                type = "type"
            )
            val itemListViewOrgItem = ListItemMlc(Action("ResourceItemListViewOrgItem", "SubTypeItemListViewOrgItem", "resource"),
                "DescriptionItemListViewOrgItem", ListItemMlc.IconLeft("IconLeftItemListViewOrgItem"), ListItemMlc.IconRight("IconRightItemListViewOrgItem"), "LabelItemListViewOrgItem", "LogoLeftItemListViewOrgItem", "StateItemListViewOrgItem", "state")

            body.add(
                Body(
                    articlePicAtm = ArticlePicAtm("ArticlePicAtmImage"),
                    articleVideoMlc = ArticleVideoMlc("ArticleVideoAtm", null),
                    listItemGroupOrg = ListItemGroupOrg(listOf(itemListViewOrgItem), "title"),
                    textLabelContainerMlc = TextLabelContainerMlc("label", "text", listOf(textParam)),
                    btnIconRoundedGroupOrg = null, halvedCardCarouselOrg = null,
                    imageCardMlc = null, sectionTitleAtm = null, smallNotificationCarouselOrg = null,
                    verticalCardCarouselOrg = null, mediaTitleOrg = null, articlePicCarouselOrg = null,
                    whiteCardMlc = null
                )
            )

            coEvery { apiNotifications.getPullNotification(any()) } returns NotificationFull(
                body,
                null,
                null
            )
            notificationFullComposeVM.loadNotification(MessageIdentification(true, "resId", "notId"))
            advanceUntilIdle()

            //Check body
            val textContainer = notificationFullComposeVM.bodyData[0] as TextLabelContainerMlcData
            val articlePicAtmImage = notificationFullComposeVM.bodyData[1] as ArticlePicAtmData
            val articleVideoAtmVideo = notificationFullComposeVM.bodyData[2] as ArticleVideoMlcData
            val itemListViewOrgItems = notificationFullComposeVM.bodyData[3] as ListItemGroupOrgData
            val itemListViewOrgItemResult = itemListViewOrgItems.itemsList[0]
            //check  mapping body textContainer
            val bodyItem = body[0];
            val parameter = textContainer.data!!.parameters!![0]
            val parameterData = parameter.data!!
            assertEquals(UiText.DynamicString(bodyItem.textLabelContainerMlc!!.text!!), textContainer.data!!.text)
            assertEquals(textParam.type, parameter.type)
            assertEquals(UiText.DynamicString(textParamData.alt!!), parameterData.alt)
            assertEquals(UiText.DynamicString(textParamData.name!!), parameterData.name)
            assertEquals(UiText.DynamicString(textParamData.resource!!), parameterData.resource)

            //check  mapping body articlePicAtmImage
            assertEquals(bodyItem.articlePicAtm!!.image , articlePicAtmImage.url)

            //check mapping body articleVideoAtm
            assertEquals(bodyItem.articleVideoMlc!!.source , articleVideoAtmVideo.url)

            //check mapping body itemListViewOrgItems
            assertEquals(UiText.DynamicString(itemListViewOrgItem.label!!), itemListViewOrgItemResult.label!!)
            assertEquals(itemListViewOrgItem.action!!.type, itemListViewOrgItemResult.action!!.type)
            assertEquals(itemListViewOrgItem.action!!.resource, itemListViewOrgItemResult.action!!.resource)
            assertEquals(UiText.DynamicString(itemListViewOrgItem.description!!), itemListViewOrgItemResult.description)
            assertEquals(itemListViewOrgItem.iconLeft!!.code!!, itemListViewOrgItemResult.iconLeft!!.code)
            assertEquals(itemListViewOrgItem.iconRight!!.code!!, itemListViewOrgItemResult.iconRight!!.code)
            assertEquals(
                UiIcon.DynamicIconBase64(itemListViewOrgItem.logoLeft!!), itemListViewOrgItemResult.logoLeft!!)
            assertEquals("LabelItemListViewOrgItem", itemListViewOrgItemResult.id)
        }
    }

    @Test
    fun `test loadNotification check mapping of bottom data`() {
        runTest {
            val itemListViewOrgItem = ListItemMlc(
                Action("ResourceItemListViewOrgItem", "SubTypeItemListViewOrgItem", "respurce"),
                "DescriptionItemListViewOrgItem",
                ListItemMlc.IconLeft("IconLeftItemListViewOrgItem"), ListItemMlc.IconRight("IconRightItemListViewOrgItem"), "LabelItemListViewOrgItem", "LogoLeftItemListViewOrgItem", "StateItemListViewOrgItem", "state")

            val bottomGroup = mutableListOf<BottomGroup>()
            bottomGroup.add(BottomGroup(ListItemGroupOrg(listOf(itemListViewOrgItem), "ItemListViewOrgTitle")))

            coEvery { apiNotifications.getPullNotification(any()) } returns NotificationFull(
                null,
                bottomGroup,
                null
            )
            notificationFullComposeVM.loadNotification(MessageIdentification(true, "resId", "notId"))
            advanceUntilIdle()

            //Check bottom data mapping
            val bottomListItem = notificationFullComposeVM.bottomData[0] as ListItemGroupOrgData
            val bottomItem = bottomListItem.itemsList[0]
            assertEquals(UiText.DynamicString(itemListViewOrgItem.label!!), bottomItem.label)
            assertEquals(itemListViewOrgItem.action!!.type, bottomItem.action!!.type)
            assertEquals(itemListViewOrgItem.action!!.resource, bottomItem.action!!.resource)
            assertEquals(UiText.DynamicString(itemListViewOrgItem.description!!), bottomItem.description)
            assertEquals(itemListViewOrgItem.iconLeft!!.code!!, bottomItem.iconLeft!!.code)
            assertEquals(itemListViewOrgItem.iconRight!!.code!!, bottomItem.iconRight!!.code)
            assertEquals(UiIcon.DynamicIconBase64(itemListViewOrgItem.logoLeft!!), bottomItem.logoLeft!!)
            assertEquals("LabelItemListViewOrgItem", bottomItem.id)
        }
    }
}