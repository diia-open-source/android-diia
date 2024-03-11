package ua.gov.diia.opensource.di

import androidx.work.WorkManager
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.asCoroutineDispatcher
import ua.gov.diia.core.controller.DeeplinkProcessor
import ua.gov.diia.core.controller.NotificationController
import ua.gov.diia.core.models.SingleDeeplinkProcessor
import ua.gov.diia.diia_storage.store.datasource.itn.ItnDataRepository
import ua.gov.diia.home.helper.HomeHelper
import ua.gov.diia.notifications.NotificationControllerImpl
import ua.gov.diia.notifications.helper.NotificationHelper
import ua.gov.diia.notifications.store.datasource.notifications.KeyValueNotificationDataSource
import ua.gov.diia.notifications.store.datasource.notifications.NotificationDataRepository
import ua.gov.diia.notifications.util.notification.manager.DiiaNotificationManager
import ua.gov.diia.notifications.util.notification.push.PushTokenProvider
import ua.gov.diia.opensource.data.data_source.itn.ItnDataRepositoryImpl
import ua.gov.diia.opensource.data.data_source.itn.KeyValueItnDataSource
import ua.gov.diia.opensource.data.data_source.itn.NetworkItnDataSource
import ua.gov.diia.opensource.helper.HomeHelperImpl
import ua.gov.diia.opensource.helper.NotificationHelperImpl
import ua.gov.diia.opensource.helper.PSCriminalCertHelperImpl
import ua.gov.diia.opensource.helper.PSNavigationHelperImpl
import ua.gov.diia.opensource.helper.PinHelperImpl
import ua.gov.diia.opensource.helper.PublicServiceHelperImpl
import ua.gov.diia.opensource.helper.PublicServicesCategoriesTabMapperImpl
import ua.gov.diia.opensource.helper.SplashHelperImpl
import ua.gov.diia.opensource.util.DeeplinkProcessorImpl
import ua.gov.diia.pin.helper.PinHelper
import ua.gov.diia.ps_criminal_cert.helper.PSCriminalCertHelper
import ua.gov.diia.publicservice.helper.PSNavigationHelper
import ua.gov.diia.publicservice.helper.PublicServiceHelper
import ua.gov.diia.publicservice.ui.categories.compose.PublicServicesCategoriesTabMapper
import ua.gov.diia.publicservice.ui.compose.PublicServiceCategoryDetailsComposeMapper
import ua.gov.diia.publicservice.ui.compose.PublicServiceCategoryDetailsComposeMapperImpl
import ua.gov.diia.publicservice.ui.compose.PublicServicesSearchComposeMapper
import ua.gov.diia.publicservice.ui.compose.PublicServicesSearchComposeMapperImpl
import ua.gov.diia.splash.helper.SplashHelper
import java.util.concurrent.Executors
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface FeatureModule {

    @Binds
    fun bindPinHelper(impl: PinHelperImpl): PinHelper

    @Binds
    fun bindSplashHelper(impl: SplashHelperImpl): SplashHelper

    @Binds
    fun bindNotificationHelper(impl: NotificationHelperImpl): NotificationHelper

    companion object {

        @Provides
        fun provideHomeHelper(): HomeHelper = HomeHelperImpl()

        @Provides
        fun providePublicServiceHelper(): PublicServiceHelper {
            return PublicServiceHelperImpl()
        }

        @Provides
        fun providePublicServicesTabMapper(): PublicServicesCategoriesTabMapper {
            return PublicServicesCategoriesTabMapperImpl()
        }

        @Provides
        fun providePublicServiceCategoryDetailsMapper(): PublicServiceCategoryDetailsComposeMapper {
            return PublicServiceCategoryDetailsComposeMapperImpl()
        }

        @Provides
        fun providePublicServicesSearchComposeMapper(): PublicServicesSearchComposeMapper {
            return PublicServicesSearchComposeMapperImpl()
        }

        @Provides
        fun provideDeepLinkProcessor(
        ): DeeplinkProcessor {
            val linkActionProcessors = listOf<SingleDeeplinkProcessor>()
            return DeeplinkProcessorImpl(linkActionProcessors)
        }

        @Provides
        fun provideNotificationController(
            workManager: WorkManager,
            notificationsDataSource: NotificationDataRepository,
            notificationManager: DiiaNotificationManager,
            pushTokenProvider: PushTokenProvider,
            keyValueSource: KeyValueNotificationDataSource,
        ): NotificationController {
            return NotificationControllerImpl(
                workManager = workManager,
                notificationsDataSource = notificationsDataSource,
                notificationManager = notificationManager,
                pushTokenProvider = pushTokenProvider,
                keyValueSource = keyValueSource
            )
        }

        @Provides
        @Singleton
        fun bindIntDataSource(
            network: NetworkItnDataSource,
            keyValueInt: KeyValueItnDataSource,
        ): ItnDataRepository {
            val dispatcher = Executors.newSingleThreadExecutor().asCoroutineDispatcher()
            val applicationScope = CoroutineScope(SupervisorJob() + dispatcher)
            return ItnDataRepositoryImpl(applicationScope, keyValueInt, network)
        }

        @Provides
        fun providePSCriminalCertHelper(): PSCriminalCertHelper = PSCriminalCertHelperImpl()

        @Provides
        fun providePSNavigationHelper(): PSNavigationHelper = PSNavigationHelperImpl()

    }
}