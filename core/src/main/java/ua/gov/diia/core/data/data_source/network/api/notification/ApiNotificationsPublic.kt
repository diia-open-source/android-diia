package ua.gov.diia.core.data.data_source.network.api.notification

import retrofit2.http.*
import ua.gov.diia.core.models.PushToken
import ua.gov.diia.core.models.notification.pull.message.NotificationFull
import ua.gov.diia.core.network.annotation.Analytics

interface ApiNotificationsPublic {

    @Analytics("getMessageByID")
    @GET("api/v3/notification/message/{messageId}")
    suspend fun getMessage(@Path("messageId") messageId: String): NotificationFull

    @Analytics("sendUserDevicePushToken")
    @POST("api/v1/notification/user-push-token")
    suspend fun sendDeviceUserPushToken(@Body pushToken: PushToken)

    @Analytics("sendAppStatus")
    @POST("api/v1/analytics/app-status")
    suspend fun sendAppStatus(@HeaderMap headers: Map<String, String>, @Body appStatus: ua.gov.diia.core.models.AppStatus)

    @Analytics("sendAppVersion")
    @POST("api/v1/notification/app-version")
    suspend fun sendAppVersion(@Body body: Any = Object())
}
