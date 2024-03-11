package ua.gov.diia.documents.ui

import ua.gov.diia.documents.models.DiiaDocumentWithMetadata

interface BaseLocalizationChecker {
    fun checkLocalizationDocs(doc: DiiaDocumentWithMetadata): String?
}