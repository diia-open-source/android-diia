package ua.gov.diia.documents.ui

import ua.gov.diia.core.models.document.BaseLocalizationChecker
import ua.gov.diia.core.models.document.DiiaDocumentWithMetadata
import javax.inject.Inject

class WithCheckLocalizationDocsImpl @Inject constructor(
    private val localizationCheckers: List<@JvmSuppressWildcards BaseLocalizationChecker>): WithCheckLocalizationDocs {
    override fun checkLocalizationDocs(
        docs: List<DiiaDocumentWithMetadata>?,
        updateDocs: (List<String>) -> Unit
    ) {
        val updateList = mutableListOf<String>()
        docs?.forEach { diiaDoc ->
            localizationCheckers.forEach {
                it.checkLocalizationDocs(diiaDoc)?.let { doc ->
                    updateList.add(doc)
                }
            }
        }
        if (updateList.size > 0) {
            updateDocs(updateList)
        }
    }
}