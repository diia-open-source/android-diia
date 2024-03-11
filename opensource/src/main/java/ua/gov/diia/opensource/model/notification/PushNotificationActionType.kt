package ua.gov.diia.opensource.model.notification

enum class PushNotificationActionType(val id: String) {
    APP_SESSIONS("newDeviceConnecting"),
    MESSAGE("message"),
    DOCUMENT_VIEW("documents/");

    companion object {
        fun fromId(id: String): PushNotificationActionType? {
            return values().find { e -> id.startsWith(e.id) }
        }
    }

}