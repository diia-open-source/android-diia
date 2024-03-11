package ua.gov.diia.diia_storage.model

interface KeyValueStore {

    fun getString(key: PreferenceKey, defValue: String): String

    fun getBoolean(key: PreferenceKey, defValue: Boolean): Boolean

    fun getInt(key: PreferenceKey, defValue: Int): Int

    fun getFloat(key: PreferenceKey, defValue: Float): Float

    fun getLong(key: PreferenceKey, defValue: Long): Long

    fun set(key: PreferenceKey, value: Any)

    fun get(key: PreferenceKey, default: Any? = null): Any?

    fun containsKey(key: PreferenceKey): Boolean

    fun delete(key: PreferenceKey)

    fun clear()

    companion object {
        const val DEFAULT_STRING_VALUE = "PREF_DEF"
    }
}

