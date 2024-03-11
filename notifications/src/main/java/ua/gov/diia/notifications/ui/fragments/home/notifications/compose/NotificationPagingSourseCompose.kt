package ua.gov.diia.notifications.ui.fragments.home.notifications.compose

import androidx.paging.PagingSource
import androidx.paging.PagingState
import kotlinx.coroutines.runBlocking
import ua.gov.diia.notifications.models.notification.LoadingState
import ua.gov.diia.notifications.models.notification.pull.PullNotificationSyncAction
import ua.gov.diia.notifications.store.datasource.notifications.NotificationDataRepository
import ua.gov.diia.ui_base.components.molecule.message.MessageMoleculeData

class NotificationPagingSourceCompose(
    private val notificationDataSource: NotificationDataRepository,
    private val composeMapper: NotificationsMapperCompose,
    private val onContentLoadedStateChanged: (LoadingState) -> Unit,
    ) : PagingSource<Int, MessageMoleculeData>(), NotificationsMapperCompose by composeMapper {

    private var networkTotal = 0

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, MessageMoleculeData> {
        return try {
            val pageSize = params.loadSize
            val key = params.key
            val skip = key ?: 0

            onContentLoadedStateChanged(
                if (key == null || key == 0) {
                    LoadingState.FIRST_PAGE_LOADING
                } else {
                    LoadingState.ADDITIONAL_PAGE_LOADING
                }
            )

            notificationDataSource.loadDataFromNetwork(skip, pageSize, updateTotal = {
                networkTotal = it
            })

            val total = notificationDataSource.getTotalSize()

            val response = notificationDataSource.getPage(skip, pageSize)
            val nextPage = (skip + pageSize).coerceAtMost(notificationDataSource.getTotalSize())

            val prevKey = if (key == null || key == 0) {
                null
            } else {
                (key - pageSize).coerceAtLeast(0)
            }
            val nextKey = if (nextPage != networkTotal && skip != nextPage) nextPage else null

            val convertedData = mutableListOf<MessageMoleculeData>().apply {
                response.forEach { element ->
                    with(composeMapper) {
                        add(element.toComposeMessage())
                    }
                }
            }

            val filteredConvertedData = convertedData.filter {
                it.syncAction != PullNotificationSyncAction.REMOVE
            }
            onContentLoadedStateChanged(LoadingState.NOT_LOADING)
            LoadResult.Page(
                data = filteredConvertedData,
                prevKey = prevKey,
                nextKey = nextKey,
                itemsBefore = skip,
                itemsAfter = total - skip + pageSize
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        } finally {
            onContentLoadedStateChanged(LoadingState.NOT_LOADING)
        }

    }

    override fun getRefreshKey(state: PagingState<Int, MessageMoleculeData>): Int? {
        val atMost = runBlocking {
            notificationDataSource.getTotalSize()
        }
        val anchor = state.anchorPosition?.coerceAtMost(atMost) ?: return null
        val closestPage = state.closestPageToPosition(anchor)
        return closestPage?.prevKey
    }

}

