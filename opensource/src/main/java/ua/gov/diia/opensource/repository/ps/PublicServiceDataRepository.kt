package ua.gov.diia.opensource.repository.ps

import com.squareup.moshi.JsonAdapter
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ua.gov.diia.core.data.repository.DataRepository
import ua.gov.diia.core.di.data_source.http.AuthorizedClient
import ua.gov.diia.core.util.DispatcherProvider
import ua.gov.diia.diia_storage.DiiaStorage
import ua.gov.diia.diia_storage.store.Preferences
import ua.gov.diia.login.ui.PostLoginAction
import ua.gov.diia.opensource.di.MoshiAdapterPublicServiceCategories
import ua.gov.diia.publicservice.models.PublicServicesCategories
import ua.gov.diia.publicservice.network.ApiPublicServices
import javax.inject.Inject

class PublicServiceDataRepository @Inject constructor(
    @MoshiAdapterPublicServiceCategories private val adapter: JsonAdapter<PublicServicesCategories?>,
    @AuthorizedClient private val apiPublicServices: ApiPublicServices,
    private val diiaStorage: DiiaStorage,
    private val dispatcherProvider: DispatcherProvider
) : DataRepository<PublicServicesCategories?>, PostLoginAction {

    private val _data = MutableSharedFlow<PublicServicesCategories?>(
        replay = 1,
        onBufferOverflow = BufferOverflow.DROP_LATEST
    )
    override val data: Flow<PublicServicesCategories?> = _data

    override suspend fun load(): PublicServicesCategories? {
        val cache = loadCachedData()
        _data.emit(cache)
        return try {
            val services = apiPublicServices.getPublicServices()
            coroutineScope {
                launch { _data.emit(services) }
                launch { cacheData(services) }
            }
            services
        } catch (e: Exception) {
            if (cache != null) return cache
            else throw e
        }
    }

    override suspend fun onPostLogin() {
        load()
    }

    private fun loadCachedData(): PublicServicesCategories? = try {
        val servicesJson = diiaStorage.getString(Preferences.PublicServicesCategories, "")
        if (servicesJson.isNotEmpty()) {
            adapter.fromJson(servicesJson)
        } else {
            null
        }
    } catch (e: Exception) {
        null
    }

    private suspend fun cacheData(categories: PublicServicesCategories) =
        withContext(dispatcherProvider.work) {
            val json = adapter.toJson(categories)
            diiaStorage.set(Preferences.PublicServicesCategories, json)
        }

    override suspend fun clear() {
        _data.emit(null)
    }

}