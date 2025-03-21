package ua.gov.diia.opensource.util.documents

import ua.gov.diia.core.di.data_source.http.AuthorizedClient
import ua.gov.diia.core.models.document.DiiaDocument
import ua.gov.diia.core.models.document.LocalizationType
import ua.gov.diia.documents.verificationdata.DocumentVerificationDataFactory
import ua.gov.diia.documents.verificationdata.DocumentVerificationDataRepository
import ua.gov.diia.documents.verificationdata.DocumentVerificationDataRepositoryResult
import ua.gov.diia.documents.verificationdata.DocumentVerificationDataResult
import ua.gov.diia.opensource.data.data_source.network.api.ApiDocs
import ua.gov.diia.ui_base.components.organism.document.Localization
import ua.gov.diia.ui_base.components.organism.document.VerificationCodesOrgData
import ua.gov.diia.ui_base.components.organism.document.toUIModel
import javax.inject.Inject

class DocumentVerificationDataRepositoryImpl @Inject constructor(
    @AuthorizedClient private val apiDocs: ApiDocs,
    private val docNameVerificationMapper: DocNameVerificationMapper,
    private val documentVerificationDataFactory: DocumentVerificationDataFactory,
) : DocumentVerificationDataRepository {
    override suspend fun loadVerificationData(
        doc: DiiaDocument,
        position: Int,
        fullInfo: Boolean,
        localizationType: LocalizationType
    ): DocumentVerificationDataRepositoryResult? {
        val result: DocumentVerificationDataResult = try {
            val docType =
                if (fullInfo) docNameVerificationMapper.mapDocFullInfo(doc) else docNameVerificationMapper.mapDocGeneral(
                    doc
                )
            when (docType) {

                else -> {
                    val apiResult = when (docType) {

                        else -> {
                            apiDocs.getVerificationCodesOrg(
                                documentType = docType,
                                documentId = doc.id,
                                localization = localizationType.name
                            ).verificationCodesOrg
                        }
                    }
                    val verificationCodesOrgData: VerificationCodesOrgData? = apiResult?.toUIModel(
                        idle = false,
                        localization = when (localizationType) {
                            LocalizationType.ua -> Localization.ua
                            LocalizationType.eng -> Localization.eng
                        }
                    )
                    verificationCodesOrgData?.let {
                        return@let DocumentVerificationDataResult.DocumentVerificationDataSuccessfulLoadResult(
                            verificationCodesOrgData = verificationCodesOrgData
                        )
                    } ?: DocumentVerificationDataResult.DocumentVerificationDataErrorLoadResult(
                        verificationCodesOrgData = documentVerificationDataFactory.getVerificationCodeOrgExceptionState(
                            localization = localizationType
                        ),
                        exception = java.lang.Exception("")
                    )
                }
            }
        } catch (e: Exception) {
            DocumentVerificationDataResult.DocumentVerificationDataErrorLoadResult(
                verificationCodesOrgData = documentVerificationDataFactory.getVerificationCodeOrgExceptionState(
                    localization = localizationType
                ),
                exception = e
            )
        }
        return DocumentVerificationDataRepositoryResult(result)
    }
}