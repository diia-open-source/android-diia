package ua.gov.diia.biometric.store

import kotlinx.coroutines.withContext
import ua.gov.diia.core.util.DispatcherProvider
import ua.gov.diia.diia_storage.store.Preferences
import ua.gov.diia.diia_storage.DiiaStorage
import javax.inject.Inject
import javax.inject.Singleton

class BiometricRepositoryImpl @Inject constructor(
    private val diiaStorage: DiiaStorage,
    private val dispatcherProvider: DispatcherProvider
): BiometricRepository {

    override suspend fun enableBiometricAuth(enable: Boolean) {
        withContext(dispatcherProvider.work) {
            diiaStorage.set(Preferences.UseTouchId, enable)
        }
    }

    override suspend fun isBiometricAuthEnabled(): Boolean = withContext(dispatcherProvider.work) {
        diiaStorage.getBoolean(Preferences.UseTouchId, false)
    }
}