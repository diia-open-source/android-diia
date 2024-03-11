package ua.gov.diia.notifications.store.datasource

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import app.cash.turbine.test
import com.nhaarman.mockitokotlin2.mock
import io.mockk.clearMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
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
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner
import ua.gov.diia.core.models.notification.push.PushAction
import ua.gov.diia.core.util.delegation.WithCrashlytics
import ua.gov.diia.core.util.event.UiDataEvent
import ua.gov.diia.notifications.MainDispatcherRule
import ua.gov.diia.notifications.models.notification.pull.PullNotification
import ua.gov.diia.notifications.models.notification.pull.PullNotificationMessage
import ua.gov.diia.notifications.models.notification.pull.PullNotificationSyncAction
import ua.gov.diia.notifications.models.notification.pull.PullNotificationsResponse
import ua.gov.diia.notifications.models.notification.pull.PullNotificationsToModify
import ua.gov.diia.notifications.models.notification.pull.UpdatePullNotificationResponse
import ua.gov.diia.notifications.store.datasource.notifications.KeyValueNotificationDataSource
import ua.gov.diia.notifications.store.datasource.notifications.NetworkNotificationDataSource
import ua.gov.diia.notifications.store.datasource.notifications.NotificationDataRepositoryImpl
import ua.gov.diia.notifications.util.notification.manager.DiiaNotificationManager

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class NotificationDataRepositoryImplTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    val notificationId = "notid"

    lateinit var keyValueSource: KeyValueNotificationDataSource

    @Mock
    lateinit var diiaNotificationManager: DiiaNotificationManager

    lateinit var networkSource: NetworkNotificationDataSource

    lateinit var actionNotificationRead: MutableLiveData<UiDataEvent<String>>

    @Mock
    lateinit var withCrashlytics: WithCrashlytics

    lateinit var notificationDataRepositoryImpl: NotificationDataRepositoryImpl

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        networkSource = mockk()
        keyValueSource = mockk(relaxed = true)
    }

    fun initRepo(scope: CoroutineScope) {
        actionNotificationRead = MutableLiveData<UiDataEvent<String>>()
        notificationDataRepositoryImpl = NotificationDataRepositoryImpl(
            scope,
            keyValueSource,
            diiaNotificationManager,
            networkSource,
            actionNotificationRead,
            withCrashlytics
        )
    }

    suspend fun mainInitialization(scope: CoroutineScope): MutableList<PullNotification> {
        initRepo(scope)

        val data = mutableListOf<PullNotification>()
        data.add(
            PullNotification(
                notificationId,
                "createdate",
                false,
                null,
                PullNotificationSyncAction.NONE
            )
        )

        coEvery { keyValueSource.fetchData() } returns data
        coEvery { keyValueSource.fetchUnreadCount() } returns 0
        return data
    }

    @Test
    fun `test invalidate`() {
        runTest {
            val data = mainInitialization(this)

            val unreadedCound = 5
            coEvery { keyValueSource.fetchUnreadCount()} returns unreadedCound

            notificationDataRepositoryImpl.invalidate()
            advanceUntilIdle()

            coVerify(exactly = 1) { keyValueSource.fetchData() }
            coVerify(exactly = 1) { keyValueSource.fetchUnreadCount() }

            notificationDataRepositoryImpl.data.test {
                val item = awaitItem()
                assertEquals(true, item.isSuccessful)
                assertEquals(null, item.exception)
                assertEquals(data, item.data)
            }

            notificationDataRepositoryImpl.unreadCount.test {
                val item = awaitItem()
                Assert.assertEquals(unreadedCound, item)
            }

            notificationDataRepositoryImpl.isDataLoading.test {
                val item = awaitItem()
                Assert.assertEquals(false, item)
            }
        }
    }

    @Test
    fun `test invalidate sync with read data`() {
        runTest {
            initRepo(this)

            val unreadedCound = 5
            val data = mutableListOf<PullNotification>()
            data.add(
                PullNotification(
                    "notid_read", "createdate", false,
                    null, PullNotificationSyncAction.READ
                )
            )

            coEvery { keyValueSource.fetchData() } returns data
            coEvery { keyValueSource.fetchUnreadCount() } returns unreadedCound
            coEvery { networkSource.markNotificationsAsRead(any()) } returns UpdatePullNotificationResponse(10)

            notificationDataRepositoryImpl.invalidate()
            advanceUntilIdle()


            coVerify(exactly = 1) { networkSource.markNotificationsAsRead(any()) }

            coVerify(exactly = 1) { keyValueSource.updateUnreadCount(10) }
            coVerify(exactly = 1) { keyValueSource.saveDataToStore(data) }
            notificationDataRepositoryImpl.unreadCount.test {
                val item = awaitItem()
                Assert.assertEquals(10, item)
            }
        }
    }

    @Test
    fun `test invalidate sync with remove data`() {
        runTest {
            initRepo(this)

            val unreadedCound = 5
            val data = mutableListOf<PullNotification>()
            data.add(
                PullNotification(
                    "notid_remove", "createdate", false,
                    null, PullNotificationSyncAction.REMOVE
                )
            )

            coEvery { keyValueSource.fetchData()} returns data
            coEvery { keyValueSource.fetchUnreadCount() } returns unreadedCound
            coEvery { networkSource.markNotificationsAsRead(any()) } returns UpdatePullNotificationResponse(10)


            notificationDataRepositoryImpl.invalidate()
            advanceUntilIdle()


            coVerify(exactly = 1) { networkSource.markNotificationsAsRead(any()) }
            coVerify(exactly = 1) { keyValueSource.updateUnreadCount(10) }

            data.removeFirst()
            coVerify(exactly = 1) { keyValueSource.saveDataToStore(data) }
            notificationDataRepositoryImpl.unreadCount.test {
                val item = awaitItem()
                Assert.assertEquals(10, item)
            }
        }
    }

    @Test
    fun `test invalidate sync with read and remove data`() {
        runTest {
            initRepo(this)

            val unreadedCound = 5
            val data = mutableListOf<PullNotification>()
            data.add(
                PullNotification(
                    "notid_remove", "createdate", false,
                    null, PullNotificationSyncAction.REMOVE
                )
            )
            data.add(
                PullNotification(
                    "notid_read", "createdate", false,
                    null, PullNotificationSyncAction.READ
                )
            )

            coEvery { keyValueSource.fetchData() } returns data
            coEvery { keyValueSource.fetchUnreadCount() } returns unreadedCound
            coEvery { networkSource.markNotificationsAsRead(any()) } returns UpdatePullNotificationResponse(10)

            notificationDataRepositoryImpl.invalidate()
            advanceUntilIdle()


            coVerify(exactly = 2) { networkSource.markNotificationsAsRead(any()) }
            coVerify(exactly = 1) { keyValueSource.updateUnreadCount(10) }

            data.removeFirst()
            coVerify(exactly = 1) { keyValueSource.saveDataToStore(data) }
            notificationDataRepositoryImpl.unreadCount.test {
                val item = awaitItem()
                Assert.assertEquals(10, item)
            }
        }
    }

    @Test
    fun `test invalidate sync with read data throw error and trigger crashlytics`() {
        runTest {
            initRepo(this)

            val exception = RuntimeException("read error")
            val unreadedCound = 5
            val data = mutableListOf<PullNotification>()
            data.add(
                PullNotification(
                    "notid_read", "createdate", false,
                    null, PullNotificationSyncAction.READ
                )
            )

            coEvery { keyValueSource.fetchData()} returns data
            coEvery { keyValueSource.fetchUnreadCount()} returns unreadedCound
            coEvery { networkSource.markNotificationsAsRead(any())  } throws exception

            notificationDataRepositoryImpl.invalidate()
            advanceUntilIdle()


            Mockito.verify(withCrashlytics, Mockito.times(1))
                .sendNonFatalError(exception)
        }
    }


    @Test
    fun `test invalidate sync with remove data throw error and trigger crashlytics`() {
        runTest {
            initRepo(this)

            val exception = RuntimeException("remove error")
            val unreadedCound = 5
            val data = mutableListOf<PullNotification>()
            data.add(
                PullNotification(
                    "notid_remove", "createdate", false,
                    null, PullNotificationSyncAction.REMOVE
                )
            )

            coEvery { keyValueSource.fetchData() } returns data
            coEvery { keyValueSource.fetchUnreadCount() } returns unreadedCound
            coEvery { networkSource.markNotificationsAsRead(any()) } throws exception

            notificationDataRepositoryImpl.invalidate()
            advanceUntilIdle()

            Mockito.verify(withCrashlytics, Mockito.times(1))
                .sendNonFatalError(exception)
        }
    }

    @Test
    fun `test getPullNotificationById return data from flow`() {
        runTest {
            val data = mainInitialization(this)

            notificationDataRepositoryImpl.invalidate()
            advanceUntilIdle()
            assertEquals(
                data[0],
                notificationDataRepositoryImpl.getPullNotificationById(notificationId)
            )
            assertEquals(
                null,
                notificationDataRepositoryImpl.getPullNotificationById("someid")
            )
        }
    }

    @Test
    fun `test getPullNotificationById return null if data is empty`() {
        runTest {
            initRepo(this)

            assertEquals(
                null,
                notificationDataRepositoryImpl.getPullNotificationById(notificationId)
            )
        }
    }

    @Test
    fun `test removeNotification trigger sendNonFatalError if exception appears`() {
        runTest {
            val exception = RuntimeException("remove error")
            mainInitialization(this)

            coEvery { networkSource.deleteNotifications(any()) } throws exception

            notificationDataRepositoryImpl.invalidate()
            advanceUntilIdle()
            notificationDataRepositoryImpl.removeNotification(notificationId)

            Mockito.verify(withCrashlytics, Mockito.times(1))
                .sendNonFatalError(exception)
        }
    }

    @Test
    fun `test removeNotification call deleteNotifications method in network source`() {
        runTest {
            mainInitialization(this)

            notificationDataRepositoryImpl.invalidate()
            advanceUntilIdle()
            notificationDataRepositoryImpl.removeNotification("notificationId")

            coVerify (exactly = 1) { networkSource.deleteNotifications(any()) }
        }
    }

    @Test
    fun `test removeNotification mark notification as read that is not in data flow`() {
        runTest {
            mainInitialization(this)
            val updatePullNotificationResponse: UpdatePullNotificationResponse = mock()
            val unread = 20
            Mockito.`when`(updatePullNotificationResponse.unread).thenReturn(unread)
            coEvery {
                networkSource.markNotificationsAsRead(
                    PullNotificationsToModify(
                        mutableListOf<String>("notificationId")
                    )
                )
            } returns updatePullNotificationResponse

            notificationDataRepositoryImpl.invalidate()
            advanceUntilIdle()
            notificationDataRepositoryImpl.removeNotification("notificationId")

            coVerify(exactly = 1) { networkSource.markNotificationsAsRead(PullNotificationsToModify(mutableListOf<String>("notificationId"))) }
            coVerify(exactly = 1) { networkSource.deleteNotifications(any()) }
            notificationDataRepositoryImpl.unreadCount.test {
                Assert.assertEquals(unread, awaitItem())
            }
        }
    }

    @Test
    fun `test removeNotification clear notification if data stores in data`() {
        runTest {
            initRepo(this)

            val notificationId = "notid"
            val data = mutableListOf<PullNotification>()
            data.add(
                PullNotification(
                    notificationId,
                    "createdate",
                    false,
                    null,
                    PullNotificationSyncAction.READ
                )
            )

            coEvery { keyValueSource.fetchData() } returns data
            coEvery { keyValueSource.fetchUnreadCount() } returns 0

            notificationDataRepositoryImpl.invalidate()
            advanceUntilIdle()
            notificationDataRepositoryImpl.removeNotification(notificationId)

            Mockito.verify(diiaNotificationManager, Mockito.times(1))
                .clearNotification(notificationId)
        }
    }

    @Test
    fun `test removeNotification has conflict in syncAction and call syncWithRemote`() {
        runTest {
            mainInitialization(this)
            notificationDataRepositoryImpl.invalidate()
            advanceUntilIdle()
            notificationDataRepositoryImpl.removeNotification(notificationId)

            coVerify(exactly = 1) {networkSource.markNotificationsAsRead(PullNotificationsToModify(mutableListOf(notificationId))) }

        }
    }

    @Test
    fun `test markNotificationAsRead call updateMessageSyncStatus with read status`() {
        runTest {
            mainInitialization(this)

            notificationDataRepositoryImpl.invalidate()
            advanceUntilIdle()
            notificationDataRepositoryImpl.markNotificationAsRead(notificationId)

            coVerify(exactly = 1) { networkSource.markNotificationsAsRead(PullNotificationsToModify(mutableListOf(notificationId))) }
            Assert.assertEquals(notificationId, actionNotificationRead.value!!.peekContent())
        }
    }

    @Test
    fun `test markNotificationAsRead call network if notification is not loaded yet`() {
        runTest {
            initRepo(this)

            notificationDataRepositoryImpl.markNotificationAsRead(notificationId)

            coVerify(exactly = 1) { networkSource.markNotificationsAsRead(PullNotificationsToModify(listOf(notificationId))) }
        }
    }

    @Test
    fun `test updateWithLocal save data to store`() {
        runTest {
            val data = mainInitialization(this)
            notificationDataRepositoryImpl.invalidate()
            advanceUntilIdle()
            clearMocks(keyValueSource)

            notificationDataRepositoryImpl.updateWithLocal(data)

            coVerify(exactly = 1) { keyValueSource.saveDataToStore(data) }
        }
    }

    @Test
    fun `test updateWithLocal emits new data in data flow`() {
        runTest {
            val data = mainInitialization(this)
            notificationDataRepositoryImpl.invalidate()
            advanceUntilIdle()
            clearMocks(keyValueSource)

            val deleteData = mutableListOf<PullNotification>()
            deleteData.add(
                PullNotification(
                    "notificationId",
                    "createdate",
                    false,
                    null,
                    PullNotificationSyncAction.NONE
                )
            )
            notificationDataRepositoryImpl.updateWithLocal(deleteData)

            notificationDataRepositoryImpl.data.test {
                Assert.assertEquals(data + deleteData[0], awaitItem().data)
            }
        }
    }

    @Test
    fun `test updateWithLocal emits data with changed read status`() {
        runTest {
            val data = mainInitialization(this)
            notificationDataRepositoryImpl.invalidate()
            advanceUntilIdle()
            clearMocks(keyValueSource)

            val deleteData = mutableListOf<PullNotification>()
            deleteData.add(
                PullNotification(
                    notificationId,
                    "createdate",
                    true,
                    null,
                    PullNotificationSyncAction.NONE
                )
            )
            notificationDataRepositoryImpl.updateWithLocal(deleteData)

            notificationDataRepositoryImpl.data.test {
                Assert.assertEquals(true, awaitItem().data!![0].isRead)
            }
        }
    }

    @Test
    fun `test appendItems to start`() {
        runTest {
            val data = mainInitialization(this)
            notificationDataRepositoryImpl.invalidate()
            advanceUntilIdle()
            clearMocks(keyValueSource)

            val addData = mutableListOf<PullNotification>()
            addData.add(
                PullNotification(
                    notificationId,
                    "createdate",
                    false,
                    null,
                    PullNotificationSyncAction.NONE
                )
            )
            notificationDataRepositoryImpl.appendItems(addData, true)

            coVerify(exactly = 1) { keyValueSource.saveDataToStore(addData + data) }
            notificationDataRepositoryImpl.data.test {
                val result = awaitItem().data!!
                Assert.assertEquals(notificationId, result[0].notificationId)
                Assert.assertEquals(data[0].notificationId, result[1].notificationId)
            }
        }
    }

    @Test
    fun `test appendItems to end`() {
        runTest {
            val data = mainInitialization(this)
            notificationDataRepositoryImpl.invalidate()
            advanceUntilIdle()
            clearMocks(keyValueSource)

            val addData = mutableListOf<PullNotification>()
            addData.add(
                PullNotification(
                    notificationId,
                    "createdate",
                    false,
                    null,
                    PullNotificationSyncAction.NONE
                )
            )
            notificationDataRepositoryImpl.appendItems(addData, false)

            coVerify(exactly = 1) { keyValueSource.saveDataToStore(data + addData) }
            notificationDataRepositoryImpl.data.test {
                val result = awaitItem().data!!
                Assert.assertEquals(data[0].notificationId, result[0].notificationId)
                Assert.assertEquals(notificationId, result[1].notificationId)
            }
        }
    }

    @Test
    fun `test removeItems not remove data if its not in the data list`() {
        runTest {
            val data = mainInitialization(this)
            notificationDataRepositoryImpl.invalidate()
            advanceUntilIdle()
            clearMocks(keyValueSource)

            val items = mutableListOf<PullNotification>()
            items.add(
                PullNotification(
                    "notificationId",
                    "createdate",
                    false,
                    null,
                    PullNotificationSyncAction.NONE
                )
            )
            notificationDataRepositoryImpl.removeItems(items)

            coVerify(exactly = 1) { keyValueSource.saveDataToStore(data) }
            notificationDataRepositoryImpl.data.test {
                Assert.assertEquals(data, awaitItem().data)
            }
        }
    }

    @Test
    fun `test removeItems removes passed items`() {
        runTest {
            val data = mainInitialization(this)
            notificationDataRepositoryImpl.invalidate()
            advanceUntilIdle()
            clearMocks(keyValueSource)

            val items = mutableListOf<PullNotification>()
            items.add(
                PullNotification(
                    notificationId,
                    "createdate",
                    false,
                    null,
                    PullNotificationSyncAction.NONE
                )
            )
            notificationDataRepositoryImpl.removeItems(items)

            coVerify(exactly = 1) { keyValueSource.saveDataToStore(data - items) }
            notificationDataRepositoryImpl.data.test {
                val item = awaitItem()
                Assert.assertTrue(item.data!!.isEmpty())
                Assert.assertTrue(item.isSuccessful)
                Assert.assertEquals(null, item.exception)
            }
        }
    }

    @Test
    fun `test getPage`() {
        runTest {
            initRepo(this)

            val data = mutableListOf<PullNotification>()
            data.add(
                PullNotification(
                    notificationId, "createdate", false,
                    null, PullNotificationSyncAction.NONE
                )
            )
            data.add(
                PullNotification(
                    "notid2", "createdate", false,
                    null, PullNotificationSyncAction.NONE
                )
            )
            data.add(
                PullNotification(
                    "notid3", "createdate", false,
                    null, PullNotificationSyncAction.NONE
                )
            )

            coEvery { keyValueSource.fetchData()} returns data
            coEvery { keyValueSource.fetchUnreadCount() } returns 0
            notificationDataRepositoryImpl.invalidate()
            advanceUntilIdle()
            clearMocks(keyValueSource)

            val result = notificationDataRepositoryImpl.getPage(1, 2)
            assertEquals(data[1], result[0])
            assertEquals(data[2], result[1])
        }
    }

    @Test
    fun `test getTotalSize`() {
        runTest {
            initRepo(this)

            val data = mutableListOf<PullNotification>()
            data.add(
                PullNotification(
                    notificationId, "createdate", false,
                    null, PullNotificationSyncAction.NONE
                )
            )
            data.add(
                PullNotification(
                    "notid2", "createdate", false,
                    null, PullNotificationSyncAction.NONE
                )
            )
            data.add(
                PullNotification(
                    "notid3", "createdate", false,
                    null, PullNotificationSyncAction.NONE
                )
            )

            coEvery { keyValueSource.fetchData() } returns data
            coEvery { keyValueSource.fetchUnreadCount() } returns 0
            notificationDataRepositoryImpl.invalidate()
            advanceUntilIdle()
            clearMocks(keyValueSource)

            assertEquals(3, notificationDataRepositoryImpl.getTotalSize())
        }
    }

    @Test
    fun `test indexOf`() {
        runTest {
            initRepo(this)

            val data = mutableListOf<PullNotification>()
            data.add(
                PullNotification(
                    notificationId, "createdate", false,
                    null, PullNotificationSyncAction.NONE
                )
            )
            data.add(
                PullNotification(
                    "notid2", "createdate", false,
                    null, PullNotificationSyncAction.NONE
                )
            )
            data.add(
                PullNotification(
                    "notid3", "createdate", false,
                    null, PullNotificationSyncAction.NONE
                )
            )

            coEvery { keyValueSource.fetchData() } returns data
            coEvery { keyValueSource.fetchUnreadCount() } returns 0
            notificationDataRepositoryImpl.invalidate()
            advanceUntilIdle()
            clearMocks(keyValueSource)

            assertEquals(1, notificationDataRepositoryImpl.indexOf("notid2"))
        }
    }

    @Test
    fun `test updateUnreadCount`() {
        runTest {
            val data = mainInitialization(this)
            notificationDataRepositoryImpl.invalidate()
            advanceUntilIdle()
            clearMocks(keyValueSource)

            notificationDataRepositoryImpl.updateUnreadCount(10)

            coVerify(exactly = 1) { keyValueSource.updateUnreadCount(10) }
            notificationDataRepositoryImpl.unreadCount.test {
                Assert.assertEquals(10, awaitItem())
            }
            clearMocks(keyValueSource)

            notificationDataRepositoryImpl.updateUnreadCount(10)

            coVerify(exactly = 0) { keyValueSource.updateUnreadCount(any()) }
            notificationDataRepositoryImpl.unreadCount.test {
                Assert.assertEquals(10, awaitItem())
            }
        }
    }

    @Test
    fun `test findNotificationByResourceId`() {
        runTest {
            initRepo(this)

            val data = mutableListOf<PullNotification>()
            data.add(
                PullNotification(
                    notificationId, "createdate", false,
                    PullNotificationMessage(
                        "icon",
                        "title",
                        "shortText",
                        PushAction("resourceIdfirstId", "type", "subtype")
                    ), PullNotificationSyncAction.NONE
                )
            )
            data.add(
                PullNotification(
                    "notid2", "createdate", false,
                    PullNotificationMessage(
                        "icon",
                        "title",
                        "shortText",
                        PushAction("resourceIdSecond", "type", "subtype")
                    ), PullNotificationSyncAction.NONE
                )
            )
            data.add(
                PullNotification(
                    "notid3", "createdate", false,
                    PullNotificationMessage(
                        "icon",
                        "title",
                        "shortText",
                        PushAction("resourceIdThird", "type", "subtype")
                    ), PullNotificationSyncAction.NONE
                )
            )

            coEvery { keyValueSource.fetchData() } returns data
            coEvery { keyValueSource.fetchUnreadCount() } returns 0
            notificationDataRepositoryImpl.invalidate()
            advanceUntilIdle()
            clearMocks(keyValueSource)

            assertEquals(
                data[1].notificationId,
                notificationDataRepositoryImpl.findNotificationByResourceId("resourceIdSecond")!!.notificationId
            )
            assertEquals(
                null,
                notificationDataRepositoryImpl.findNotificationByResourceId("resourceIdFourch")
            )

        }
    }

    @Test
    fun `test loadDataFromNetwork`() {
        runTest {
            initRepo(this)
            val notification1 = PullNotification("notid1", "createdate", true, null)
            val notification2 = PullNotification("notid2", "createdate", false, null)
            coEvery { networkSource.getNotifications(0) } returns PullNotificationsResponse(listOf(notification1, notification2), 2, 1)
            var networkTotal = -1
            notificationDataRepositoryImpl.loadDataFromNetwork(0, 50) {
                networkTotal = it
            }

            coVerify(exactly = 1) { networkSource.getNotifications(0) }
            coVerify(exactly = 1) { keyValueSource.updateUnreadCount(1) }
            assertEquals(2, networkTotal)
            notificationDataRepositoryImpl.unreadCount.test {
                assertEquals(1, awaitItem())
            }
        }
    }

    @Test
    fun `test loadDataFromNetwork append remove notifications to local empty list`() {
        runTest {
            initRepo(this)
            val notification1 = PullNotification("notid1", "createdate", true, null)
            val notification2 = PullNotification("notid2", "createdate", false, null)
            val notificationList = listOf(notification1, notification2)
            coEvery { networkSource.getNotifications(0) } returns PullNotificationsResponse(notificationList, 2, 1)

            var networkTotal = -1
            notificationDataRepositoryImpl.loadDataFromNetwork(0, 50) {
                networkTotal = it
            }

            coVerify(exactly = 1) { keyValueSource.saveDataToStore(notificationList) }
            assertEquals(2, networkTotal)
            notificationDataRepositoryImpl.data.test {
                val dataSourceResult = awaitItem()
                assertEquals(notificationList, dataSourceResult.data)
                assertTrue(dataSourceResult.isSuccessful)
                assertEquals(null, dataSourceResult.exception)
            }
        }
    }
    @Test
    fun `test loadDataFromNetwork append notification that not require merging processNotificationIfTopFoundNotFound`() {
        runTest {
            // Precondition

            initRepo(this)
            var notification1 = PullNotification("notid1", "createdate", true, null)
            var notification2 = PullNotification("notid2", "createdate", false, null)
            var notificationList = listOf(notification2, notification1)
            coEvery { networkSource.getNotifications(0) } returns PullNotificationsResponse(notificationList, 2, 1)

            notificationDataRepositoryImpl.loadDataFromNetwork(0, 50)

            // Condition
            clearMocks(keyValueSource)
            val notification3 = PullNotification("notid3", "createdate", true, null)
            val notification4 = PullNotification("notid4", "createdate", false, null)
            notificationList = listOf(notification4, notification3)

            coEvery { networkSource.getNotifications(2) } returns PullNotificationsResponse(notificationList, 4, 1)

            var networkTotal = -1
            notificationDataRepositoryImpl.loadDataFromNetwork(2, 50){
                networkTotal = it
            }

            val newNotList = listOf(notification2, notification1, notification4, notification3)
            coVerify(exactly = 1) { keyValueSource.saveDataToStore(newNotList) }
            assertEquals(4, networkTotal)
            notificationDataRepositoryImpl.data.test {
                val dataSourceResult = awaitItem()
                assertEquals(newNotList, dataSourceResult.data)
                assertTrue(dataSourceResult.isSuccessful)
                assertEquals(null, dataSourceResult.exception)
            }
        }
    }

    @Test
    fun `test loadDataFromNetwork merge notifications processNotificationIfTopFoundNotFound`() {
        runTest {
            // Precondition

            initRepo(this)
            var notification1 = PullNotification("notid1", "createdate", true, null)
            var notification2 = PullNotification("notid2", "createdate", false, null)
            var notificationList = listOf(notification2, notification1)
            coEvery { networkSource.getNotifications(0) } returns PullNotificationsResponse(notificationList, 2, 1)

            notificationDataRepositoryImpl.loadDataFromNetwork(0, 50)

            // Condition
            clearMocks(keyValueSource)

            notification1 = PullNotification("notid1", "createdate", true, null)
            notification2 = PullNotification("notid2", "createdate", false, null)
            val notification3 = PullNotification("notid3", "createdate", true, null)
            val notification4 = PullNotification("notid4", "createdate", false, null)
            notificationList = listOf(notification4, notification3, notification2, notification1)

            coEvery { networkSource.getNotifications(0) } returns PullNotificationsResponse(notificationList, 4, 1)
            var networkTotal = -1
            notificationDataRepositoryImpl.loadDataFromNetwork(0, 50){
                networkTotal = it
            }

            assertEquals(4, networkTotal)
            coVerify(exactly = 1) { keyValueSource.saveDataToStore(listOf(notification4, notification3, notification2, notification1)) }
            notificationDataRepositoryImpl.data.test {
                val dataSourceResult = awaitItem()
                assertEquals(notificationList, dataSourceResult.data)
                assertTrue(dataSourceResult.isSuccessful)
                assertEquals(null, dataSourceResult.exception)
            }
        }
    }

    @Test
    fun `test loadDataFromNetwork merge notifications from bottom mergeRemoteNotificationsWithLocal`() {
        runTest {
            // Precondition

            initRepo(this)
            var notification0 = PullNotification("notid0", "createdate", true, null)
            var notification1 = PullNotification("notid1", "createdate", true, null)
            var notification2 = PullNotification("notid2", "createdate", false, null)
            var notificationList = listOf(notification0, notification2, notification1)
            coEvery { networkSource.getNotifications(0) } returns PullNotificationsResponse(notificationList, 3, 1)

            notificationDataRepositoryImpl.loadDataFromNetwork(0, 50)

            // Condition
            clearMocks(keyValueSource)
            notification1 = PullNotification("notid1", "createdate", true, null)
            notification2 = PullNotification("notid2", "createdate", true, null)
            val notification3 = PullNotification("notid3", "createdate", true, null)
            val notification4 = PullNotification("notid4", "createdate", false, null)
            notificationList = listOf(notification2, notification1, notification4, notification3, )

            coEvery { networkSource.getNotifications(1) } returns PullNotificationsResponse(notificationList, 5, 1)

            var networkTotal = -1
            notificationDataRepositoryImpl.loadDataFromNetwork(1, 50){
                networkTotal = it
            }

            val expectedResultList = listOf(notification0, notification2, notification1, notification4, notification3)
            assertEquals(5, networkTotal)
            coVerify(exactly = 1) { keyValueSource.saveDataToStore(expectedResultList) }
            notificationDataRepositoryImpl.data.test {
                val dataSourceResult = awaitItem()
                assertEquals(expectedResultList, dataSourceResult.data)
                assertTrue(dataSourceResult.isSuccessful)
                assertEquals(null, dataSourceResult.exception)
            }
        }
    }

    @Test
    fun `test loadDataFromNetwork merge empty notification in range`() {
        runTest {
            // Precondition

            initRepo(this)
            val notification0 = PullNotification("notid0", "createdate", true, null)
            val notification1 = PullNotification("notid1", "createdate", true, null)
            val notification2 = PullNotification("notid2", "createdate", false, null)
            val notificationList = listOf(notification0, notification2, notification1)
            coEvery { networkSource.getNotifications(0) } returns PullNotificationsResponse(notificationList, 3, 1)

            notificationDataRepositoryImpl.loadDataFromNetwork(0, 50)

            // Condition
            clearMocks(keyValueSource)
            coEvery { networkSource.getNotifications(0) } returns PullNotificationsResponse(listOf(), 0, 0)

            var networkTotal = -1
            notificationDataRepositoryImpl.loadDataFromNetwork(0, 50) {
                networkTotal = it
            }

            assertEquals(0, networkTotal)
            coVerify(exactly = 1) { keyValueSource.saveDataToStore(listOf()) }
            notificationDataRepositoryImpl.data.test {
                val dataSourceResult = awaitItem()
                assertEquals(0, dataSourceResult.data!!.size)
                assertTrue(dataSourceResult.isSuccessful)
                assertEquals(null, dataSourceResult.exception)
            }
        }
    }
}