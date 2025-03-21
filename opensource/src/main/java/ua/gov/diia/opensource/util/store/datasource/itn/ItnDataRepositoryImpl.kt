package ua.gov.diia.opensource.util.store.datasource.itn

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import ua.gov.diia.core.models.ITN
import ua.gov.diia.diia_storage.store.datasource.DataSourceDataResult
import ua.gov.diia.diia_storage.store.datasource.itn.ItnDataRepository

class ItnDataRepositoryImpl(
    private val scope: CoroutineScope,
    private val keyValueDataSource: KeyValueItnDataSource,
    private val networkDocumentsDataSource: NetworkItnDataSource
) : ItnDataRepository {

    private val _isDataLoading = MutableStateFlow(false)
    override val isDataLoading: Flow<Boolean>
        get() = _isDataLoading

    private val _data = MutableStateFlow<DataSourceDataResult<ITN>>(DataSourceDataResult.failed())
    override val data: Flow<DataSourceDataResult<ITN>>
        get() = _data

    override fun invalidate() {
        scope.launch {
            if (_isDataLoading.value) {
                return@launch
            }

            _isDataLoading.value = true
            val cachedData = keyValueDataSource.fetchData()
            if (cachedData != null) {
                _data.tryEmit(DataSourceDataResult.successful(cachedData))
            } else {
                val networkData = networkDocumentsDataSource.fetchData()
                val data = networkData.data
                if (networkData.isSuccessful && data != null) {
                    keyValueDataSource.saveDataToStore(data)
                    _data.tryEmit(DataSourceDataResult.successful(data))
                } else {
                    _data.tryEmit(DataSourceDataResult.failed())
                }
            }
            _isDataLoading.value = false
        }
    }
}