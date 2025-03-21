package ua.gov.diia.documents.verificationdata

import ua.gov.diia.core.models.document.DiiaDocument
import ua.gov.diia.core.models.document.LocalizationType

interface DocumentVerificationDataRepository {
    suspend fun loadVerificationData(
        doc: DiiaDocument,
        position: Int,
        fullInfo: Boolean = false,
        localizationType: LocalizationType = LocalizationType.ua
    ): DocumentVerificationDataRepositoryResult?
}

data class DocumentVerificationDataRepositoryResult(
    val result: DocumentVerificationDataResult
)