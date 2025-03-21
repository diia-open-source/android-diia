package ua.gov.diia.diia_storage.store

import ua.gov.diia.diia_storage.model.PreferenceKey

object Preferences {

    object Settings {
        const val NAME_DIIA = "DIIA_PREF"
    }

    object Scopes {
        const val AUTH_SCOPE = "auth"
        const val UPDATE_SCOPE = "update"
        const val USER_SCOPE = "user"
        const val PIN_SCOPE = "pin"
        const val DOUBLE_CHECK = "double_check"
        const val FAQS = "faqs"
        const val FEATURES = "features"
        const val USER_PREFERENCES = "user_preferences"
        const val QUESTIONNAIRE = "questionnaire"
        const val INVINCIBILITY_PREFERENCES = "invincibility_preferences"
    }

    const val DEF = "PREF_DEF"

    open class DoubleCheck(name: String) :
        PreferenceKey(name, Scopes.DOUBLE_CHECK, Boolean::class.java)

    object V1 : DoubleCheck("v1")
    object E1 : DoubleCheck("e1")


    open class UserDataKey(name: String, dataType: Class<*>) :
        PreferenceKey(name, Scopes.USER_SCOPE, dataType)
    object UseTouchId : UserDataKey("use_touch_id", Boolean::class.java)
    object DeepLinkFeatureKey : UserDataKey("bank_account_deep_link", String::class.java)
    object Documents : UserDataKey("documents_cache", String::class.java)
    object ITN : UserDataKey("itn", String::class.java)
    object PublicServicesCategories : UserDataKey("public_services_categories", PublicServicesCategories::class.java)
    object DiiaSign : UserDataKey("diia_sign", String::class.java)
    object DiiaSignEcdsa : UserDataKey("diia_sign_ecdsa", String::class.java)
    object DiiaSignPass : UserDataKey("diia_sign_pass", String::class.java)
    object DiiaSignPassNonce : UserDataKey("diia_sign_pass_nonce", String::class.java)
    object PromoProcessCode : UserDataKey("promoProcessCode", Int::class.java)

    open class PinKey(name: String, dataType: Class<*>) :
        PreferenceKey(name, Scopes.PIN_SCOPE, dataType)
    object PinTryCountGlobal : PinKey("pin_try_count_global", Int::class.java)
    object PinTryCountSignature : PinKey("pin_try_count_signature", Int::class.java)

    open class FaqsKey(name: String, dataType: Class<*>): PreferenceKey(name, Scopes.FAQS, dataType)
    object FaqsList: FaqsKey("faq_categories", String::class.java)

    open class FeaturesKey(name: String, dataType: Class<*>): PreferenceKey(name,
        Scopes.FEATURES, dataType)
    object Features: FeaturesKey("features", String::class.java)

    open class UserPreferenceKey(name: String, dataType: Class<*>): PreferenceKey(name,
        Scopes.USER_PREFERENCES, dataType)
    object PublicServicesListSortOrder : UserPreferenceKey("ps_list_sort_order", String::class.java)

    open class InvincibilityKey(name: String, dataType: Class<*>): PreferenceKey(name,
        Scopes.INVINCIBILITY_PREFERENCES, dataType)
    object InvincibilityRegions : InvincibilityKey("invincibility_regions", InvincibilityRegions::class.java)
    data class InvincibilityMapDownloadId(val downloadId: String) : InvincibilityKey(downloadId, Long::class.java)
    object FailedConnectionData : InvincibilityKey("failed_connection", FailedConnectionData::class.java)

    open class ContactsDataPreferenceKey(name: String, dataType: Class<String>): PreferenceKey(name, Scopes.QUESTIONNAIRE, dataType)
    object UserPhoneNumber : ContactsDataPreferenceKey("questionnaire_user_phone_number", String::class.java)
    object UserEmail : ContactsDataPreferenceKey("questionnaire_user_email", String::class.java)

}