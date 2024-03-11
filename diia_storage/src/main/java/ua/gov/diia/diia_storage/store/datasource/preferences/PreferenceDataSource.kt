package ua.gov.diia.diia_storage.store.datasource.preferences

interface PreferenceDataSource {

    suspend fun setBoolean(key: String, value: Boolean)

    suspend fun getBoolean(key: String): Boolean
}