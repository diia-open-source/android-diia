package ua.gov.diia.notifications.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ua.gov.diia.notifications.util.notification.push.CloudPushTokenProvider
import ua.gov.diia.notifications.util.notification.push.PushTokenProvider

@Module
@InstallIn(SingletonComponent::class)
interface PushTokenProviderModule {

    @Binds
    fun bindTokenProvider(impl: CloudPushTokenProvider): PushTokenProvider
}