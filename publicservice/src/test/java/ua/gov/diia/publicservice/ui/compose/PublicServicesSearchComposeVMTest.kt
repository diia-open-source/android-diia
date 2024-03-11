package ua.gov.diia.publicservice.ui.compose

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.asFlow
import app.cash.turbine.test
import com.nhaarman.mockitokotlin2.any
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.yield
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner
import ua.gov.diia.ui_base.navigation.BaseNavigation
import ua.gov.diia.core.util.delegation.WithRetryLastAction
import ua.gov.diia.publicservice.helper.PublicServiceHelper
import ua.gov.diia.publicservice.models.PublicServiceView
import ua.gov.diia.publicservice.models.toDisplayService
import ua.gov.diia.publicservice.rules.MainDispatcherRule
import ua.gov.diia.publicservice.ui.compose.PublicServicesCategoriesSearchNavigation.NavigateToService
import ua.gov.diia.publicservice.util.StubErrorHandlerOnFlow
import ua.gov.diia.publicservice.util.TestDispatcherProvider
import ua.gov.diia.publicservice.util.testCategories
import ua.gov.diia.ui_base.components.infrastructure.DataActionWrapper
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.event.UIActionKeysCompose
import ua.gov.diia.ui_base.components.molecule.input.SearchInputV2Data

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class PublicServicesSearchComposeVMTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var retryLastAction: WithRetryLastAction

    @Mock
    private lateinit var composeMapper: PublicServicesSearchComposeMapper

    @Mock
    private lateinit var helper: PublicServiceHelper

    private lateinit var errorHandling: StubErrorHandlerOnFlow
    private lateinit var viewModel: PublicServicesSearchComposeVM

    @Before
    fun before() {
        errorHandling = StubErrorHandlerOnFlow()
        viewModel = PublicServicesSearchComposeVM(
            dispatcher = TestDispatcherProvider(),
            errorHandling = errorHandling,
            retryLastAction = retryLastAction,
            composeMapper = composeMapper,
            helper = helper
        )
    }

    @Test
    fun `empty search`() = runTest {
        viewModel.doInit(testCategories().categories.toTypedArray())
        yield()
        viewModel.services.asFlow().test {
            viewModel.query.value = "weeeeee"
            Assert.assertEquals(listOf<PublicServiceView>(), awaitItem())
        }
    }

    @Test
    fun `service search`() = runTest {
        val generateMock = mock(SearchInputV2Data::class.java)
        `when`(generateMock.onChange(any())).thenReturn(mock(SearchInputV2Data::class.java))
        `when`(composeMapper.generateSearchInputMoleculeV2(any(), any())).thenReturn(generateMock)
        viewModel.doInit(testCategories().categories.toTypedArray())
        yield()
        viewModel.services.asFlow().test {
            viewModel.onUIAction(UIAction(UIActionKeysCompose.SEARCH_INPUT, "Воркс"))
            val service = testCategories().categories.firstNotNullOf { category ->
                category.publicServices.firstNotNullOf { service ->
                    if (service.code == "officeOfficialWorkspace") {
                        service.toDisplayService(category.name, category.code)
                    } else {
                        null
                    }
                }
            }
            Assert.assertEquals(listOf(service), awaitItem())
        }
    }

    @Test
    fun `service search empty`() = runTest {
        viewModel.doInit(testCategories().categories.toTypedArray())
        yield()
        viewModel.onUIAction(UIAction(UIActionKeysCompose.SEARCH_INPUT, "Воркс"))
        yield()
        viewModel.services.asFlow().test {
            viewModel.onUIAction(UIAction(UIActionKeysCompose.SEARCH_INPUT, " "))
            Assert.assertEquals(emptyList<PublicServiceView>(), awaitItem())
        }
    }

    @Test
    fun `service search null`() = runTest {
        viewModel.doInit(testCategories().categories.toTypedArray())
        yield()
        viewModel.onUIAction(UIAction(UIActionKeysCompose.SEARCH_INPUT, "Воркс"))
        yield()
        viewModel.services.asFlow().test {
            viewModel.onUIAction(UIAction(UIActionKeysCompose.SEARCH_INPUT, null))
            Assert.assertEquals(emptyList<PublicServiceView>(), awaitItem())
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
    fun `click on service`() = runTest {
        val code = "officeOfficialWorkspace"
        viewModel.doInit(testCategories().categories.toTypedArray())
        yield()
        viewModel.services.asFlow().test {
            viewModel.onUIAction(UIAction(UIActionKeysCompose.SEARCH_INPUT, "Воркс"))
            awaitItem()
            viewModel.navigation.test {
                viewModel.onUIAction(
                    UIAction(
                        actionKey = UIActionKeysCompose.LIST_ITEM_GROUP_ORG,
                        action = DataActionWrapper(""),
                        data = "qweqe"
                    )
                )
                expectNoEvents()
            }
            viewModel.navigation.test {
                viewModel.onUIAction(
                    UIAction(
                        actionKey = UIActionKeysCompose.LIST_ITEM_GROUP_ORG,
                        action = DataActionWrapper(""),
                        data = code
                    )
                )
                val service = (awaitItem() as NavigateToService).service
                Assert.assertEquals(code, service.code)
            }
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
        viewModel.navigation.test {
            viewModel.onUIAction(
                UIAction(UIActionKeysCompose.LIST_ITEM_CLICK)
            )
            expectNoEvents()
        }
    }
}
