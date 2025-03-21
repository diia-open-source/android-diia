package ua.gov.diia.documents.data.datasource.local

import ua.gov.diia.core.util.extensions.date_time.getUTCDate
import ua.gov.diia.diia_storage.store.Preferences
import ua.gov.diia.documents.helper.DocumentsHelper
import ua.gov.diia.core.models.document.DiiaDocument
import ua.gov.diia.core.models.document.DiiaDocumentWithMetadata
import ua.gov.diia.core.util.date.CurrentDateProvider
import javax.inject.Inject

class RemoveExpiredDocBehaviorImpl @Inject constructor(private val currentDateProvider: CurrentDateProvider,
                                                       private val documentsHelper: DocumentsHelper) :
    RemoveExpiredDocBehavior {
    override suspend fun removeExpiredDocs(
        data: List<DiiaDocumentWithMetadata>,
        remove: suspend (DiiaDocument) -> Unit
    ) {
        data.forEach { item ->
            if (documentsHelper.isDocEligibleForDeletion(item.type)) {
                val date = item.diiaDocument?.getExpirationDateISO() ?: return
                if (date != Preferences.DEF && getUTCDate(date)?.before(
                        currentDateProvider.getDate()
                    ) == true
                ) {
                    item.diiaDocument?.let { document ->
                        remove(document)
                    }
                }
            }
        }
    }
}