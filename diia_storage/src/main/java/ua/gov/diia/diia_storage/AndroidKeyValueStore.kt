package ua.gov.diia.diia_storage

import android.annotation.SuppressLint
import android.content.SharedPreferences
import ua.gov.diia.diia_storage.model.BaseSecuredKeyValueStore
import ua.gov.diia.diia_storage.model.PreferenceKey

abstract class AndroidKeyValueStore : BaseSecuredKeyValueStore() {

    abstract fun getSharedPreferences(): SharedPreferences

    override fun getBoolean(key: PreferenceKey, defValue: Boolean): Boolean {
        scopeCheck(key)
        return getSharedPreferences().getBoolean(key.name, defValue)
    }

    override fun getInt(key: PreferenceKey, defValue: Int): Int {
        scopeCheck(key)
        return getSharedPreferences().getInt(key.name, defValue)
    }

    override fun getFloat(key: PreferenceKey, defValue: Float): Float {
        scopeCheck(key)
        return getSharedPreferences().getFloat(key.name, defValue)
    }

    override fun getLong(key: PreferenceKey, defValue: Long): Long {
        scopeCheck(key)
        return getSharedPreferences().getLong(key.name, defValue)
    }

    override fun getString(key: PreferenceKey, defValue: String): String {
        scopeCheck(key)
        return getSharedPreferences().getString(key.name, defValue) ?: defValue
    }

    override fun delete(key: PreferenceKey) {
        getSharedPreferences().edit().remove(key.name).apply()
    }

    override fun set(key: PreferenceKey, value: Any) {
        scopeCheck(key)
        when (key.dataType) {
            Boolean::class.java -> {
                getSharedPreferences().edit().putBoolean(key.name, value as Boolean).apply()
            }
            Int::class.java -> {
                getSharedPreferences().edit().putInt(key.name, value as Int).apply()
            }
            Float::class.java -> {
                getSharedPreferences().edit().putFloat(key.name, value as Float).apply()
            }
            Long::class.java -> {
                getSharedPreferences().edit().putLong(key.name, value as Long).apply()
            }
            String::class.java -> {
                getSharedPreferences().edit().putString(key.name, value as String).apply()
            }
            else -> {
                getSharedPreferences().edit().putString(key.name, value.toString()).apply()
            }
        }
    }

    override fun containsKey(key: PreferenceKey): Boolean {
        return getSharedPreferences().contains(key.name)
    }

    @SuppressLint("ApplySharedPref")
    override fun clear() {
        val preferences = getSharedPreferences()
        preferences.edit().clear().commit()
    }
}
