package ua.gov.diia.notifications.store.datasource.notifications

import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import ua.gov.diia.core.di.actions.GlobalActionNotificationRead
import ua.gov.diia.core.util.delegation.WithCrashlytics
import ua.gov.diia.core.util.event.UiDataEvent
import ua.gov.diia.diia_storage.store.datasource.DataSourceDataResult
import ua.gov.diia.notifications.models.notification.pull.PullNotification
import ua.gov.diia.notifications.models.notification.pull.PullNotificationSyncAction
import ua.gov.diia.notifications.models.notification.pull.PullNotificationsToModify
import ua.gov.diia.notifications.util.notification.manager.DiiaNotificationManager
import javax.inject.Inject

class NotificationDataRepositoryImpl @Inject constructor(
    private val scope: CoroutineScope,
    private val keyValueSource: KeyValueNotificationDataSource,
    private val diiaNotificationManager: DiiaNotificationManager,
    private val networkSource: NetworkNotificationDataSource,
    @GlobalActionNotificationRead private val actionNotificationRead: MutableLiveData<UiDataEvent<String>>,
    private val withCrashlytics: WithCrashlytics
) : NotificationDataRepository {

    private val _isDataLoading = MutableStateFlow(false)
    override val isDataLoading: Flow<Boolean>
        get() = _isDataLoading

    private val _data = MutableStateFlow<DataSourceDataResult<List<PullNotification>>>(
        DataSourceDataResult.failed()
    )
    override val data: Flow<DataSourceDataResult<List<PullNotification>>>
        get() = _data


    private val _unreadCount = MutableStateFlow(0)
    override val unreadCount: Flow<Int>
        get() = _unreadCount

    override fun invalidate() {
        scope.launch {
            _isDataLoading.value = true

            val data = keyValueSource.fetchData()
            _data.emit(DataSourceDataResult.successful(data))
            _unreadCount.emit(keyValueSource.fetchUnreadCount())

            syncWithRemote()

            _isDataLoading.value = false
        }
    }

    override suspend fun getPullNotificationById(notificationId: String): PullNotification? {
        return _data.value.data?.find {
            it.notificationId == notificationId
        }
    }

    override suspend fun removeNotification(notificationId: String) {
        try {
            val notification = PullNotificationsToModify(listOf(notificationId))
            updateMessageSyncStatus(notification, PullNotificationSyncAction.REMOVE)

            networkSource.deleteNotifications(notification)
        } catch (e: Exception) {
            withCrashlytics.sendNonFatalError(e)
        }
    }

    override suspend fun markNotificationAsRead(notificationId: String) {
        try {
            if (_data.value.data == null) {
                PullNotificationsToModify(listOf(notificationId)).let {
                    networkSource.markNotificationsAsRead(it)
                }
                return
            }
            updateMessageSyncStatus(
                PullNotificationsToModify(listOf(notificationId)),
                PullNotificationSyncAction.READ
            )
            actionNotificationRead.postValue(UiDataEvent(notificationId))
        } catch (e: Exception) {
            withCrashlytics.sendNonFatalError(e)
        }
    }

    override suspend fun updateWithLocal(remoteNotifications: List<PullNotification>) {
        val cachedData = (_data.value.data ?: return).toMutableList()
        remoteNotifications.forEach { messageToModify ->
            val localMessage =
                cachedData.find { it.notificationId == messageToModify.notificationId }
            if (localMessage == null) {
                cachedData += messageToModify
            } else {
                if (localMessage.isRead == false && localMessage.isRead != messageToModify.isRead) {
                    localMessage.isRead = messageToModify.isRead
                }
            }
        }
        keyValueSource.saveDataToStore(cachedData)
        _data.emit(DataSourceDataResult.successful(cachedData))
    }

    override suspend fun appendItems(items: List<PullNotification>, toStart: Boolean) {
        val localData = (this._data.value.data ?: emptyList())
        val newData = if (toStart) {
            items + localData
        } else {
            localData + items
        }
        keyValueSource.saveDataToStore(newData)
        _data.emit(DataSourceDataResult.successful(newData))
    }

    private suspend fun insertJustAfter(position: Int, notifications: List<PullNotification>) {
        val localData = this._data.value.data?.toMutableList() ?: return
        val newData = localData.subList(0, position) + notifications + localData.subList(
            position,
            localData.size
        )
        _data.emit(DataSourceDataResult.successful(newData))
    }

    override suspend fun removeItems(items: List<PullNotification>) {
        val localData = this._data.value.data ?: return

        val itemsToRemoveId = items.map { it.notificationId }
        val itemsToRemove = localData.filter { it.notificationId in itemsToRemoveId }
        val newData = localData - itemsToRemove
        keyValueSource.saveDataToStore(newData)
        _data.emit(DataSourceDataResult.successful(newData))
    }

    private suspend fun updateMessageSyncStatus(
        pullNotificationsToModify: PullNotificationsToModify,
        action: PullNotificationSyncAction
    ) {
        val cachedData = _data.value.data ?: return
        var localStorageModifications = 0

        val notFoundLocalNotificationIds = mutableListOf<String>()

        pullNotificationsToModify.messageIds.forEach { messageToModify ->
            diiaNotificationManager.clearNotification(messageToModify)
            val localNotification = cachedData.find { it.notificationId == messageToModify }
            if (localNotification == null) {
                notFoundLocalNotificationIds.add(messageToModify)
            } else {
                if (localNotification.syncAction != action) {
                    when (action) {
                        PullNotificationSyncAction.READ -> {
                            with(localNotification) {
                                if (isRead == false) {
                                    isRead = true
                                    syncAction = action
                                    localStorageModifications++
                                }
                            }
                        }

                        PullNotificationSyncAction.REMOVE -> {
                            localNotification.syncAction = action
                            localStorageModifications++
                        }

                        else -> {
                        }
                    }
                }
            }
        }
        if (notFoundLocalNotificationIds.isNotEmpty()) {
            try {
                PullNotificationsToModify(notFoundLocalNotificationIds).let {
                    val updateResponse = networkSource.markNotificationsAsRead(it)
                    _unreadCount.emit(updateResponse.unread)
                }
            } catch (e: Exception) {
                withCrashlytics.sendNonFatalError(e)
            }
        }
        if (localStorageModifications > 0) {
            syncWithRemote()
        }
    }

    override suspend fun syncWithRemote() {
        var cachedData = _data.value.data ?: return
        var unreadCount = _unreadCount.value
        try {
            val readMessages = cachedData.filter {
                it.syncAction == PullNotificationSyncAction.READ
            }

            val removedMessages = cachedData.filter {
                it.syncAction == PullNotificationSyncAction.REMOVE
            }

            unreadCount =
                (unreadCount - readMessages.size + readMessages.count { it.isRead == false })
                    .coerceAtLeast(0)

            if (readMessages.isNotEmpty()) {
                try {
                    PullNotificationsToModify(readMessages.mapNotNull { it.notificationId }).let {
                        val updateResponse = networkSource.markNotificationsAsRead(it)
                        unreadCount = updateResponse.unread
                        readMessages.forEach {
                            it.syncAction = PullNotificationSyncAction.NONE
                        }
                    }
                } catch (e: Exception) {
                    withCrashlytics.sendNonFatalError(e)
                }
            }

            if (removedMessages.isNotEmpty()) {
                PullNotificationsToModify(removedMessages.mapNotNull { it.notificationId }).let {
                    val updateResponse = networkSource.markNotificationsAsRead(it)
                    unreadCount = updateResponse.unread
                    cachedData = cachedData - removedMessages
                }
            }
        } catch (e: Exception) {
            withCrashlytics.sendNonFatalError(e)
        } finally {
            updateUnreadCount(unreadCount)
            keyValueSource.saveDataToStore(cachedData)
            _data.emit(DataSourceDataResult.successful(cachedData))
        }
    }

    override suspend fun getPage(skip: Int, pageSize: Int): List<PullNotification> {
        val data = _data.value.data
        return data?.subList(skip, (skip + pageSize).coerceAtMost(data.size)) ?: emptyList()
    }

    override suspend fun getTotalSize(): Int {
        return _data.value.data?.size ?: 0
    }

    override suspend fun indexOf(notificationId: String): Int {
        return _data.value.data?.indexOfFirst { it.notificationId == notificationId } ?: NOT_FOUND
    }

    override suspend fun updateUnreadCount(newUnreadCount: Int) {
        if (_unreadCount.value != newUnreadCount) {
            keyValueSource.updateUnreadCount(newUnreadCount)
            _unreadCount.emit(newUnreadCount)
        }
    }

    override suspend fun findNotificationByResourceId(resourceId: String): PullNotification? {
        return _data.value.data?.find {
            it.pullNotificationMessage?.action?.resourceId == resourceId
        }
    }

    override suspend fun loadDataFromNetwork(skip: Int, pageSize: Int, updateTotal: (Int) -> Unit) {

        val notificationsResponse = try {
            networkSource.getNotifications(skip)
        } catch (e: Exception) {
            return
        }

        val networkTotal = notificationsResponse.total
        updateTotal(networkTotal)
        updateUnreadCount(notificationsResponse.unread)
        val remoteNotificationsPage = notificationsResponse.notifications

        if (remoteNotificationsPage.isEmpty()) {
            if (networkTotal < getTotalSize()) {
                removeLocalNotificationsThatNotExistOnServerInRange(remoteNotificationsPage, skip, pageSize)
            }
            return
        } else {
            if (getTotalSize() == 0) {
                appendItems(remoteNotificationsPage, true)
                return
            }
            mergeRemoteNotificationsWithLocal(remoteNotificationsPage, skip, pageSize)
        }

        return
    }

    private suspend fun mergeRemoteNotificationsWithLocal(
        remoteNotificationsPage: List<PullNotification>,
        skip: Int,
        pageSize: Int
    ) {
        val headItem = remoteNotificationsPage.first().notificationId
        val localPageTopPosition = if (headItem != null) {
            indexOf(headItem)
        } else {
            NOT_FOUND
        }

        if (localPageTopPosition == NOT_FOUND) {
            processNotificationIfTopRemoteNotificationNotFound(remoteNotificationsPage, skip)
        } else {
            removeLocalNotificationsThatNotExistOnServerInRange(remoteNotificationsPage, skip, pageSize)
            updateWithLocal(remoteNotificationsPage)
        }
    }

    private suspend fun processNotificationIfTopRemoteNotificationNotFound(
        remoteNotificationsPage: List<PullNotification>,
        skip: Int
    ) {
        val remoteTopPositionMatchingLocal =
            getIndexOfLocalTopNotificationInRemoveList(remoteNotificationsPage)

        if (remoteTopPositionMatchingLocal == NOT_FOUND) {
            val toStart = skip < getTotalSize()
            appendItems(remoteNotificationsPage, toStart)
        } else {
            val newItems =
                remoteNotificationsPage.subList(0, remoteTopPositionMatchingLocal)
            insertJustAfter(skip, newItems)
            removeLocalNotificationsThatNotExistOnServerInRange(remoteNotificationsPage,
                skip + remoteTopPositionMatchingLocal,
                remoteNotificationsPage.size - remoteTopPositionMatchingLocal)
            updateWithLocal(remoteNotificationsPage)
        }
    }

    private suspend fun getIndexOfLocalTopNotificationInRemoveList(remoteNotificationsPage: List<PullNotification>): Int {
        var remoteTopPositionMatchingLocal = -1
        for (i in remoteNotificationsPage.indices) {
            val remoteId = remoteNotificationsPage[i].notificationId ?: continue
            if (getPullNotificationById(remoteId) != null) {
                remoteTopPositionMatchingLocal = i
                break
            }
        }
        return remoteTopPositionMatchingLocal
    }

    /**
     * Detect items that was removed remotely but present locally
     * Remove local items until pages are the same
     */
    private suspend fun removeLocalNotificationsThatNotExistOnServerInRange(
        remoteChanges: List<PullNotification>,
        topIndex: Int,
        pageSize: Int
    ) {
        val remoteMessagesIds = remoteChanges.mapNotNull { it.notificationId }

        var diff = getPage(topIndex, pageSize)
            .filter { it.notificationId !in remoteMessagesIds }

        while (diff.isNotEmpty()) {
            removeItems(diff)
            diff = getPage(topIndex, pageSize)
                .filter { it.notificationId !in remoteMessagesIds }
        }
    }

    companion object {
        private const val NOT_FOUND = -1
    }
}