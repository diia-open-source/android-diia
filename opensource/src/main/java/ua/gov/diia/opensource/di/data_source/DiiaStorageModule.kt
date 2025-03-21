package ua.gov.diia.opensource.di.data_source

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import ua.gov.diia.diia_storage.DiiaStorage
import ua.gov.diia.diia_storage.PreferenceConfiguration
import ua.gov.diia.diia_storage.SecureDiiaStorage
import ua.gov.diia.diia_storage.store.Preferences
import ua.gov.diia.notifications.store.NotificationsPreferences
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DiiaStorageModule {

    @Provides
    @Singleton
    fun provideDiiaStorage(
        @ApplicationContext context: Context
    ): DiiaStorage {
        val preferenceConfiguration = PreferenceConfiguration(
            _preferenceName = Preferences.Settings.NAME_DIIA,
            preferenceNamePrefix = "",
            allowedScopes = setOf(
                Preferences.Scopes.AUTH_SCOPE,
                Preferences.Scopes.UPDATE_SCOPE,
                Preferences.Scopes.PIN_SCOPE,
                NotificationsPreferences.Scopes.PUSH_SCOPE,
                Preferences.Scopes.USER_SCOPE,
                Preferences.Scopes.DOUBLE_CHECK,
                NotificationsPreferences.Scopes.NOTIFICATION,
                Preferences.Scopes.FAQS,
                Preferences.Scopes.FEATURES,
                Preferences.Scopes.USER_PREFERENCES,
                Preferences.Scopes.INVINCIBILITY_PREFERENCES,
                Preferences.Scopes.QUESTIONNAIRE
            )
        )
        return SecureDiiaStorage(context, preferenceConfiguration)
    }


}