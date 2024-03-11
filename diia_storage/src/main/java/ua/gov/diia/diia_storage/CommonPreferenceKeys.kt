package ua.gov.diia.diia_storage

import ua.gov.diia.diia_storage.CommonPreferenceKeys.CommonScopes.AUTH_SCOPE
import ua.gov.diia.diia_storage.model.PreferenceKey

object CommonPreferenceKeys {

    object CommonScopes {
        const val HASH_SCOPE = "hash"
        const val AUTH_SCOPE = "auth"
    }

    open class HashKey(name: String) :
        PreferenceKey(name, CommonScopes.HASH_SCOPE, String::class.java)

    open class AuthKey(name: String, dataType: Class<*>) :
        PreferenceKey(name, AUTH_SCOPE, dataType)

    object LastActivityDate : AuthKey("last_activity_date", String::class.java)
    object LastDocumentUpdate : AuthKey("last_document_update", String::class.java)
    object CurrentAppVersion : AuthKey("current_version_code", Int::class.java)

    object Token : AuthKey("token", String::class.java)
    object UUID : AuthKey("uuid", String::class.java)
    object IsFirst : AuthKey("is_first", Boolean::class.java)
    object IsPassed : AuthKey("is_passed", Boolean::class.java)
}
