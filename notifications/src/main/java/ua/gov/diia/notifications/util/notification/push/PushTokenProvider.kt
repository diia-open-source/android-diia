package ua.gov.diia.notifications.util.notification.push

interface PushTokenProvider {

    fun requestCurrentPushToken(forceRefresh: Boolean = true): String

}