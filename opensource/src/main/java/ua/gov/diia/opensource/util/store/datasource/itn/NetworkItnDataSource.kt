package ua.gov.diia.opensource.util.store.datasource.itn

import ua.gov.diia.core.di.data_source.http.AuthorizedClient
import ua.gov.diia.core.models.ITN
import ua.gov.diia.diia_storage.store.datasource.DataSourceDataResult
import ua.gov.diia.opensource.data.data_source.network.api.ApiDocs
import javax.inject.Inject

class NetworkItnDataSource @Inject constructor(
    @AuthorizedClient private val apiDocs: ApiDocs
) {

    suspend fun fetchData(): DataSourceDataResult<ITN> {
        return try {
            val itn = apiDocs.getItn()
            DataSourceDataResult.successful(itn)
        } catch (e: Exception) {
            DataSourceDataResult.failed(e)
        }
    }
}