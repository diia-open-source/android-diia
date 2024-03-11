package ua.gov.diia.diia_storage.store.datasource

data class DataSourceDataResult<T>(
    val isSuccessful: Boolean,
    val data: T?,
    val exception: Exception?,
) {

    companion object {
        fun <T> failed(exception: Exception? = null): DataSourceDataResult<T> {
            return DataSourceDataResult(false, null, exception)
        }

        fun <T> successful(data: T): DataSourceDataResult<T> {
            return DataSourceDataResult(true, data, null)
        }
    }
}
