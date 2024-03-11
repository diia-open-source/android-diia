package ua.gov.diia.documents.models.docgroups

import com.squareup.moshi.Json
import ua.gov.diia.diia_storage.store.Preferences
import ua.gov.diia.documents.models.DiiaDocument
import ua.gov.diia.documents.models.DiiaDocumentGroup
import ua.gov.diia.documents.models.DiiaDocumentWithMetadata.Companion.LAST_DOC_ORDER

abstract class BaseDocumentGroup<T : DiiaDocument>(
    @property:Json(name = "status")
    var status: Int? = null,
    @property:Json(name = "expirationDate")
    var expirationDate: String = Preferences.DEF,
    @property:Json(name = "currentDate")
    var currentDate: String = Preferences.DEF,
    @property:Json(name = "order")
    var order: Int = LAST_DOC_ORDER
) : DiiaDocumentGroup<T> {

    override fun getStatus(): Int = status ?: 0

    override fun getTimestamp() = currentDate

    override fun getDocExpirationDate() = expirationDate

    override fun getDocOrder() = order

    override fun setNewOrder(newOrder: Int) {
        order = newOrder
    }
}
