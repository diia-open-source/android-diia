package ua.gov.diia.diia_storage.model

import ua.gov.diia.diia_storage.model.KeyValueStore.Companion.DEFAULT_STRING_VALUE

abstract class BaseSecuredKeyValueStore : KeyValueStore {

    abstract fun allowedScopes(): Set<String>


    override fun get(key: PreferenceKey, default: Any?): Any? {
        scopeCheck(key)
        return when (key.dataType) {
            Boolean::class.java -> {
                if (containsKey(key) || default != null) {
                    return if (default == null) {
                        getBoolean(key, false)
                    } else {
                        getBoolean(key, default as Boolean)
                    }
                } else {
                    null
                }
            }
            Int::class.java -> {
                if (containsKey(key) || default != null) {
                    return if (default == null) {
                        getInt(key, 0)
                    } else {
                        getInt(key, default as Int)
                    }
                } else {
                    null
                }
            }
            Float::class.java -> {
                if (containsKey(key) || default != null) {
                    return if (default == null) {
                        getFloat(key, 0.0f)
                    } else {
                        getFloat(key, default as Float)
                    }
                } else {
                    null
                }
            }
            Long::class.java -> {
                if (containsKey(key) || default != null) {
                    return if (default == null) {
                        getLong(key, 0L)
                    } else {
                        getLong(key, default as Long)
                    }
                } else {
                    null
                }
            }
            String::class.java -> {
                if (containsKey(key) || default != null) {
                    return if (default == null) {
                        getString(key, DEFAULT_STRING_VALUE)
                    } else {
                        getString(key, default as String)
                    }
                } else {
                    null
                }
            }
            else -> {
                if (containsKey(key) || default != null) {
                    return if (default == null) {
                        getString(key, DEFAULT_STRING_VALUE)
                    } else {
                        getString(key, default as String)
                    }
                } else {
                    null
                }
            }
        }
    }

    protected open fun scopeCheck(key: PreferenceKey) {
        if (key.scope !in allowedScopes()) {
            throw IllegalAccessException("Store does not have scope : ${key.scope} for key ${key.name}")
        }
    }
}
