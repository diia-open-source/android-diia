package ua.gov.diia.opensource.di.util

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.ElementsIntoSet
import ua.gov.diia.core.util.settings_action.SettingsActionExecutor
import ua.gov.diia.notifications.util.settings_action.PushTokenUpdateActionExecutor
import ua.gov.diia.opensource.util.settings_action.ForceUpdateActionExecutor

@Module
@InstallIn(SingletonComponent::class)
object PushServiceModule {

    @Provides
    @ElementsIntoSet
    fun provideSettingsAction(
        pushTokenUpdateAction: PushTokenUpdateActionExecutor,
        appForceUpdateAAction: ForceUpdateActionExecutor,
    ): Set<SettingsActionExecutor> {
        return setOf(pushTokenUpdateAction, appForceUpdateAAction)
    }
}