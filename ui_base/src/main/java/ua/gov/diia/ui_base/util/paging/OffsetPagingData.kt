package ua.gov.diia.ui_base.util.paging

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import androidx.paging.cachedIn
import kotlin.math.abs

fun <T : Any> ViewModel.offsetPagingData(
    pageSize: Int = 5,
    prefetchDistance: Int = 4,
    enablePlaceholders: Boolean = false,
    initialLoadSize: Int = 10,
    initialKey: Int = 0,
    params: suspend (offset: Int, pageSize: Int) -> List<T>
): LiveData<PagingData<T>> {
    val pagingConfig = PagingConfig(
        pageSize = pageSize,
        prefetchDistance = prefetchDistance,
        enablePlaceholders = enablePlaceholders,
        initialLoadSize = initialLoadSize
    )

    return Pager(
        config = pagingConfig,
        initialKey = initialKey,
        pagingSourceFactory = {
            offsetDataSource(pagingConfig, params)
        }
    )
        .flow
        .cachedIn(viewModelScope)
        .asLiveData()
}

fun <T : Any> offsetDataSource(
    pagingConfig: PagingConfig,
    block: suspend (offset: Int, pageSize: Int) -> List<T>
) = object : PagingSource<Int, T>() {

    override fun getRefreshKey(state: PagingState<Int, T>): Int {
        return ((state.anchorPosition ?: 0) - state.config.initialLoadSize / 2).coerceAtLeast(0)
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, T> =
        try {
            val key = params.key ?: 0
            val loadSize = params.loadSize
            val pageOffset = if (key < 0) 0 else key
            val currentPageSize = if (key < 0) loadSize + key else loadSize

            val page = block(pageOffset, currentPageSize)
            val nextOffsetValue = pageOffset + currentPageSize
            val prevOffsetValue = pageOffset - pagingConfig.pageSize
            val nextKey = if (page.size == params.loadSize) nextOffsetValue else null
            val prevKey = if (prevOffsetValue >= 0) {
                prevOffsetValue
            } else if (key < 0 || abs(prevOffsetValue) == pagingConfig.pageSize) {
                null
            } else {
                prevOffsetValue
            }

            LoadResult.Page(
                data = page,
                prevKey = prevKey,
                nextKey = nextKey
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
}