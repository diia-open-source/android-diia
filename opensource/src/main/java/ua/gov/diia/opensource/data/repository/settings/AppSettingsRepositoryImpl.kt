package ua.gov.diia.opensource.data.repository.settings

import kotlinx.coroutines.withContext
import ua.gov.diia.core.util.DispatcherProvider
import ua.gov.diia.diia_storage.CommonPreferenceKeys
import ua.gov.diia.diia_storage.DiiaStorage
import ua.gov.diia.diia_storage.store.Preferences
import javax.inject.Inject

class AppSettingsRepositoryImpl @Inject constructor(
    private val diiaStorage: DiiaStorage,
    private val dispatcherProvider: DispatcherProvider
) : AppSettingsRepository {

    private companion object {
        const val DEF_STRING_VALUE = "unknown"
    }

    override suspend fun enableBiometricAuth(enable: Boolean) {
        withContext(dispatcherProvider.work) {
            diiaStorage.set(Preferences.UseTouchId, enable)
        }
    }

    override suspend fun isBiometricAuthEnabled(): Boolean = withContext(dispatcherProvider.work) {
        diiaStorage.getBoolean(Preferences.UseTouchId, false)
    }

    override suspend fun getLastDocumentUpdate(): String? = withContext(dispatcherProvider.work) {
        val date = diiaStorage.getString(CommonPreferenceKeys.LastDocumentUpdate, DEF_STRING_VALUE)
        if (date != DEF_STRING_VALUE) date else null
    }

    override suspend fun getLastActiveDate(): String? = withContext(dispatcherProvider.work) {
        val date = diiaStorage.getString(CommonPreferenceKeys.LastActivityDate, DEF_STRING_VALUE)
        if (date != DEF_STRING_VALUE) date else null
    }
}