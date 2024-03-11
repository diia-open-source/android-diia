package ua.gov.diia.notifications.di.legacy

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ua.gov.diia.notifications.util.push.notification.AndroidNotificationEnabledChecker
import ua.gov.diia.notifications.util.push.notification.NotificationEnabledChecker

@Module
@InstallIn(SingletonComponent::class)
interface NotificationEnabledCheckerModule {

    @Binds
    fun bindChecker(impl: AndroidNotificationEnabledChecker): NotificationEnabledChecker
}