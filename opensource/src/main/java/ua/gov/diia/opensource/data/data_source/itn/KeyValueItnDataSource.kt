package ua.gov.diia.opensource.data.data_source.itn

import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import ua.gov.diia.core.models.ITN
import ua.gov.diia.core.util.delegation.WithCrashlytics
import ua.gov.diia.diia_storage.DiiaStorage
import ua.gov.diia.diia_storage.model.PreferenceKey
import ua.gov.diia.diia_storage.store.AbstractKeyValueDataSource
import ua.gov.diia.diia_storage.store.Preferences
import javax.inject.Inject

class KeyValueItnDataSource @Inject constructor(
    diiaStorage: DiiaStorage,
    withCrashlytics: WithCrashlytics
) : AbstractKeyValueDataSource<ITN>(diiaStorage, withCrashlytics) {

    override val preferenceKey: PreferenceKey = Preferences.ITN

    override val jsonAdapter: JsonAdapter<ITN> = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()
        .adapter(ITN::class.java)

    suspend fun fetchData() = if (store.containsKey(preferenceKey)) {
        loadData()
    } else {
        null
    }
}