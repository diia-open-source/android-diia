package ua.gov.diia.core.models.document

interface BaseLocalizationChecker {
    fun checkLocalizationDocs(doc: DiiaDocumentWithMetadata): String?
}