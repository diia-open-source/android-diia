package ua.gov.diia.notifications.di.legacy

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.work.WorkManager
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.asCoroutineDispatcher
import ua.gov.diia.analytics.DiiaAnalytics
import ua.gov.diia.core.di.actions.GlobalActionNotificationRead
import ua.gov.diia.core.di.actions.GlobalActionNotificationReceived
import ua.gov.diia.core.util.deeplink.DeepLinkActionFactory
import ua.gov.diia.core.util.delegation.WithCrashlytics
import ua.gov.diia.core.util.event.UiDataEvent
import ua.gov.diia.core.util.event.UiEvent
import ua.gov.diia.diia_storage.DiiaStorage
import ua.gov.diia.notifications.helper.NotificationHelper
import ua.gov.diia.notifications.models.notification.pull.PullNotification
import ua.gov.diia.notifications.service.PushService
import ua.gov.diia.notifications.store.datasource.notifications.KeyValueNotificationDataSource
import ua.gov.diia.notifications.store.datasource.notifications.KeyValueNotificationDataSourceImpl
import ua.gov.diia.notifications.store.datasource.notifications.NetworkNotificationDataSource
import ua.gov.diia.notifications.store.datasource.notifications.NotificationDataRepository
import ua.gov.diia.notifications.store.datasource.notifications.NotificationDataRepositoryImpl
import ua.gov.diia.notifications.util.notification.manager.DiiaAndroidNotificationManager
import ua.gov.diia.notifications.util.notification.manager.DiiaNotificationManager
import java.util.concurrent.Executors
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NotificationDataSourceModule {

    @Provides
    @Singleton
    fun provideListPullNotificationJsonAdapter(): JsonAdapter<List<PullNotification>> {
        val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
        return moshi.adapter(
            Types.newParameterizedType(
                MutableList::class.java,
                PullNotification::class.java
            )
        )
    }

    @Provides
    @Singleton
    fun bind(
        keyValueStore: DiiaStorage,
        withCrashlytics: WithCrashlytics,
        jsonAdapter: JsonAdapter<List<PullNotification>>
    ): KeyValueNotificationDataSource {
        return KeyValueNotificationDataSourceImpl(keyValueStore, withCrashlytics, jsonAdapter)
    }

    @Provides
    @Singleton
    fun bindNotificationDataRepo(
        keyValueSource: KeyValueNotificationDataSource,
        diiaNotificationManager: DiiaNotificationManager,
        networkSource: NetworkNotificationDataSource,
        @GlobalActionNotificationRead actionNotificationRead: MutableLiveData<UiDataEvent<String>>,
        withCrashlytics: WithCrashlytics
    ): NotificationDataRepository {
        val dispatcher = Executors.newSingleThreadExecutor().asCoroutineDispatcher()
        val applicationScope = CoroutineScope(SupervisorJob() + dispatcher)
        return NotificationDataRepositoryImpl(
            applicationScope,
            keyValueSource,
            diiaNotificationManager,
            networkSource,
            actionNotificationRead,
            withCrashlytics
        )
    }

    @Provides
    fun providePushService(
        @ApplicationContext context: Context,
        notificationHelper: NotificationHelper,
        deepLinkActionFactory: DeepLinkActionFactory,
        @GlobalActionNotificationReceived globalActionNotificationReceived: MutableLiveData<UiEvent>,
        diiaAndroidNotificationManager: DiiaAndroidNotificationManager,
        analytics: DiiaAnalytics,
        diiaStorage: DiiaStorage,
        workManager: WorkManager
    ): PushService {
        return PushService(
            context,
            notificationHelper,
            deepLinkActionFactory,
            analytics,
            globalActionNotificationReceived,
            diiaStorage,
            workManager,
            diiaAndroidNotificationManager
        )
    }
}