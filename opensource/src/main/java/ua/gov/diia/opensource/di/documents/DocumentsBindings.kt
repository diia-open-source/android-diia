package ua.gov.diia.opensource.di.documents

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ua.gov.diia.core.models.document.barcode.DocumentBarcodeRepository
import ua.gov.diia.documents.data.datasource.local.BrokenDocFilter
import ua.gov.diia.documents.data.datasource.local.BrokenDocFilterImpl
import ua.gov.diia.documents.data.datasource.local.RemoveExpiredDocBehavior
import ua.gov.diia.documents.data.datasource.local.RemoveExpiredDocBehaviorImpl
import ua.gov.diia.documents.ui.WithCheckLocalizationDocs
import ua.gov.diia.documents.ui.WithCheckLocalizationDocsImpl
import ua.gov.diia.documents.ui.WithPdfCertificate
import ua.gov.diia.documents.ui.WithPdfDocument
import ua.gov.diia.documents.ui.WithRemoveDocument
import ua.gov.diia.documents.ui.actions.DocActionsNavigationHandler
import ua.gov.diia.documents.ui.actions.DocActionsProvider
import ua.gov.diia.documents.ui.actions.DocActionsProviderImpl
import ua.gov.diia.documents.ui.fullinfo.DocFullInfoComposeMapper
import ua.gov.diia.documents.ui.fullinfo.DocFullInfoComposeMapperImpl
import ua.gov.diia.documents.ui.gallery.DocGalleryNavigationHelper
import ua.gov.diia.documents.util.DocNameProvider
import ua.gov.diia.documents.util.WithUpdateExpiredDocs
import ua.gov.diia.documents.util.WithUpdateExpiredDocsImpl
import ua.gov.diia.documents.verificationdata.DocumentVerificationDataRepository
import ua.gov.diia.opensource.util.documents.DocActionsNavigationHandlerImpl
import ua.gov.diia.opensource.util.documents.DocGalleryNavigationHelperImpl
import ua.gov.diia.opensource.util.documents.DocNameProviderImpl
import ua.gov.diia.opensource.util.documents.DocNameVerificationMapper
import ua.gov.diia.opensource.util.documents.DocNameVerificationMapperImpl
import ua.gov.diia.opensource.util.documents.DocumentVerificationDataRepositoryImpl
import ua.gov.diia.opensource.util.documents.WithPdfCertificateImpl
import ua.gov.diia.opensource.util.documents.WithPdfDocumentImpl
import ua.gov.diia.opensource.util.documents.WithRemoveDocumentImpl
import ua.gov.diia.opensource.util.documents.barcode.DocumentBarcodeRepositoryImpl

@Module
@InstallIn(SingletonComponent::class)
interface DocumentsBindings {

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
    fun bindDocumentVerificationDataRepository(impl: DocumentVerificationDataRepositoryImpl): DocumentVerificationDataRepository

    @Binds
    fun bindWithUpdateExpiredDocs(impl: WithUpdateExpiredDocsImpl): WithUpdateExpiredDocs

    @Binds
    fun bindWithPdfCertificate(impl: WithPdfCertificateImpl): WithPdfCertificate

    @Binds
    fun bindWithPdfDocument(impl: WithPdfDocumentImpl): WithPdfDocument

    @Binds
    fun bindWithCheckLocalizationDocs(impl: WithCheckLocalizationDocsImpl): WithCheckLocalizationDocs

    @Binds
    fun bindWithRemoveDocument(impl: WithRemoveDocumentImpl): WithRemoveDocument

    @Binds
    fun bindDocFullComposeMapper(impl: DocFullInfoComposeMapperImpl): DocFullInfoComposeMapper

    @Binds
    fun bindDocNameProvider(impl: DocNameProviderImpl): DocNameProvider

    @Binds
    fun bindDocNameMapper(
        impl: DocNameVerificationMapperImpl
    ): DocNameVerificationMapper

}