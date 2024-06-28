package ua.gov.diia.notifications.data.data_source.network.api.notification

import retrofit2.http.*
import ua.gov.diia.core.models.notification.pull.message.NotificationFull
import ua.gov.diia.core.network.annotation.Analytics
import ua.gov.diia.notifications.models.notification.SubscribeResponse
import ua.gov.diia.notifications.models.notification.Subscriptions
import ua.gov.diia.notifications.models.notification.TemplateDialogModelWithProcessCode
import ua.gov.diia.notifications.models.notification.pull.PullNotificationsResponse
import ua.gov.diia.notifications.models.notification.pull.PullNotificationsToModify
import ua.gov.diia.notifications.models.notification.pull.UpdatePullNotificationResponse

interface ApiNotifications {

    @Analytics("getNotifications")
    @GET("api/v3/notification/notifications")
    suspend fun getNotifications(@Query("skip") skip: Int): PullNotificationsResponse

    @Analytics("getNotificationByID")
    @GET("api/v4/notification/notification/{notificationId}")
    suspend fun getPullNotification(@Path("notificationId") notificationId: String): NotificationFull

    @Analytics("getNotificationByMessageId")
    @GET("api/v4/notification/message/{messageId}")
    suspend fun getNotificationByMessageId(@Path("messageId") messageId: String?): NotificationFull

    @Analytics("markNotificationsAsRead")
    @PUT("api/v2/notification/notifications/read")
    suspend fun markNotificationsAsRead(@Body pullNotificationsToModify: PullNotificationsToModify): UpdatePullNotificationResponse

    @Analytics("deleteNotifications")
    @PUT("api/v2/notification/notifications/delete")
    suspend fun deleteNotifications(@Body pullNotificationsToModify: PullNotificationsToModify): UpdatePullNotificationResponse

    @Analytics("getSubscriptions")
    @GET("api/v1/user/subscriptions")
    suspend fun getSubscriptions(): Subscriptions

    @Analytics("subscribe")
    @POST("api/v1/user/subscription/{code}")
    suspend fun subscribe(@Path("code") code: String): SubscribeResponse

    @Analytics("unsubscribe")
    @DELETE("api/v1/user/subscription/{code}")
    suspend fun unsubscribe(@Path("code") code: String): SubscribeResponse

    @Analytics("refuseNacpDeclarantRelatives")
    @POST("api/v1/public-service/nacp-declarant-relatives/{requestId}/refuse")
    suspend fun refuseNacpDeclarantRelatives(
        @Path("requestId") requestId: String,
        @Query("force") force: Boolean
    ): TemplateDialogModelWithProcessCode

    @Analytics("confirmNacpDeclarantRelatives")
    @POST("api/v1/public-service/nacp-declarant-relatives/{requestId}/confirm")
    suspend fun confirmNacpDeclarantRelatives(
        @Path("requestId") requestId: String,
    ): TemplateDialogModelWithProcessCode
}
