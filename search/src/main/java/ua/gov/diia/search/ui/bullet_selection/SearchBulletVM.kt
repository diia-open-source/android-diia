package ua.gov.diia.search.ui.bullet_selection

import androidx.lifecycle.*
import ua.gov.diia.search.models.SearchableBullet
import ua.gov.diia.core.util.event.UiDataEvent
import ua.gov.diia.core.util.extensions.lifecycle.asLiveData

@Suppress("UNCHECKED_CAST")
class SearchBulletVMF : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        SearchBulletVM() as T
}

class SearchBulletVM : ViewModel() {

    private val _screenHeader = MutableLiveData<String>()
    val screenHeader = _screenHeader.asLiveData()

    private val _contentTitle = MutableLiveData<String>()
    val contentTitle = _contentTitle.asLiveData()

    private var _data = MutableLiveData<List<SearchableBullet>>()
    val data = _data.asLiveData()

    val selectedBulletId = MutableLiveData(-1)

    val enableActionButton: LiveData<Boolean> = selectedBulletId.map { id -> id != null && id != -1 }

    fun setArgs(data: Array<SearchableBullet>, header: String, title: String) {
        _data.value = data.toList()
        _screenHeader.value = header
        _contentTitle.value = title
    }

    private val _setNavigationResult = MutableLiveData<UiDataEvent<SearchableBullet>>()
    val setNavigationResult = _setNavigationResult.asLiveData()

    fun sendResult() {
        val enumCode = getSelectedItemEnumCode() ?: return
        _setNavigationResult.value = UiDataEvent(enumCode)
    }

    private fun getSelectedItemEnumCode(): SearchableBullet? {
        val selectedItemPosition = selectedBulletId.value
        val data = _data.value

        return if (
            selectedItemPosition != null
            && selectedItemPosition != -1
            && !data.isNullOrEmpty()
            && selectedItemPosition < data.size
        ) {
            data[selectedItemPosition]
        } else {
            null
        }
    }

}