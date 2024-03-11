package ua.gov.diia.search.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import ua.gov.diia.core.util.DispatcherProvider
import ua.gov.diia.core.util.event.UiDataEvent
import ua.gov.diia.core.util.extensions.isStringValid
import ua.gov.diia.core.util.extensions.lifecycle.asLiveData
import ua.gov.diia.search.models.SearchResult
import ua.gov.diia.search.models.SearchableItem
import javax.inject.Inject

@HiltViewModel
class SearchFVM @Inject constructor(
    private val dispatcherProvider: DispatcherProvider,
) : ViewModel() {

    private var _resultKey: String? = null

    private var _itemList: List<SearchableItem> = emptyList()

    private val _displayItemList = MutableLiveData<List<SearchableItem>>()
    val displayItemList = _displayItemList.asLiveData()

    private var filterJob: Job? = null

    fun doInit(data: List<SearchableItem>, resultKey: String) {
        _resultKey = resultKey
        _itemList = data
        _displayItemList.value = data
    }

    //------------- Data filtering -----------------------

    fun filter(q: String) {
        filterJob?.cancel()
        filterJob = viewModelScope.launch(dispatcherProvider.work) {
            val items = _itemList.filterByQuery(q)
            if (isActive) {
                _displayItemList.postValue(items)
            }
        }
    }

    private fun List<SearchableItem>.filterByQuery(q: String?): List<SearchableItem> {
        if (q == null || !q.isStringValid()) return this
        val result = mutableListOf<Pair<Int, SearchableItem>>()

        this.forEach {
            val index = it.getQueryString().indexOf(q, ignoreCase = true)
            if (index != -1) result.add(index to it)
        }

        return result.sortedBy { it.first }.map { it.second }.safeSubList(0, 200)
    }

    private fun <T> List<T>.safeSubList(fromIndex: Int, toIndex: Int): List<T> =
        this.subList(fromIndex, toIndex.coerceAtMost(this.size))

    //---------- Find result ----------------------

    private val _setResult = MutableLiveData<UiDataEvent<SearchResult>>()
    val setResult = _setResult

    fun findResult(selectedQ: String) {
        val key = _resultKey ?: return
        val item = _displayItemList.value?.findResult(selectedQ)
        val result = SearchResult(item, key)
        _setResult.value = UiDataEvent(result)
    }

    private fun List<SearchableItem>.findResult(q: String): Any? = let { list ->
        list.find { item -> item match q }
    }

    private infix fun SearchableItem.match(q: String) = getQueryString() == q
}