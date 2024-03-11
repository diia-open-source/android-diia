package ua.gov.diia.notifications.ui.fragments.notifications.compose

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.paging.PagingSource
import androidx.paging.PagingState
import io.mockk.clearMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import ua.gov.diia.notifications.MainDispatcherRule
import ua.gov.diia.notifications.models.notification.LoadingState
import ua.gov.diia.notifications.models.notification.pull.PullNotification
import ua.gov.diia.notifications.models.notification.pull.PullNotificationMessage
import ua.gov.diia.notifications.models.notification.pull.PullNotificationSyncAction
import ua.gov.diia.notifications.store.datasource.notifications.NotificationDataRepository
import ua.gov.diia.notifications.ui.fragments.home.notifications.compose.NotificationPagingSourceCompose
import ua.gov.diia.notifications.ui.fragments.home.notifications.compose.NotificationsActionKey
import ua.gov.diia.notifications.ui.fragments.home.notifications.compose.NotificationsMapperCompose
import ua.gov.diia.notifications.ui.fragments.home.notifications.compose.NotificationsMapperComposeImpl
import ua.gov.diia.ui_base.components.molecule.message.MessageMoleculeData

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class NotificationPagingSourceComposeTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    lateinit var notificationPagingSourceCompose: NotificationPagingSourceCompose

    lateinit var notificationDataSource: NotificationDataRepository
    lateinit var composeMapper: NotificationsMapperCompose

    @Before
    fun setUp() {
        notificationDataSource = mockk(relaxed = true)
        composeMapper = NotificationsMapperComposeImpl()
        notificationPagingSourceCompose = NotificationPagingSourceCompose(notificationDataSource, composeMapper) {
        }
    }

    @Test
    fun `test getRefreshKey call getTotalSize from data source`() {
        runBlocking {
            val pair = mockk<PagingSource.LoadResult.Page<Int, MessageMoleculeData>>(relaxed = true)
            every { pair.prevKey } returns 0
            val state = mockk<PagingState<Int, MessageMoleculeData>>(relaxed = true)
            coEvery { state.closestPageToPosition(any()) } returns pair
            coEvery { notificationDataSource.getTotalSize() } returns 1
            notificationPagingSourceCompose.getRefreshKey(state)

            coVerify(exactly = 1) { notificationDataSource.getTotalSize() }
        }
    }

    @Test
    fun `test getRefreshKey find anchor and apply it to current state`() {
        runBlocking {
            val prevKey = 0
            val pair = mockk<PagingSource.LoadResult.Page<Int, MessageMoleculeData>>(relaxed = true)
            every { pair.prevKey } returns prevKey
            val state = mockk<PagingState<Int, MessageMoleculeData>>(relaxed = true)
            every { state.anchorPosition } returns 2
            coEvery { state.closestPageToPosition(any()) } returns pair
            coEvery { notificationDataSource.getTotalSize() } returns 1
            val result = notificationPagingSourceCompose.getRefreshKey(state)

            coVerify(exactly = 1) { state.closestPageToPosition(1) }
            assertEquals(prevKey, result)
        }
    }

    @Test
    fun `test getRefreshKey return null if closestPageToPosition returns null`() {
        runBlocking {
            val state = mockk<PagingState<Int, MessageMoleculeData>>(relaxed = true)
            every { state.anchorPosition } returns 2
            coEvery { state.closestPageToPosition(any()) } returns null
            coEvery { notificationDataSource.getTotalSize() } returns 1
            val result = notificationPagingSourceCompose.getRefreshKey(state)

            assertEquals(null, result)
        }
    }

    @Test
    fun `test getRefreshKey return null if state anchorPosition is null`() {
        runBlocking {
            val state = mockk<PagingState<Int, MessageMoleculeData>>(relaxed = true)
            every { state.anchorPosition } returns null
            coEvery { notificationDataSource.getTotalSize() } returns 1
            val result = notificationPagingSourceCompose.getRefreshKey(state)

            assertEquals(null, result)
        }
    }

    @Test
    fun `test check load for first page loading`() {
        val loadState = mutableListOf<LoadingState>()

        notificationPagingSourceCompose = NotificationPagingSourceCompose(notificationDataSource, composeMapper) {
            loadState.add(it)
        }
        runBlocking {
            val params = spyk(mockk<PagingSource.LoadParams<Int>>(relaxed = true))
            coEvery { params.key } returns null

            notificationPagingSourceCompose.load(params)

            assertEquals(LoadingState.FIRST_PAGE_LOADING, loadState[0])
            assertEquals(LoadingState.NOT_LOADING, loadState[1])
            assertEquals(LoadingState.NOT_LOADING, loadState[2])
        }
    }

    @Test
    fun `test check load for next page loading`() {

        val loadState = mutableListOf<LoadingState>()

        notificationPagingSourceCompose = NotificationPagingSourceCompose(notificationDataSource, composeMapper) {
            loadState.add(it)
        }
        runBlocking {
            loadState.clear()
            val params = spyk(mockk<PagingSource.LoadParams<Int>>(relaxed = true))
            coEvery { params.key } returns 1

            notificationPagingSourceCompose.load(params)

            assertEquals(LoadingState.ADDITIONAL_PAGE_LOADING, loadState[0])
            assertEquals(LoadingState.NOT_LOADING, loadState[1])
            assertEquals(LoadingState.NOT_LOADING, loadState[2])
        }
    }

    @Test
    fun `test load data from notificationDataSource with skip value as null`() {
        runBlocking {
            val pageSize = 50
            val params = spyk(mockk<PagingSource.LoadParams<Int>>(relaxed = true))
            coEvery { params.key } returns null
            coEvery { params.loadSize } returns pageSize

            clearMocks(notificationDataSource)
            notificationPagingSourceCompose.load(params)

            coVerify(exactly = 1) { notificationDataSource.loadDataFromNetwork(0, pageSize, any()) }
            coVerify(exactly = 2) { notificationDataSource.getTotalSize() }
            coVerify(exactly = 1) { notificationDataSource.getPage(0, pageSize) }
        }
    }
    @Test
    fun `test load data from notificationDataSource with skip value as 2`() {
        runBlocking {
            val pageSize = 50
            val params = spyk(mockk<PagingSource.LoadParams<Int>>(relaxed = true))
            coEvery { params.key } returns 2
            coEvery { params.loadSize } returns pageSize

            notificationPagingSourceCompose.load(params)

            coVerify(exactly = 1) { notificationDataSource.loadDataFromNetwork(2, pageSize, any()) }
            coVerify(exactly = 2) { notificationDataSource.getTotalSize() }
            coVerify(exactly = 1) { notificationDataSource.getPage(2, pageSize) }
        }
    }

    @Test
    fun `test load gathering results`() {
        runBlocking {
            val pageSize = 50
            val totalSize = 150
            val skip = 50
            val params = spyk(mockk<PagingSource.LoadParams<Int>>(relaxed = true))
            coEvery { params.key } returns skip
            coEvery { params.loadSize } returns pageSize
            coEvery { notificationDataSource.getTotalSize() } returns totalSize

            val result = notificationPagingSourceCompose.load(params)

            assertTrue(result is PagingSource.LoadResult.Page<Int, MessageMoleculeData>)
            val page = result as PagingSource.LoadResult.Page<Int, MessageMoleculeData>
            assertEquals(skip - pageSize, page.prevKey)
            assertEquals(skip + pageSize, page.nextKey)
            assertTrue(page.data.isEmpty())
            assertEquals(skip, page.itemsBefore)
            assertEquals(totalSize - skip + pageSize, page.itemsAfter)
        }
    }

    @Test
    fun `test load mapping results`() {
        runBlocking {
            val pageSize = 50
            val totalSize = 150
            val skip = 50
            val params = spyk(mockk<PagingSource.LoadParams<Int>>(relaxed = true))
            coEvery { params.key } returns skip
            coEvery { params.loadSize } returns pageSize
            coEvery { notificationDataSource.getTotalSize() } returns totalSize
            val notificationsList = mutableListOf<PullNotification>()
            val notificationOne = PullNotification("notId", "creationDate", false, PullNotificationMessage("icon", "notTitle", "notShortTitle", null), PullNotificationSyncAction.NONE)
            notificationsList.add(notificationOne)
            coEvery { notificationDataSource.getPage(any(), any()) } returns notificationsList

            val result = notificationPagingSourceCompose.load(params)

            assertTrue(result is PagingSource.LoadResult.Page<Int, MessageMoleculeData>)
            val page = result as PagingSource.LoadResult.Page<Int, MessageMoleculeData>
            val pageItemOne = page.data[0]

            assertEquals(NotificationsActionKey.SELECT_NOTIFICATION, pageItemOne.actionKey)
            assertEquals(notificationOne.pullNotificationMessage!!.title, pageItemOne.title)
            assertEquals(notificationOne.pullNotificationMessage!!.shortText, pageItemOne.shortText)
            assertEquals(notificationOne.creationDate, pageItemOne.creationDate)
            assertEquals(notificationOne.isRead, pageItemOne.isRead)
            assertEquals(notificationOne.notificationId, pageItemOne.notificationId)
            assertEquals(notificationOne.notificationId, pageItemOne.id)
            assertEquals(notificationOne.syncAction, pageItemOne.syncAction)
        }
    }

    @Test
    fun `test load removing deleted notification`() {
        runBlocking {
            val pageSize = 50
            val totalSize = 150
            val skip = 50
            val params = spyk(mockk<PagingSource.LoadParams<Int>>(relaxed = true))
            coEvery { params.key } returns skip
            coEvery { params.loadSize } returns pageSize
            coEvery { notificationDataSource.getTotalSize() } returns totalSize
            val notificationOne = PullNotification("notId", "creationDate", false, PullNotificationMessage("icon", "notTitle", "notShortTitle", null), PullNotificationSyncAction.NONE)
            val notificationTwo = PullNotification("notId", "creationDate", false, PullNotificationMessage("icon", "notTitle", "notShortTitle", null),
                PullNotificationSyncAction.REMOVE)
            val notificationsList = mutableListOf(notificationOne, notificationTwo)

            coEvery { notificationDataSource.getPage(any(), any()) } returns notificationsList

            val result = notificationPagingSourceCompose.load(params)

            assertTrue(result is PagingSource.LoadResult.Page<Int, MessageMoleculeData>)
            val page = result as PagingSource.LoadResult.Page<Int, MessageMoleculeData>

            assertEquals(1, page.data.size)

            val pageItemOne = page.data[0]

            assertEquals(notificationOne.notificationId, pageItemOne.notificationId)
            assertEquals(notificationOne.notificationId, pageItemOne.id)
        }
    }

    @Test
    fun `test load catch error`() {
        runBlocking {
            val params = mockk<PagingSource.LoadParams<Int>>(relaxed = true)
            val error = RuntimeException("Error")

            coEvery { notificationDataSource.getPage(any(), any()) } throws error

            val result = notificationPagingSourceCompose.load(params)

            assertTrue(result is PagingSource.LoadResult.Error)
            val errorPage = result as PagingSource.LoadResult.Error
            assertEquals(error, errorPage.throwable)
        }
    }
}