package ua.gov.diia.publicservice.ui.compose

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.asFlow
import app.cash.turbine.test
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import ua.gov.diia.ui_base.navigation.BaseNavigation
import ua.gov.diia.core.util.delegation.WithRetryLastAction
import ua.gov.diia.publicservice.helper.PublicServiceHelper
import ua.gov.diia.publicservice.rules.MainDispatcherRule
import ua.gov.diia.publicservice.util.StubErrorHandlerOnFlow
import ua.gov.diia.publicservice.util.testCategories
import ua.gov.diia.ui_base.components.infrastructure.DataActionWrapper
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.event.UIActionKeysCompose
import ua.gov.diia.ui_base.components.molecule.header.NavigationPanelMlcData

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class PublicServiceCategoryDetailsComposeVMTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var helper: PublicServiceHelper

    @Mock
    private lateinit var retryLastAction: WithRetryLastAction

    @Mock
    private lateinit var composeMapper: PublicServiceCategoryDetailsComposeMapper

    private lateinit var errorHandling: StubErrorHandlerOnFlow
    private lateinit var viewModel: PublicServiceCategoryDetailsComposeVM

    @Before
    fun before() {
        errorHandling = StubErrorHandlerOnFlow()
        viewModel = PublicServiceCategoryDetailsComposeVM(
            helper = helper,
            errorHandling = errorHandling,
            retryLastAction = retryLastAction,
            composeMapper = composeMapper
        )
    }

    @Test
    fun `screen content`() = runTest {
        viewModel.doInit(testCategories().categories.first())
        val toolbar = viewModel.toolBarData.singleOrNull() as? NavigationPanelMlcData
        Assert.assertEquals(UIActionKeysCompose.TOOLBAR_NAVIGATION_BACK, toolbar?.backAction)
        Assert.assertEquals(UIActionKeysCompose.TOOLBAR_CONTEXT_MENU, toolbar?.contextMenuAction)
    }

    @Test
    fun `open service`() = runTest {
        val category = testCategories().categories.first()
        val service = category.publicServices.first()
        viewModel.navigation.test {
            viewModel.onUIAction(UIAction(UIActionKeysCompose.LIST_ITEM_GROUP_ORG, service.code))
            expectNoEvents()
        }
        viewModel.doInit(category)
        Assert.assertEquals(category, viewModel.category.asFlow().first())
        viewModel.navigation.test {
            viewModel.onUIAction(
                UIAction(
                    actionKey = UIActionKeysCompose.LIST_ITEM_GROUP_ORG,
                    action = DataActionWrapper(type = ""),
                    data = service.code
                )
            )
            val event = awaitItem() as PublicServicesCategoriesDetailsNavigation.NavigateToService
            Assert.assertEquals(service, event.service)
        }
        viewModel.navigation.test {
            viewModel.onUIAction(UIAction(UIActionKeysCompose.LIST_ITEM_GROUP_ORG))
            expectNoEvents()
        }
    }

    @Test
    fun `open nonexistent service`() = runTest {
        val category = testCategories().categories.first()
        viewModel.doInit(category)
        Assert.assertEquals(category, viewModel.category.asFlow().first())
        viewModel.navigation.test {
            viewModel.onUIAction(UIAction(UIActionKeysCompose.LIST_ITEM_GROUP_ORG, "nonexistent"))
            expectNoEvents()
        }
    }

    @Test
    fun `navigate back`() = runTest {
        viewModel.navigation.test {
            viewModel.onUIAction(UIAction(UIActionKeysCompose.TOOLBAR_NAVIGATION_BACK))
            Assert.assertEquals(BaseNavigation.Back, awaitItem())
        }
    }

    @Test
    fun `empty actions`() = runTest {
        viewModel.navigation.test {
            viewModel.onUIAction(
                UIAction(
                    UIActionKeysCompose.LIST_ITEM_GROUP_ORG,
                    action = DataActionWrapper(""),
                    data = "test",
                )
            )
            expectNoEvents()
        }
        viewModel.navigation.test {
            viewModel.onUIAction(
                UIAction(
                    UIActionKeysCompose.LIST_ITEM_GROUP_ORG,
                    action = DataActionWrapper(""),
                )
            )
            expectNoEvents()
        }
        viewModel.navigation.test {
            viewModel.onUIAction(
                UIAction(UIActionKeysCompose.LIST_ITEM_GROUP_ORG)
            )
            expectNoEvents()
        }
    }
}
