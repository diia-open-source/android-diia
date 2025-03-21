package ua.gov.diia.diia_storage.store

import com.squareup.moshi.JsonAdapter
import ua.gov.diia.core.util.delegation.WithCrashlytics
import ua.gov.diia.diia_storage.model.KeyValueStore
import ua.gov.diia.diia_storage.model.PreferenceKey

abstract class AbstractKeyValueDataSource<T>(val store: KeyValueStore, val withCrashlytics: WithCrashlytics) {

    protected abstract val preferenceKey: PreferenceKey
    protected abstract val jsonAdapter: JsonAdapter<T>

    open fun saveDataToStore(data: T) {
        store.set(preferenceKey, jsonAdapter.toJson(data))
    }

    fun loadData(): T? {
        if (store.containsKey(preferenceKey)) {
            try {
                return jsonAdapter.fromJson(store.getString(preferenceKey, Preferences.DEF))
            } catch (e: Exception) {
                store.set(preferenceKey, "")
                withCrashlytics.sendNonFatalError(e)
            }
        }
        return null
    }

}