package ua.gov.diia.home.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import androidx.navigation.NavDirections
import app.cash.turbine.test
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.doThrow
import com.nhaarman.mockitokotlin2.mock
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.stubbing.Answer
import ua.gov.diia.core.controller.DeeplinkProcessor
import ua.gov.diia.core.controller.NotificationController
import ua.gov.diia.core.controller.PromoController
import ua.gov.diia.core.models.deeplink.DeepLinkAction
import ua.gov.diia.core.models.deeplink.DeepLinkActionViewDocument
import ua.gov.diia.core.models.dialogs.TemplateDialogModel
import ua.gov.diia.core.models.dialogs.TemplateDialogModelWithProcessCode
import ua.gov.diia.ui_base.models.homescreen.HomeMenuItemConstructor
import ua.gov.diia.core.util.DispatcherProvider
import ua.gov.diia.core.util.delegation.WithCrashlytics
import ua.gov.diia.core.util.delegation.WithDeeplinkHandling
import ua.gov.diia.core.util.delegation.WithErrorHandling
import ua.gov.diia.core.util.delegation.WithRetryLastAction
import ua.gov.diia.core.util.event.UiDataEvent
import ua.gov.diia.home.MainDispatcherRule
import ua.gov.diia.home.model.HomeMenuItem
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.state.UIState
import ua.gov.diia.ui_base.components.organism.bottom.TabBarOrganismData

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class HomeVMTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    @Mock
    lateinit var promoController: PromoController

    @Mock
    lateinit var notificationController: NotificationController

    @Mock
    lateinit var itnDataSource: ua.gov.diia.diia_storage.store.datasource.itn.ItnDataRepository

    @Mock
    lateinit var dispatcherProvider: DispatcherProvider

    lateinit var allowAuthorizedLinksFlow: MutableSharedFlow<UiDataEvent<Boolean>>

    lateinit var globalActionDocLoadingIndicator: MutableSharedFlow<UiDataEvent<Boolean>>

    lateinit var globalActionConfirmDocumentRemoval: MutableStateFlow<UiDataEvent<String>?>

    lateinit var globalActionFocusOnDocument: MutableStateFlow<UiDataEvent<String>?>

    lateinit var globalActionSelectedMenuItem: MutableStateFlow<UiDataEvent<HomeMenuItemConstructor>?>

    @Mock
    lateinit var withRetryLastAction: WithRetryLastAction

    @Mock
    lateinit var errorHandlingDelegate: WithErrorHandling

    @Mock
    lateinit var deepLinkDelegate: WithDeeplinkHandling

    var composeMapper: HomeScreenComposeMapper = HomeScreenComposeMapperImpl()

    @Mock
    lateinit var deeplinkProcessor: DeeplinkProcessor

    @Mock
    lateinit var withCrashlytics: WithCrashlytics

    lateinit var viewModel: HomeVM

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        allowAuthorizedLinksFlow = MutableSharedFlow()
        globalActionDocLoadingIndicator = MutableSharedFlow()
        globalActionConfirmDocumentRemoval = MutableStateFlow(null)
        globalActionFocusOnDocument = MutableStateFlow(null)
        globalActionSelectedMenuItem = MutableStateFlow(null)

        `when`(dispatcherProvider.ioDispatcher()).thenReturn(UnconfinedTestDispatcher())

    }

    fun createVM() {

        viewModel = HomeVM(
            notificationController,
            dispatcherProvider,
            allowAuthorizedLinksFlow,
            globalActionDocLoadingIndicator,
            globalActionConfirmDocumentRemoval,
            globalActionFocusOnDocument,
            globalActionSelectedMenuItem,
            withRetryLastAction,
            errorHandlingDelegate,
            deepLinkDelegate,
            composeMapper,
            deeplinkProcessor,
            withCrashlytics
        )
    }

    @Test
    fun `test selectedMenuItem react on changes in globalActionSelectedMenuItem`() {
        runBlocking {
            val homeMenuItemMock = mock<HomeMenuItemConstructor>()
            var result: HomeMenuItemConstructor? = null
            val observer = Observer<UiDataEvent<HomeMenuItemConstructor>?>() {
                result = it!!.peekContent()
            }

            createVM()
            viewModel.selectedMenuItem.observeForever(observer)
            globalActionSelectedMenuItem.emit(UiDataEvent(homeMenuItemMock))

            assertEquals(homeMenuItemMock, result)
            viewModel.selectedMenuItem.removeObserver(observer)
        }
    }

    @Test
    fun `test isLoadIndicatorHomeScreen react on changes in globalActionDocLoadingIndicator`() {
        runBlocking {
            var result = true
            val observer = Observer<Boolean>() {
                result =it
            }
            createVM()
            viewModel.isLoadIndicatorHomeScreen.observeForever(observer)

            globalActionDocLoadingIndicator.emit(UiDataEvent(false))
            assertFalse(result)

            globalActionDocLoadingIndicator.emit(UiDataEvent(true))
            assertTrue(result)


            val uiDataEvent = mock<UiDataEvent<Boolean>>()
            `when`(uiDataEvent.getContentIfNotHandled()).thenReturn(null)
            globalActionDocLoadingIndicator.emit(uiDataEvent)
            assertFalse(result)

            viewModel.isLoadIndicatorHomeScreen.removeObserver(observer)
        }
    }
    @Test
    fun `test check calling of check promo`() {
        runBlocking {
            createVM()

            verify(promoController, times(1)).checkPromo(any())
        }
    }

    @Test
    fun `test triggering sendNonFatalError if checkPromo throw error`() {
        runBlocking {
            val exception = RuntimeException()

            `when`(promoController.checkPromo(any())).doThrow(exception)
            createVM()

            verify(withCrashlytics, times(1)).sendNonFatalError(exception)

        }
    }


    @Test
    fun `test show corresponded promo template`() {
        runBlocking {
            var callback: ((template: TemplateDialogModelWithProcessCode) -> Unit)? = null

            `when`(promoController.checkPromo(any())).thenAnswer(Answer {
                callback = (it.getArguments()
                    .get(0) as ((template: TemplateDialogModelWithProcessCode) -> Unit))
            })

            createVM()

            val templateDialogModel = mock<TemplateDialogModel>()
            val template = TemplateDialogModelWithProcessCode(1, templateDialogModel)

            callback!!(template)

            Assert.assertEquals(templateDialogModel, viewModel.showTemplate.value!!.peekContent())
        }
    }

    @Test
    fun `test changing of promo code from callback`() {
        runBlocking {
            var callback: ((template: TemplateDialogModelWithProcessCode) -> Unit)? = null

            `when`(promoController.checkPromo(any())).thenAnswer(Answer {
                callback = (it.getArguments()
                    .get(0) as ((template: TemplateDialogModelWithProcessCode) -> Unit))
            })

            createVM()

            val templateDialogModel = mock<TemplateDialogModel>()
            val template = TemplateDialogModelWithProcessCode(10, templateDialogModel)

            callback!!(template)

            verify(promoController, times(1)).updatePromoProcessCode(10)
        }
    }

    @Test
    fun `test invalidateDataSource trigger invalidation of notification and itn data sources`() {
        runBlocking {
            createVM()
            verify(notificationController, times(1)).invalidateNotificationDataSource()
            verify(itnDataSource, times(1)).invalidate()
        }
    }

    @Test
    fun `test invalidateDataSource trigger sendNonFatalError if notificationController throw exception`() {
        runBlocking {
            val exception = RuntimeException()

            `when`(notificationController.invalidateNotificationDataSource()).doThrow(exception)
            createVM()
            verify(withCrashlytics, times(1)).sendNonFatalError(exception)
        }
    }

    @Test
    fun `test invalidateDataSource trigger sendNonFatalError if itnDataSource throw exception`() {
        runBlocking {
            val exception = RuntimeException()

            `when`(itnDataSource.invalidate()).doThrow(exception)
            createVM()
            verify(withCrashlytics, times(1)).sendNonFatalError(exception)
        }
    }

    @Test
    fun `test creation of bottom data`() {
        runBlocking {
            createVM()

            val data = (viewModel.bottomData[0] as TabBarOrganismData)
            Assert.assertEquals(4, data.tabs.size)
        }
    }

    @Test
    fun `test select feed tab on initialization`() {
        runBlocking {
            createVM()
            Assert.assertEquals(
                HomeMenuItem.FEED,
                globalActionSelectedMenuItem.value!!.peekContent()
            )
            val tabs = (viewModel.bottomData[0] as TabBarOrganismData).tabs
            Assert.assertEquals(UIState.Selection.Selected, tabs[0].selectionState)
            Assert.assertEquals(UIState.Selection.Unselected, tabs[1].selectionState)
            Assert.assertEquals(UIState.Selection.Unselected, tabs[2].selectionState)
            Assert.assertEquals(UIState.Selection.Unselected, tabs[3].selectionState)
        }
    }
    @Test
    fun `test setting showBadges if hasUnreadNotifications was set`() {
        runBlocking {
            `when`(
                notificationController.collectUnreadNotificationCounts(any())
            ).thenAnswer { invocation ->
                    (invocation.arguments[0] as (amount: Int) -> Unit).invoke(10)
                    null
                }
            viewModel = HomeVM(
                notificationController,
                dispatcherProvider,
                allowAuthorizedLinksFlow,
                globalActionDocLoadingIndicator,
                globalActionConfirmDocumentRemoval,
                globalActionFocusOnDocument,
                globalActionSelectedMenuItem,
                withRetryLastAction,
                errorHandlingDelegate,
                deepLinkDelegate,
                composeMapper,
                deeplinkProcessor,
                withCrashlytics
            )

            val tabs = (viewModel.bottomData[0] as TabBarOrganismData).tabs
            Assert.assertEquals(true, tabs[3].showBadge)
        }
    }
    @Test
    fun `test check sync of push token`() {
        runBlocking {
            createVM()
            verify(notificationController, times(1)).checkPushTokenInSync()
        }
    }

    @Test
    fun `test send nonfatal if check push token throw exception`() {
        runBlocking {
            val exception = RuntimeException()

            `when`(notificationController.checkPushTokenInSync()).doThrow(exception)
            createVM()
            verify(withCrashlytics, times(1)).sendNonFatalError(exception)
        }
    }

    @Test
    fun `test collecting empty unread notifications`() {
        runBlocking {
            var callback: ((amount: Int) -> Unit)? = null

            `when`(notificationController.collectUnreadNotificationCounts(any())).thenAnswer(Answer {
                callback = (it.getArguments().get(0) as ((amount: Int) -> Unit))
            })
            createVM()
            callback!!(0)
            val data = (viewModel.bottomData[0] as TabBarOrganismData)
            Assert.assertEquals(false, viewModel.hasUnreadNotifications.value)
            Assert.assertEquals(4, data.tabs.size)
        }
    }

    @Test
    fun `test onUIAction select correct tab`() {
        runBlocking {
            createVM()

            viewModel.onUIAction(UIAction(HomeActions.HOME_DOCUMENTS.toString()))
            Assert.assertEquals(
                HomeMenuItem.DOCUMENTS,
                globalActionSelectedMenuItem.value!!.peekContent()
            )
            var tabs = (viewModel.bottomData[0] as TabBarOrganismData).tabs

            Assert.assertEquals(UIState.Selection.Selected, tabs[1].selectionState)
            Assert.assertEquals(UIState.Selection.Unselected, tabs[0].selectionState)
            Assert.assertEquals(UIState.Selection.Unselected, tabs[2].selectionState)
            Assert.assertEquals(UIState.Selection.Unselected, tabs[3].selectionState)

            viewModel.onUIAction(UIAction(HomeActions.HOME_FEED.toString()))
            Assert.assertEquals(
                HomeMenuItem.FEED,
                globalActionSelectedMenuItem.value!!.peekContent()
            )
            tabs = (viewModel.bottomData[0] as TabBarOrganismData).tabs
            Assert.assertEquals(UIState.Selection.Selected, tabs[0].selectionState)
            Assert.assertEquals(UIState.Selection.Unselected, tabs[1].selectionState)
            Assert.assertEquals(UIState.Selection.Unselected, tabs[2].selectionState)
            Assert.assertEquals(UIState.Selection.Unselected, tabs[3].selectionState)

            viewModel.onUIAction(UIAction(HomeActions.HOME_MENU.toString()))
            Assert.assertEquals(
                HomeMenuItem.MENU,
                globalActionSelectedMenuItem.value!!.peekContent()
            )
            tabs = (viewModel.bottomData[0] as TabBarOrganismData).tabs
            Assert.assertEquals(UIState.Selection.Selected, tabs[3].selectionState)
            Assert.assertEquals(UIState.Selection.Unselected, tabs[1].selectionState)
            Assert.assertEquals(UIState.Selection.Unselected, tabs[2].selectionState)
            Assert.assertEquals(UIState.Selection.Unselected, tabs[0].selectionState)

            viewModel.onUIAction(UIAction(HomeActions.HOME_SERVICES.toString()))
            Assert.assertEquals(
                HomeMenuItem.SERVICES,
                globalActionSelectedMenuItem.value!!.peekContent()
            )
            tabs = (viewModel.bottomData[0] as TabBarOrganismData).tabs
            Assert.assertEquals(UIState.Selection.Selected, tabs[2].selectionState)
            Assert.assertEquals(UIState.Selection.Unselected, tabs[1].selectionState)
            Assert.assertEquals(UIState.Selection.Unselected, tabs[0].selectionState)
            Assert.assertEquals(UIState.Selection.Unselected, tabs[3].selectionState)
        }
    }

    @Test
    fun `test confirmation of removing doc from gallery`() {
        runBlocking {
            createVM()
            val docName = "doc_name"
            viewModel.confirmRemoveDocFromGallery(docName)

            Assert.assertEquals(docName, globalActionConfirmDocumentRemoval.value!!.peekContent())
        }
    }

    @Test
    fun `test show data loading indicator`() {
        runTest {

            createVM()
            globalActionDocLoadingIndicator.test {
                viewModel.showDataLoadingIndicator(true)
                Assert.assertEquals(true, awaitItem().peekContent())
            }
        }
    }

    @Test
    fun `test allowAuthorizedDeepLinks emin flow with true value`() {
        runTest {
            createVM()
            allowAuthorizedLinksFlow.test {
                viewModel.allowAuthorizedDeepLinks()
                Assert.assertEquals(true, awaitItem().peekContent())
            }
        }
    }

    @Test
    fun `test checkNotificationsRequested pass value to notifications requested LiveData`() {
        runBlocking {
            createVM()

            `when`(notificationController.checkNotificationsRequested()).thenReturn(true)
            viewModel.checkNotificationsRequested()

            Assert.assertEquals(true, viewModel.notificationsRequested.value!!.peekContent())
        }
    }

    @Test
    fun `test allow notifications through notification controller`() {
        runBlocking {
            createVM()

            viewModel.allowNotifications()

            verify(notificationController, times(1)).allowNotifications()
        }
    }

    @Test
    fun `test deny notifications through notification controller`() {
        runBlocking {
            createVM()

            viewModel.denyNotifications()

            verify(notificationController, times(1)).denyNotifications()
        }
    }

    @Test
    fun `test handleDeepLinks collect deeplinks and handle them`() {
        runTest {
            val route = mock<NavDirections>()
            val deepLinkAction = DeepLinkActionViewDocument("", "", "")
            val deeplinkFlow =
                MutableStateFlow<UiDataEvent<DeepLinkAction>?>(UiDataEvent(deepLinkAction))

            `when`(deepLinkDelegate.deeplinkFlow).thenReturn(deeplinkFlow)
            `when`(deeplinkProcessor.handleDeepLinkAction(deepLinkAction)).thenReturn(route)

            createVM()
            val job = launch {
                viewModel.handleDeepLinks()
            }
            advanceUntilIdle()

            verify(deeplinkProcessor, times(1)).handleDeepLinkAction(deepLinkAction)
            Assert.assertEquals(route, viewModel.processNavigation.value!!.peekContent())
            job.cancel()
        }
    }

}