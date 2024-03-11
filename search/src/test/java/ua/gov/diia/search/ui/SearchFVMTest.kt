package ua.gov.diia.search.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.asFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import ua.gov.diia.search.models.SearchableItem
import ua.gov.diia.search.rules.MainDispatcherRule
import ua.gov.diia.search.util.TestDispatcherProvider
import ua.gov.diia.search.util.TestSearchableItem
import ua.gov.diia.search.util.awaitEvent

@RunWith(MockitoJUnitRunner::class)
class SearchFVMTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: SearchFVM

    @Before
    fun before() {
        viewModel = SearchFVM(
            dispatcherProvider = TestDispatcherProvider()
        )
    }

    @Test
    fun initialization() = runTest {
        viewModel.doInit(searchableItems(), "result_key")
        Assert.assertEquals(searchableItems(), viewModel.displayItemList.asFlow().first())
    }

    @Test
    fun `search results`() = runTest {
        viewModel.doInit(searchableItems(), "result_key")
        viewModel.filter("2")
        Assert.assertEquals(
            listOf(searchableItems()[1]),
            viewModel.displayItemList.asFlow().first()
        )
    }

    @Test
    fun `empty search results`() = runTest {
        viewModel.doInit(searchableItems(), "result_key")
        viewModel.filter("5")
        Assert.assertEquals(
            listOf<SearchableItem>(),
            viewModel.displayItemList.asFlow().first()
        )
    }

    @Test
    fun `search with empty query`() = runTest {
        val list = searchableItems()

        viewModel.doInit(list, "result_key")
        viewModel.filter(" ")
        Assert.assertEquals(
            list,
            viewModel.displayItemList.asFlow().first()
        )
    }

    @Test
    fun `invalid search query`() = runTest {
        viewModel.doInit(searchableItems(), "result_key")
        viewModel.filter("    ")
        Assert.assertEquals(
            searchableItems(),
            viewModel.displayItemList.asFlow().first()
        )
    }

    @Test
    fun `find result`() = runTest {
        viewModel.doInit(searchableItems(), "result_key")
        viewModel.findResult("test item 4")
        val result = viewModel.setResult.awaitEvent()
        Assert.assertEquals(searchableItems()[3], result.item)
        Assert.assertEquals("result_key", result.key)
    }

    @Test
    fun `find result not work before initializing of key`() = runTest {
        viewModel.findResult("test item 4")
        val result = viewModel.setResult.value
        Assert.assertEquals(null, result)
    }

    @Test
    fun `find result no matching`() = runTest {
        viewModel.doInit(searchableItems(), "result_key")
        viewModel.findResult("ljkljljlk")
        val result = viewModel.setResult.awaitEvent()
        Assert.assertEquals(null, result.item)
        Assert.assertEquals("result_key", result.key)
    }

    private fun searchableItems() = listOf<SearchableItem>(
        TestSearchableItem("TestItem1", "test item 1"),
        TestSearchableItem("TestItem2", "test item 2"),
        TestSearchableItem("TestItem3", "test item 3"),
        TestSearchableItem("TestItem4", "test item 4")
    )
}
