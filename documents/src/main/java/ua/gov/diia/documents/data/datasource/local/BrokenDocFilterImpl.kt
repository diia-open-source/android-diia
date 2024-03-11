package ua.gov.diia.documents.data.datasource.local

import ua.gov.diia.documents.helper.DocumentsHelper
import ua.gov.diia.documents.models.DiiaDocumentWithMetadata
import javax.inject.Inject

class BrokenDocFilterImpl @Inject constructor(val documentsHelper: DocumentsHelper) : BrokenDocFilter {
    override fun filter(
        docs: List<DiiaDocumentWithMetadata>,
        existsId: MutableList<String?>,
        removeList: MutableList<DiiaDocumentWithMetadata>
    ) {
        docs.forEach { item ->
            if (documentsHelper.isDocCanBeBroken(item.type)) {
                val id = item.diiaDocument?.docId()
                if (id == null || id == "") {
                    removeList.add(item)
                } else {
                    existsId.add(id)
                }
            }
        }
    }
}