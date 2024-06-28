package ua.gov.diia.diia_storage

import android.content.Context
import ua.gov.diia.diia_storage.model.BaseSecuredKeyValueStore
import ua.gov.diia.diia_storage.model.PreferenceKey

class SecureDiiaStorage(
    context: Context,
    persistentStorageConfiguration: PreferenceConfiguration,
) : DiiaStorage() {

    private val keyValueStore = EncryptedAndroidKeyValueStore(
        context,
        persistentStorageConfiguration
    )

    override val currentKeyValueStore: BaseSecuredKeyValueStore = keyValueStore
    override var pinStorage: PinStore = currentKeyValueStore as PinStore
    override var serviceUserUUIDStorage: ServiceUserUidStore = currentKeyValueStore as ServiceUserUidStore

    override fun delete(key: PreferenceKey) {
        keyValueStore.delete(key)
    }

    override suspend fun userAuthorized(userPin: String) {
        savePin(userPin)
    }

    override fun userLogOut() {
        currentKeyValueStore.clear()
    }

    override suspend fun userChangePin(oldPin: String, newPin: String) {
        savePin(newPin)
    }

    override fun isFirstLaunch(): Boolean {
        return currentKeyValueStore.getBoolean(CommonPreferenceKeys.IsFirst, true)
    }

    override fun markAppLaunched() {
        currentKeyValueStore.set(CommonPreferenceKeys.IsFirst, false)
    }

    override fun isPassed(): Boolean {
        return currentKeyValueStore.getBoolean(CommonPreferenceKeys.IsPassed, false)
    }

    override fun markAppPassed() {
        currentKeyValueStore.set(CommonPreferenceKeys.IsPassed, true)
    }
}
