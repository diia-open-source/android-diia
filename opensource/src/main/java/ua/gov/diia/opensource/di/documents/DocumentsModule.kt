package ua.gov.diia.opensource.di.documents

import android.content.Context
import com.squareup.moshi.JsonAdapter
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.asCoroutineDispatcher
import okhttp3.OkHttpClient
import ua.gov.diia.core.di.data_source.http.AuthorizedClient
import ua.gov.diia.core.di.data_source.http.UnauthorizedClient
import ua.gov.diia.core.models.document.BaseLocalizationChecker
import ua.gov.diia.core.models.document.DiiaDocument
import ua.gov.diia.core.models.document.DiiaDocumentWithMetadata
import ua.gov.diia.core.models.document.DocJsonAdapterDelegate
import ua.gov.diia.core.models.document.barcode.DocumentBarcodeFactory
import ua.gov.diia.core.util.alert.ClientAlertDialogsFactory
import ua.gov.diia.core.util.date.CurrentDateProvider
import ua.gov.diia.core.util.delegation.WithCrashlytics
import ua.gov.diia.core.util.extensions.context.dpToPx
import ua.gov.diia.core.util.share.ShareHelper
import ua.gov.diia.diia_storage.DiiaStorage
import ua.gov.diia.doc_driver_license.models.DocName.DRIVER_LICENSE
import ua.gov.diia.doc_driver_license.utils.DriverLicenceActionProvider
import ua.gov.diia.doc_driver_license.utils.DriverLicenceJsonAdapterDelegate
import ua.gov.diia.doc_driver_license.utils.DriverLicenceLocalizationChecker
import ua.gov.diia.doc_driver_license.utils.DriverLicenseFullInfoComposeMapper
import ua.gov.diia.documents.data.api.ApiDocuments
import ua.gov.diia.documents.data.datasource.local.BrokenDocFilter
import ua.gov.diia.documents.data.datasource.local.DefaultDocGroupUpdateBehavior
import ua.gov.diia.documents.data.datasource.local.DocGroupUpdateBehavior
import ua.gov.diia.documents.data.datasource.local.DocumentsTransformation
import ua.gov.diia.documents.data.datasource.local.KeyValueDocumentsDataSource
import ua.gov.diia.documents.data.datasource.local.RemoveExpiredDocBehavior
import ua.gov.diia.documents.data.datasource.remote.NetworkDocumentsDataSource
import ua.gov.diia.documents.data.repository.BeforePublishAction
import ua.gov.diia.documents.data.repository.DocumentsDataRepository
import ua.gov.diia.documents.data.repository.DocumentsDataRepositoryImpl
import ua.gov.diia.documents.di.DocTypesAvailableToUsers
import ua.gov.diia.documents.helper.DocumentsHelper
import ua.gov.diia.documents.util.datasource.ExpirationStrategy
import ua.gov.diia.documents.verificationdata.DocumentVerificationDataFactory
import ua.gov.diia.opensource.data.data_source.network.api.ApiDocs
import ua.gov.diia.opensource.helper.DocumentsHelperImpl
import ua.gov.diia.opensource.util.store.docs.api.ApiDocumentsWrapper
import ua.gov.diia.ui_base.mappers.document.BaseDocumentActionProvider
import ua.gov.diia.ui_base.mappers.document.BaseFullInfoComposeMapper
import ua.gov.diia.ui_base.mappers.document.DocumentComposeMapper
import java.util.concurrent.Executors
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DocumentsModule {

    @Provides
    @Singleton
    fun provideNetworkDocumentsDataSource(
        apiDocs: ApiDocuments,
        withCrashlytics: WithCrashlytics
    ): NetworkDocumentsDataSource {
        return NetworkDocumentsDataSource(apiDocs, withCrashlytics)
    }

    @Provides
    @Singleton
    fun provideLocalizationCheckers(): List<@JvmSuppressWildcards BaseLocalizationChecker> {
        return listOf(DriverLicenceLocalizationChecker())
    }

    @Provides
    fun provideDocumentsHelper(
        shareHelper: ShareHelper,
        @UnauthorizedClient okhttp: OkHttpClient,
        clientAlertDialogsFactory: ClientAlertDialogsFactory,
    ): DocumentsHelper {
        return DocumentsHelperImpl(
            shareHelper,
            okhttp,
            clientAlertDialogsFactory
        )
    }

    @Provides
    @Singleton
    fun provideFullIntoComposeMapper(docComposeMapper: DocumentComposeMapper): List<@JvmSuppressWildcards BaseFullInfoComposeMapper> {
        return listOf(
            DriverLicenseFullInfoComposeMapper(docComposeMapper),
        )
    }

    @Provides
    @Singleton
    fun provideDocMenuActions(): List<@JvmSuppressWildcards BaseDocumentActionProvider> {
        return listOf(
            DriverLicenceActionProvider(),
        )
    }

    @Provides
    @Singleton
    fun provideKeyValueDocumentsDataSource(
        diiaStorage: DiiaStorage,
        jsonAdapter: JsonAdapter<List<@JvmSuppressWildcards DiiaDocumentWithMetadata>>,
        docTransformations: List<@JvmSuppressWildcards DocumentsTransformation>,
        @DocTypesAvailableToUsers docTypesAvailableToUsers: Set<@JvmSuppressWildcards String>,
        expirationStrategy: ExpirationStrategy,
        documentsHelper: DocumentsHelper,
        docGroupUpdateBehaviors: List<@JvmSuppressWildcards DocGroupUpdateBehavior>,
        defaultDocGroupUpdateBehavior: DefaultDocGroupUpdateBehavior,
        brokenDocFilter: BrokenDocFilter,
        removeExpiredDocBehavior: RemoveExpiredDocBehavior,
        withCrashlytics: WithCrashlytics
    ): KeyValueDocumentsDataSource {
        return KeyValueDocumentsDataSource(
            jsonAdapter,
            diiaStorage,
            docTransformations,
            docTypesAvailableToUsers,
            expirationStrategy,
            documentsHelper,
            docGroupUpdateBehaviors,
            defaultDocGroupUpdateBehavior,
            brokenDocFilter,
            removeExpiredDocBehavior,
            withCrashlytics
        )
    }

    @Provides
    @Singleton
    fun provideDocumentsTransformations(
    ): List<@JvmSuppressWildcards DocumentsTransformation> {
        return listOf()
    }

    @Provides
    @Singleton
    fun provideDocumentsDataSource(
        keyValueDataSource: KeyValueDocumentsDataSource,
        networkDocumentsDataSource: NetworkDocumentsDataSource,
        beforePublishActions: List<@JvmSuppressWildcards BeforePublishAction>,
        @DocTypesAvailableToUsers docTypesAvailableToUsers: Set<@JvmSuppressWildcards String>,
        withCrashlytics: WithCrashlytics,
        documentsHelper: DocumentsHelper,
    ): DocumentsDataRepository {
        val dispatcher =
            Executors.newSingleThreadExecutor().asCoroutineDispatcher()
        val applicationScope = CoroutineScope(SupervisorJob() + dispatcher)
        return DocumentsDataRepositoryImpl(
            applicationScope,
            keyValueDataSource,
            networkDocumentsDataSource,
            beforePublishActions,
            docTypesAvailableToUsers,
            withCrashlytics,
            documentsHelper
        )
    }

    @Provides
    @Singleton
    fun provideApiDocuments(
        @AuthorizedClient apiDocs: ApiDocs,
        diiaStorage: DiiaStorage,
        currentDateProvider: CurrentDateProvider,
        withCrashlytics: WithCrashlytics
    ): ApiDocuments {
        return ApiDocumentsWrapper(
            apiDocs,
            diiaStorage,
            currentDateProvider,
            withCrashlytics
        )
    }

    @Provides
    @Singleton
    fun provideBeforePublishActions(
    ): List<@JvmSuppressWildcards BeforePublishAction> {
        return listOf()
    }

    @Provides
    @Singleton
    fun provideDocDelegates(): List<DocJsonAdapterDelegate<out DiiaDocument>> {
        return listOf(
            DriverLicenceJsonAdapterDelegate(),
        )
    }

    @Provides
    @Singleton
    @DocTypesAvailableToUsers
    fun provideDocTypesForUser(): Set<@JvmSuppressWildcards String> {
        return setOf(
            DRIVER_LICENSE,
        )
    }

    @Provides
    @Singleton
    fun provideDocGroupUpdateBehaviors(): List<@JvmSuppressWildcards DocGroupUpdateBehavior> {
        return listOf()
    }

    private const val QR_SIZE = 218F
    private const val EAN_13_H = 36F
    private const val EAN_13_W = 218F

    @Provides
    fun provideBarcodeFactory(
        @ApplicationContext context: Context
    ): DocumentBarcodeFactory = DocumentBarcodeFactory(
        qrSizePx = context.dpToPx(QR_SIZE),
        ean13CodeHeight = context.dpToPx(EAN_13_H),
        ean13CodeWidth = context.dpToPx(EAN_13_W)
    )

    @Provides
    fun provideDocumentVerificationDataFactory(): DocumentVerificationDataFactory =
        DocumentVerificationDataFactory()
}