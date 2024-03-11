package ua.gov.diia.pin.repository

import kotlinx.coroutines.withContext
import ua.gov.diia.core.util.DispatcherProvider
import ua.gov.diia.diia_storage.store.Preferences
import ua.gov.diia.diia_storage.DiiaStorage
import javax.inject.Inject

class LoginPinRepositoryImpl @Inject constructor(
    private val dispatcherProvider: DispatcherProvider,
    private val storage: DiiaStorage
) : LoginPinRepository {

    override suspend fun setUserAuthorized(pin: String) {
        withContext(dispatcherProvider.work) {
            storage.userAuthorized(pin)
        }
    }

    override suspend fun isPinValid(pin: String): Boolean = withContext(dispatcherProvider.work) {
        storage.isPinValid(pin)
    }

    override suspend fun isPinPresent(): Boolean = withContext(dispatcherProvider.work) {
        storage.pinPresent()
    }

    override suspend fun getPinTryCount(): Int = withContext(dispatcherProvider.work) {
        storage.getInt(Preferences.PinTryCountGlobal, 0)
    }

    override suspend fun setPinTryCount(count: Int) = withContext(dispatcherProvider.work) {
        storage.set(Preferences.PinTryCountGlobal, count)
    }
}