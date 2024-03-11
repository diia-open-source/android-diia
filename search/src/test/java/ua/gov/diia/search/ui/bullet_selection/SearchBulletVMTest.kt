package ua.gov.diia.search.ui.bullet_selection

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
import ua.gov.diia.search.models.SearchableBullet
import ua.gov.diia.search.rules.MainDispatcherRule
import ua.gov.diia.search.util.TestSearchableBullet
import ua.gov.diia.search.util.awaitEvent

@RunWith(MockitoJUnitRunner::class)
class SearchBulletVMTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: SearchBulletVM

    @Before
    fun before() {
        viewModel = SearchBulletVM()
    }

    @Test
    fun initialization() = runTest {
        viewModel.setArgs(searchableBullets().toTypedArray(), "header", "title")
        Assert.assertEquals("header", viewModel.screenHeader.asFlow().first())
        Assert.assertEquals("title", viewModel.contentTitle.asFlow().first())
        Assert.assertEquals(searchableBullets(), viewModel.data.asFlow().first())
    }

    @Test
    fun `empty selection`() = runTest {
        viewModel.setArgs(searchableBullets().toTypedArray(), "header", "title")
        Assert.assertFalse(viewModel.enableActionButton.asFlow().first())
        viewModel.sendResult()
        Assert.assertNull(viewModel.setNavigationResult.value?.getContentIfNotHandled())
    }

    @Test
    fun `empty selection if selectedItemPosition is null`() = runTest {
        viewModel.setArgs(searchableBullets().toTypedArray(), "header", "title")
        Assert.assertFalse(viewModel.enableActionButton.asFlow().first())
        viewModel.selectedBulletId.value = null
        viewModel.sendResult()
        Assert.assertNull(viewModel.setNavigationResult.value?.getContentIfNotHandled())
    }

    @Test
    fun `empty selection if no data`() = runTest {
        viewModel.setArgs(arrayOf(), "header", "title")
        viewModel.selectedBulletId.value = 3
        viewModel.sendResult()
        Assert.assertNull(viewModel.setNavigationResult.value?.getContentIfNotHandled())
    }

    @Test
    fun `empty selection if index not in range`() = runTest {
        viewModel.setArgs(searchableBullets().toTypedArray(), "header", "title")
        viewModel.selectedBulletId.value = 5
        viewModel.sendResult()
        Assert.assertNull(viewModel.setNavigationResult.value?.getContentIfNotHandled())
    }

    @Test
    fun `bullet selection`() = runTest {
        viewModel.setArgs(searchableBullets().toTypedArray(), "header", "title")
        viewModel.selectedBulletId.value = 3
        Assert.assertTrue(viewModel.enableActionButton.asFlow().first())
        viewModel.sendResult()
        Assert.assertEquals(searchableBullets()[3], viewModel.setNavigationResult.awaitEvent())
    }

    private fun searchableBullets() = List<SearchableBullet>(4) { i ->
        TestSearchableBullet("TestBullet$i")
    }
}
