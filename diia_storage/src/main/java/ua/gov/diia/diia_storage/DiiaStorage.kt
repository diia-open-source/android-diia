package ua.gov.diia.diia_storage

import ua.gov.diia.diia_storage.model.BaseSecuredKeyValueStore
import ua.gov.diia.diia_storage.model.KeyValueStore
import ua.gov.diia.diia_storage.model.PreferenceKey
import java.util.UUID

abstract class DiiaStorage : KeyValueStore, PinStore, MobileUidStore, ServiceUserUidStore {

    abstract fun isFirstLaunch(): Boolean

    abstract fun isPassed(): Boolean

    abstract fun markAppLaunched()

    abstract fun markAppPassed()

    abstract suspend fun userAuthorized(userPin: String)

    abstract suspend fun userChangePin(oldPin: String, newPin: String)

    abstract fun userLogOut()

    protected abstract val currentKeyValueStore: BaseSecuredKeyValueStore
    protected abstract var pinStorage: PinStore
    protected abstract var serviceUserUUIDStorage: ServiceUserUidStore

    override fun getMobileUuid(): String {
        val key = CommonPreferenceKeys.UUID
        val uuid = getKeyValueStoreForKey(key).getString(
            CommonPreferenceKeys.UUID,
            KeyValueStore.DEFAULT_STRING_VALUE,
        )
        return if (uuid == KeyValueStore.DEFAULT_STRING_VALUE) {
            val newUuid = UUID.randomUUID().toString()
            getKeyValueStoreForKey(key).set(CommonPreferenceKeys.UUID, newUuid)
            newUuid
        } else {
            uuid
        }
    }

    override suspend fun saveServiceUserUUID(uuid: String) {
        serviceUserUUIDStorage.saveServiceUserUUID(uuid)
    }

    override suspend fun getServiceUserUUID(): String? {
        return serviceUserUUIDStorage.getServiceUserUUID()
    }

    override suspend fun savePin(pin: String) {
        pinStorage.savePin(pin)
    }

    override suspend fun isPinValid(pinInput: String): Boolean {
        return pinStorage.isPinValid(pinInput)
    }

    override fun pinPresent(): Boolean {
        return pinStorage.pinPresent()
    }

    override fun getString(key: PreferenceKey, defValue: String): String {
        return getKeyValueStoreForKey(key).getString(key, defValue)
    }

    override fun getBoolean(key: PreferenceKey, defValue: Boolean): Boolean {
        return getKeyValueStoreForKey(key).getBoolean(key, defValue)
    }

    override fun getInt(key: PreferenceKey, defValue: Int): Int {
        return getKeyValueStoreForKey(key).getInt(key, defValue)
    }

    override fun getFloat(key: PreferenceKey, defValue: Float): Float {
        return getKeyValueStoreForKey(key).getFloat(key, defValue)
    }

    override fun getLong(key: PreferenceKey, defValue: Long): Long {
        return getKeyValueStoreForKey(key).getLong(key, defValue)
    }

    override fun set(key: PreferenceKey, value: Any) {
        getKeyValueStoreForKey(key).set(key, value)
    }

    override fun get(key: PreferenceKey, default: Any?): Any? {
        return getKeyValueStoreForKey(key).get(key, default)
    }

    override fun containsKey(key: PreferenceKey): Boolean {
        return getKeyValueStoreForKey(key).containsKey(key)
    }

    override fun clear() {
        currentKeyValueStore.clear()
    }

    protected open fun getKeyValueStoreForKey(key: PreferenceKey): KeyValueStore {
        return currentKeyValueStore
    }

}
