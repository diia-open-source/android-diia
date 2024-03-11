package ua.gov.diia.documents.ui

import ua.gov.diia.documents.models.DiiaDocumentWithMetadata

interface WithCheckLocalizationDocs {

    /**
     * performs documents localization check and runs update for documents with outdated locale
     */
    fun checkLocalizationDocs(
        docs: List<DiiaDocumentWithMetadata>?,
        updateDocs: (List<String>) -> Unit
    )
}