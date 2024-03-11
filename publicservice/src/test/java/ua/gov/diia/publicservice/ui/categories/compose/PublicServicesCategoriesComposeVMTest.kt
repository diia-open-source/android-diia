package ua.gov.diia.publicservice.ui.categories.compose

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.asFlow
import app.cash.turbine.test
import com.nhaarman.mockitokotlin2.doAnswer
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.yield
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import ua.gov.diia.core.data.repository.DataRepository
import ua.gov.diia.core.util.delegation.WithRetryLastAction
import ua.gov.diia.publicservice.helper.PublicServiceHelper
import ua.gov.diia.publicservice.models.CategoryStatus
import ua.gov.diia.publicservice.models.PublicServicesCategories
import ua.gov.diia.publicservice.rules.MainDispatcherRule
import ua.gov.diia.publicservice.util.StubErrorHandlerOnFlow
import ua.gov.diia.publicservice.util.TestDispatcherProvider
import ua.gov.diia.publicservice.util.testCategories
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.event.UIActionKeysCompose
import java.io.IOException

@RunWith(MockitoJUnitRunner::class)
class PublicServicesCategoriesComposeVMTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var repository: DataRepository<PublicServicesCategories?>

    @Mock
    private lateinit var helper: PublicServiceHelper

    @Mock
    private lateinit var retryLastAction: WithRetryLastAction

    @Mock
    private lateinit var composeMapper: PublicServicesCategoriesTabMapper

    private lateinit var errorHandling: StubErrorHandlerOnFlow
    private lateinit var viewModel: PublicServicesCategoriesComposeVM

    @Before
    fun before() {
        errorHandling = StubErrorHandlerOnFlow()
        viewModel = PublicServicesCategoriesComposeVM(
            repository = repository,
            helper = helper,
            dispatcherProvider = TestDispatcherProvider(),
            retryLastAction = retryLastAction,
            errorHandling = errorHandling,
            composeMapper = composeMapper
        )
        runBlocking { whenever(repository.load()).doReturn(testCategories()) }
    }

    @Test
    fun tabs() = runTest {
        viewModel.doInit(null)
        val tabs = viewModel.tabs.asFlow().first()
        Assert.assertEquals(2, tabs.size)
        Assert.assertTrue(tabs[0].isChecked)
        Assert.assertFalse(tabs[1].isChecked)
    }

    @Test
    fun `categories list`() = runTest {
        viewModel.doInit(null)
        viewModel.contentLoaded.first { it.second }
        val firstTab = testCategories().tabs.first().code
        Assert.assertEquals(
            testCategories().categories.filter { it.tabCode == firstTab },
            viewModel.publicServices.asFlow().first()
        )
        val secondTab = testCategories().tabs[1].code
        viewModel.onUIAction(
            UIAction(UIActionKeysCompose.CHIP_TABS_MOLECULE, secondTab)
        )
        Assert.assertEquals(
            testCategories().categories.filter { it.tabCode == secondTab },
            viewModel.publicServices.asFlow().first()
        )
    }

    @Test
    fun reloading() = runTest {
        viewModel.invalidateDataSource()
        yield()
        verify(repository).load()
    }

    @Test
    fun `navigate to search`() = runTest {
        viewModel.navigation.test {
            viewModel.onUIAction(UIAction(UIActionKeysCompose.SEARCH_INPUT))
            Assert.assertTrue(
                awaitItem() is PublicServicesCategoriesNavigation.NavigateToServiceSearch
            )
        }
    }

    @Test
    fun `navigate to category`() = runTest {
        viewModel.navigation.test {
            viewModel.onUIAction(UIAction(UIActionKeysCompose.PS_ITEM_CLICK))
            expectNoEvents()
        }
        viewModel.navigation.test {
            viewModel.onUIAction(UIAction(UIActionKeysCompose.PS_ITEM_CLICK, "carServices"))
            expectNoEvents()
        }

        viewModel.doInit(null)
        viewModel.publicServices.asFlow().first()
        viewModel.onUIAction(UIAction(UIActionKeysCompose.CHIP_TABS_MOLECULE, "citizen"))
        viewModel.navigation.test {
            viewModel.onUIAction(UIAction(UIActionKeysCompose.PS_ITEM_CLICK, "carServices"))
            val event = awaitItem() as PublicServicesCategoriesNavigation.NavigateToCategory
            Assert.assertEquals("carServices", event.category.code)
        }
    }

    @Test
    fun `navigate to service`() = runTest {
        viewModel.doInit(null)
        viewModel.publicServices.asFlow().first()
        viewModel.onUIAction(UIAction(UIActionKeysCompose.CHIP_TABS_MOLECULE, "office"))
        viewModel.navigation.test {
            viewModel.onUIAction(UIAction(UIActionKeysCompose.PS_ITEM_CLICK, "officeWorkspace"))
            val event = awaitItem() as PublicServicesCategoriesNavigation.NavigateToService
            Assert.assertEquals("officeOfficialWorkspace", event.service.code)
        }
    }

    @Test
    fun `open category immediate`() = runTest {
        viewModel.navigation.test {
            viewModel.doInit("carServices")
            val event = awaitItem() as PublicServicesCategoriesNavigation.NavigateToCategory
            Assert.assertEquals("carServices", event.category.code)
        }
    }

    @Test
    fun `open category immediate 2`() = runTest {
        val tabCode = "citizen"
        val response = PublicServicesCategories(
            categories = testCategories().categories.filter { x -> x.tabCode == tabCode },
            tabs = testCategories().tabs.filter { x -> x.code == tabCode }
        )
        whenever(repository.load()).doReturn(response)
        viewModel.doInit("carServices")
        advanceUntilIdle()
        val createdTopBar = viewModel.topBarData[0]
        viewModel.doInit("carServices")
        advanceUntilIdle()
        Assert.assertEquals(createdTopBar, viewModel.topBarData[0])
    }

    @Test
    fun `navigate to disabled service`() = runTest {
        whenever(repository.load()) doReturn testCategories().run {
            copy(categories = categories.map {
                it.copy(publicServices = it.publicServices.map { s ->
                    s.copy(status = CategoryStatus.inactive)
                })
            })
        }

        viewModel.doInit(null)
        viewModel.publicServices.asFlow().first()
        viewModel.onUIAction(UIAction(UIActionKeysCompose.CHIP_TABS_MOLECULE, "office"))
        viewModel.navigation.test {
            viewModel.onUIAction(UIAction(UIActionKeysCompose.PS_ITEM_CLICK, "officeWorkspace"))
            expectNoEvents()
        }
    }

    @Test
    fun `open disabled category`() = runTest {
        whenever(repository.load()) doReturn testCategories().run {
            copy(categories = categories.map {
                it.copy(status = CategoryStatus.inactive)
            })
        }

        viewModel.doInit(null)
        viewModel.publicServices.asFlow().first()
        viewModel.onUIAction(UIAction(UIActionKeysCompose.CHIP_TABS_MOLECULE, "office"))
        viewModel.navigation.test {
            viewModel.onUIAction(UIAction(UIActionKeysCompose.PS_ITEM_CLICK, "officeWorkspace"))
            expectNoEvents()
        }
    }

    @Test
    fun `open empty category`() = runTest {
        whenever(repository.load()) doReturn testCategories().run {
            copy(categories = categories.map {
                it.copy(publicServices = emptyList())
            })
        }

        viewModel.doInit(null)
        viewModel.publicServices.asFlow().first()
        viewModel.onUIAction(UIAction(UIActionKeysCompose.CHIP_TABS_MOLECULE, "office"))
        viewModel.navigation.test {
            viewModel.onUIAction(UIAction(UIActionKeysCompose.PS_ITEM_CLICK, "officeWorkspace"))
            expectNoEvents()
        }
    }

    @Test
    fun `open nonexistent category`() = runTest {
        viewModel.doInit(null)
        viewModel.publicServices.asFlow().first()
        viewModel.onUIAction(UIAction(UIActionKeysCompose.CHIP_TABS_MOLECULE, "office"))
        viewModel.navigation.test {
            viewModel.onUIAction(UIAction(UIActionKeysCompose.PS_ITEM_CLICK, "fwokdo"))
            expectNoEvents()
        }
    }

    @Test
    fun `categories loading error`() = runTest {
        whenever(repository.load()).doAnswer { throw IOException("Test") }
        viewModel.doInit(null)
        val e = errorHandling.lastError()
        Assert.assertTrue(e is IOException)
        Assert.assertEquals("Test", e.message)
    }

    @Test
    fun `one tab`() = runTest {
        val tabCode = "citizen"
        val response = PublicServicesCategories(
            categories = testCategories().categories.filter { x -> x.tabCode == tabCode },
            tabs = testCategories().tabs.filter { x -> x.code == tabCode }
        )
        whenever(repository.load()).doReturn(response)

        viewModel.doInit(null)
        viewModel.contentLoaded.first { it.second }
        val tabs = viewModel.tabs.asFlow().first()
        Assert.assertEquals(1, tabs.size)
        Assert.assertEquals(tabCode, tabs.singleOrNull()?.code)
    }

    @Test
    fun `no tabs`() = runTest {
        val response = PublicServicesCategories(
            categories = emptyList(),
            tabs = emptyList()
        )
        whenever(repository.load()).doReturn(response)

        viewModel.doInit(null)
        viewModel.contentLoaded.first { it.second }
        viewModel.onUIAction(UIAction(UIActionKeysCompose.CHIP_TABS_MOLECULE))
        val tabs = viewModel.tabs.asFlow().first()
        Assert.assertEquals(0, tabs.size)
        Assert.assertNull(tabs.singleOrNull()?.code)
    }
}
