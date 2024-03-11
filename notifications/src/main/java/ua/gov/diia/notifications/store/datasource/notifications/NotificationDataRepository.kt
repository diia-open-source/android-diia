package ua.gov.diia.notifications.store.datasource.notifications

import kotlinx.coroutines.flow.Flow
import ua.gov.diia.diia_storage.store.datasource.DataSource
import ua.gov.diia.notifications.models.notification.pull.PullNotification

interface NotificationDataRepository : DataSource<List<PullNotification>> {

    val unreadCount: Flow<Int>

    /**
     * Remove notification with specified :notificationId fom local store
     */
    suspend fun removeNotification(notificationId: String)

    /**
     * Set pull notification with id :notificationId isRead status to tru
     */
    suspend fun markNotificationAsRead(notificationId: String)

    /**
     * Mark local messages as read
     */
    suspend fun updateWithLocal(remoteNotifications: List<PullNotification>)

    /**
     * Find pull notification with same :notificationId or null
     */
    suspend fun getPullNotificationById(notificationId: String): PullNotification?

    /**
     * Notify backend about removed and read messages
     */
    suspend fun syncWithRemote()

    /**
     * Append new messages to start of local list
     */
    suspend fun appendItems(items: List<PullNotification>, toStart: Boolean)

    /**
     * Remove outdated messages from local list
     */
    suspend fun removeItems(items: List<PullNotification>)

    /**
     * Get local pull notifications from :skip position to min(pageSize, totalSize)
     */
    suspend fun getPage(skip: Int, pageSize: Int): List<PullNotification>

    /**
     * Get amount of items stored in local storage
     */
    suspend fun getTotalSize(): Int

    /**
     * Retrieve position of notification with :notificationId in current local store or -1 if notification not found
     */
    suspend fun indexOf(notificationId: String): Int

    /**
     * Update unread count of notifications
     */
    suspend fun updateUnreadCount(newUnreadCount: Int)

    /**
     * Find notification by resource id
     */
    suspend fun findNotificationByResourceId(resourceId: String): PullNotification?

    /**
     * Load notifications from network. Use [updateTotal] for view pagination
     */
    suspend fun loadDataFromNetwork(skip: Int, pageSize: Int, updateTotal: (Int) -> Unit = {})

}