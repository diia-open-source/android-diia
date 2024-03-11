package ua.gov.diia.notifications.util.push

import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import ua.gov.diia.core.models.notification.push.PushNotification

class MoshiPushParser : PushParser {

    private val moshi: Moshi = Moshi.Builder().build()

    private fun <T> parseJson(data: String, type: Class<T>): T? {
        val adapter: JsonAdapter<T> = moshi.adapter(type)
        return adapter.fromJson(data)
    }

    override fun parsePushNotification(pushJson: String): PushNotification? {
        return parseJson(pushJson, PushNotification::class.java)
    }

}