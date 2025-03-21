package ua.gov.diia.documents.util

import ua.gov.diia.core.models.document.DiiaDocument

interface DocNameProvider {
    /**
     * @return string name for specific document
     */
    fun getDocumentName(document: DiiaDocument): String
}