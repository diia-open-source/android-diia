package ua.gov.diia.opensource.di.actions

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ua.gov.diia.core.push.BasePushNotificationAction
import ua.gov.diia.notifications.action.DocumentSharingPushNotificationAction
import ua.gov.diia.notifications.action.PushAccessibilityNotificationAction

@Module
@InstallIn(SingletonComponent::class)
class PushNotificationActionModule {

    @Provides
    fun providePushActionTypes(): List<@JvmSuppressWildcards BasePushNotificationAction> {
        return listOf(
            DocumentSharingPushNotificationAction(),
            PushAccessibilityNotificationAction()
        )
    }
}