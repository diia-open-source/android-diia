package ua.gov.diia.notifications.ui.fragments.home.notifications.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import ua.gov.diia.notifications.ui.fragments.home.notifications.compose.NotificationsMapperCompose
import ua.gov.diia.notifications.ui.fragments.home.notifications.compose.NotificationsMapperComposeImpl

@Module
@InstallIn(ViewModelComponent::class)
interface NotificationsMapperModule {

    @Binds
    fun bindNotificationsMapperCompose(
        impl: NotificationsMapperComposeImpl
    ): NotificationsMapperCompose
}