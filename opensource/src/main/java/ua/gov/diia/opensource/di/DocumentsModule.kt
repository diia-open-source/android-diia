package ua.gov.diia.opensource.di

import android.content.Context
import androidx.lifecycle.ProcessLifecycleOwner
import androidx.lifecycle.lifecycleScope
import com.squareup.moshi.JsonAdapter
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.plus
import retrofit2.Retrofit
import retrofit2.create
import ua.gov.diia.core.di.data_source.http.AuthorizedClient
import ua.gov.diia.core.util.date.CurrentDateProvider
import ua.gov.diia.core.util.delegation.WithCrashlytics
import ua.gov.diia.core.util.event.UiDataEvent
import ua.gov.diia.core.util.extensions.context.dpToPx
import ua.gov.diia.diia_storage.DiiaStorage
import ua.gov.diia.doc_driver_license.DriverLicenceJsonAdapterDelegate
import ua.gov.diia.doc_driver_license.DriverLicenceLocalizationChecker
import ua.gov.diia.doc_driver_license.DriverLicenseFullInfoComposeMapper
import ua.gov.diia.documents.barcode.DocumentBarcodeFactory
import ua.gov.diia.documents.barcode.DocumentBarcodeRepository
import ua.gov.diia.documents.data.api.ApiDocuments
import ua.gov.diia.documents.data.datasource.local.BrokenDocFilter
import ua.gov.diia.documents.data.datasource.local.BrokenDocFilterImpl
import ua.gov.diia.documents.data.datasource.local.DefaultDocGroupUpdateBehavior
import ua.gov.diia.documents.data.datasource.local.DocGroupUpdateBehavior
import ua.gov.diia.documents.data.datasource.local.DocJsonAdapterDelegate
import ua.gov.diia.documents.data.datasource.local.DocumentsTransformation
import ua.gov.diia.documents.data.datasource.local.KeyValueDocumentsDataSource
import ua.gov.diia.documents.data.datasource.local.RemoveExpiredDocBehavior
import ua.gov.diia.documents.data.datasource.local.RemoveExpiredDocBehaviorImpl
import ua.gov.diia.documents.data.datasource.remote.NetworkDocumentsDataSource
import ua.gov.diia.documents.data.repository.BeforePublishAction
import ua.gov.diia.documents.data.repository.DocumentsDataRepository
import ua.gov.diia.documents.data.repository.DocumentsDataRepositoryImpl
import ua.gov.diia.documents.di.DocTypesAvailableToUsers
import ua.gov.diia.documents.di.GlobalActionUpdateDocument
import ua.gov.diia.documents.helper.DocumentsHelper
import ua.gov.diia.documents.models.DiiaDocument
import ua.gov.diia.documents.models.DiiaDocumentWithMetadata
import ua.gov.diia.documents.ui.BaseLocalizationChecker
import ua.gov.diia.documents.ui.DocumentComposeMapper
import ua.gov.diia.documents.ui.WithCheckLocalizationDocs
import ua.gov.diia.documents.ui.WithCheckLocalizationDocsImpl
import ua.gov.diia.documents.ui.WithPdfCertificate
import ua.gov.diia.documents.ui.WithRemoveDocument
import ua.gov.diia.documents.ui.actions.DocActionsNavigationHandler
import ua.gov.diia.documents.ui.actions.DocActionsProvider
import ua.gov.diia.documents.ui.actions.DocActionsProviderImpl
import ua.gov.diia.documents.ui.fullinfo.BaseFullInfoComposeMapper
import ua.gov.diia.documents.ui.fullinfo.DocFullInfoComposeMapper
import ua.gov.diia.documents.ui.fullinfo.DocFullInfoComposeMapperImpl
import ua.gov.diia.documents.ui.gallery.DocGalleryNavigationHelper
import ua.gov.diia.documents.util.BaseDocActionItemProcessor
import ua.gov.diia.documents.util.BaseDocumentActionProvider
import ua.gov.diia.documents.util.DocNameProvider
import ua.gov.diia.documents.util.DocumentActionMapper
import ua.gov.diia.documents.util.WithUpdateExpiredDocs
import ua.gov.diia.documents.util.WithUpdateExpiredDocsImpl
import ua.gov.diia.documents.util.datasource.ExpirationStrategy
import ua.gov.diia.opensource.data.network.api.ApiDocs
import ua.gov.diia.opensource.helper.documents.ApiDocumentsWrapper
import ua.gov.diia.opensource.helper.documents.DocActionsNavigationHandlerImpl
import ua.gov.diia.opensource.helper.documents.DocGalleryNavigationHelperImpl
import ua.gov.diia.opensource.helper.documents.DocName
import ua.gov.diia.opensource.helper.documents.DocNameProviderImpl
import ua.gov.diia.opensource.helper.documents.DocumentBarcodeRepositoryImpl
import ua.gov.diia.opensource.helper.documents.DocumentComposeMapperImpl
import ua.gov.diia.opensource.helper.documents.DocumentsHelperImpl
import ua.gov.diia.opensource.helper.documents.DriverLicenceActionProvider
import ua.gov.diia.opensource.helper.documents.WithPdfCertificateImpl
import ua.gov.diia.opensource.helper.documents.WithRemoveDocumentImpl
import java.util.concurrent.Executors
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface DocumentsModule {

    @Binds
    fun bindDocumentsHelper(impl: DocumentsHelperImpl): DocumentsHelper

    @Binds
    fun bindBrokenDocFilter(impl: BrokenDocFilterImpl): BrokenDocFilter

    @Binds
    fun bindRemoveExpiredDocBehavior(impl: RemoveExpiredDocBehaviorImpl): RemoveExpiredDocBehavior

    @Binds
    fun bindDocActionsNavHandler(impl: DocActionsNavigationHandlerImpl): DocActionsNavigationHandler

    @Binds
    fun bindDocGalleryNavHelper(impl: DocGalleryNavigationHelperImpl): DocGalleryNavigationHelper

    @Binds
    fun bindDocActionsProvider(impl: DocActionsProviderImpl): DocActionsProvider

    @Binds
    fun bindDocBarcodeRepository(impl: DocumentBarcodeRepositoryImpl): DocumentBarcodeRepository

    @Binds
    fun bindWithUpdateExpiredDocs(impl: WithUpdateExpiredDocsImpl): WithUpdateExpiredDocs

    @Binds
    fun bindWithPdfCertificate(impl: WithPdfCertificateImpl): WithPdfCertificate

    @Binds
    fun bindWithCheckLocalizationDocs(impl: WithCheckLocalizationDocsImpl): WithCheckLocalizationDocs

    @Binds
    fun bindWithRemoveDocument(impl: WithRemoveDocumentImpl): WithRemoveDocument

    @Binds
    fun bindPreviewComposeMapper(impl: DocumentComposeMapperImpl): DocumentComposeMapper

    @Binds
    fun bindDocFullComposeMapper(impl: DocFullInfoComposeMapperImpl): DocFullInfoComposeMapper

    @Binds
    fun bindDocNameProvider(impl: DocNameProviderImpl): DocNameProvider

    companion object {

        @Provides
        @Singleton
        fun provideActionItemProcessorList(): List<@JvmSuppressWildcards BaseDocActionItemProcessor> {
            return listOf()
        }

        @Provides
        @AuthorizedClient
        fun provideApiDocs(
            @AuthorizedClient retrofit: Retrofit,
        ): ApiDocs = retrofit.create()

        @Provides
        @GlobalActionUpdateDocument
        @Singleton
        fun provideActionUpdateDocument() = MutableStateFlow<UiDataEvent<DiiaDocument>?>(null)

        @Provides
        @Singleton
        fun provideDocMenuActions(
            documentActionMapper: DocumentActionMapper,
        ): List<@JvmSuppressWildcards BaseDocumentActionProvider> = listOf(
            DriverLicenceActionProvider(documentActionMapper),
        )

        @Provides
        @Singleton
        fun provideNetworkDocumentsDataSource(
            apiDocs: ApiDocuments,
            withCrashlytics: WithCrashlytics,
        ): NetworkDocumentsDataSource {
            return NetworkDocumentsDataSource(apiDocs, withCrashlytics)
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
            withCrashlytics: WithCrashlytics,
        ): KeyValueDocumentsDataSource {
            return KeyValueDocumentsDataSource(
                jsonAdapter = jsonAdapter,
                diiaStorage = diiaStorage,
                docTransformations = docTransformations,
                docTypesAvailableToUsers = docTypesAvailableToUsers,
                expirationStrategy = expirationStrategy,
                documentsHelper = documentsHelper,
                docGroupUpdateBehaviors = docGroupUpdateBehaviors,
                defaultDocGroupUpdateBehavior = defaultDocGroupUpdateBehavior,
                brokenDocFilter = brokenDocFilter,
                removeExpiredDocBehavior = removeExpiredDocBehavior,
                withCrashlytics = withCrashlytics
            )
        }

        @Provides
        @Singleton
        fun provideDocumentsDataSource(
            keyValueDataSource: KeyValueDocumentsDataSource,
            networkDocumentsDataSource: NetworkDocumentsDataSource,
            beforePublishActions: List<@JvmSuppressWildcards BeforePublishAction>,
            @DocTypesAvailableToUsers docTypesAvailableToUsers: Set<@JvmSuppressWildcards String>,
            withCrashlytics: WithCrashlytics,
        ): DocumentsDataRepository {
            val dispatcher = Executors.newSingleThreadExecutor().asCoroutineDispatcher()
            val applicationScope = ProcessLifecycleOwner.get().lifecycleScope + dispatcher
            return DocumentsDataRepositoryImpl(
                applicationScope,
                keyValueDataSource,
                networkDocumentsDataSource,
                beforePublishActions,
                docTypesAvailableToUsers,
                withCrashlytics
            )
        }


        @Provides
        @Singleton
        fun provideDocumentsTransformations(): List<@JvmSuppressWildcards DocumentsTransformation> {
            return emptyList()
        }

        @Provides
        @Singleton
        fun provideApiDocuments(
            @AuthorizedClient apiDocs: ApiDocs,
            diiaStorage: DiiaStorage,
            currentDateProvider: CurrentDateProvider,
            withCrashlytics: WithCrashlytics,
        ): ApiDocuments = ApiDocumentsWrapper(
            apiDocs,
            diiaStorage,
            currentDateProvider,
            withCrashlytics
        )

        @Provides
        @Singleton
        fun provideBeforePublishActions(
        ): List<@JvmSuppressWildcards BeforePublishAction> {
            return emptyList()
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
        fun provideDocTypesForUser(): Set<@JvmSuppressWildcards String> = setOf(
            DocName.DRIVER_LICENSE,
            DocName.TAXPAYER_CARD
        )


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
            @ApplicationContext context: Context,
        ): DocumentBarcodeFactory = DocumentBarcodeFactory(
            qrSizePx = context.dpToPx(QR_SIZE),
            ean13CodeHeight = context.dpToPx(EAN_13_H),
            ean13CodeWidth = context.dpToPx(EAN_13_W)
        )

        @Provides
        @Singleton
        fun provideLocalizationCheckers(): List<@JvmSuppressWildcards BaseLocalizationChecker> {
            return listOf(DriverLicenceLocalizationChecker())
        }

        @Provides
        @Singleton
        fun provideFullIntoComposeMapper(docComposeMapper: DocumentComposeMapper): List<@JvmSuppressWildcards BaseFullInfoComposeMapper> {
            return listOf(
                DriverLicenseFullInfoComposeMapper(docComposeMapper),
            )
        }
    }
}