package ua.gov.diia.notifications.di.legacy

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import ua.gov.diia.notifications.util.notification.manager.DiiaAndroidNotificationManager
import ua.gov.diia.notifications.util.notification.manager.DiiaNotificationManager

@ExperimentalCoroutinesApi
@FlowPreview
@Module
@InstallIn(SingletonComponent::class)
interface NotificationManagementModule {

    @Binds
    fun bindNotificationManager(impl: DiiaAndroidNotificationManager): DiiaNotificationManager
}