package ua.gov.diia.core.models.document.docgroups

import com.squareup.moshi.Json
import ua.gov.diia.core.models.document.DiiaDocument
import ua.gov.diia.core.models.document.DiiaDocumentGroup
import ua.gov.diia.core.models.document.DiiaDocumentWithMetadata.Companion.LAST_DOC_ORDER
import ua.gov.diia.core.models.document.Preferences
import ua.gov.diia.core.models.document.WithETag

abstract class BaseDocumentGroup<T : DiiaDocument>(
    @property:Json(name = "status")
    var status: Int? = null,
    @property:Json(name = "expirationDate")
    var expirationDate: String = Preferences.DEF,
    @property:Json(name = "currentDate")
    var currentDate: String = Preferences.DEF,
    @property:Json(name = "order")
    var order: Int = LAST_DOC_ORDER,
    @property:Json(name = "eTag")
    @get:JvmName("getDocumentETag") @set:JvmName("setDocumentETag") var eTag: String? = null
) : DiiaDocumentGroup<T>, WithETag {

    override fun getStatus(): Int = status ?: 0

    override fun getTimestamp() = currentDate

    override fun getDocExpirationDate() = expirationDate

    override fun getDocOrder() = order

    override fun setNewOrder(newOrder: Int) {
        order = newOrder
    }

    override fun getETag(): String? {
        return eTag
    }
}
