package ua.gov.diia.opensource.data.data_source.itn

import ua.gov.diia.core.models.ITN
import ua.gov.diia.diia_storage.store.datasource.DataSourceDataResult
import javax.inject.Inject

class NetworkItnDataSource @Inject constructor(
) {

    suspend fun fetchData(): DataSourceDataResult<ITN> {
        return try {
            return DataSourceDataResult.failed(RuntimeException("Not implemented"))

        } catch (e: Exception) {
            DataSourceDataResult.failed(e)
        }
    }
}