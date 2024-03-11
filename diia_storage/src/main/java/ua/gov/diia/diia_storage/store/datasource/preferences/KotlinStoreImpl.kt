package ua.gov.diia.diia_storage.store.datasource.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.firstOrNull

class KotlinStoreImpl(private val appContext: Context) : PreferenceDataSource {

    private companion object {
        const val PREFERENCES_STORAGE = "preferences_storage"
    }

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
        name = PREFERENCES_STORAGE
    )

    override suspend fun setBoolean(key: String, value: Boolean) {
        val booleanKey = booleanPreferencesKey(key)
        appContext.dataStore.edit { preferences ->
            preferences[booleanKey] = value
        }
    }

    override suspend fun getBoolean(key: String): Boolean {
        val booleanKey = booleanPreferencesKey(key)
        return appContext.dataStore.data.firstOrNull()
            ?.get(booleanKey) ?: false
    }

}