package ua.gov.diia.core.models.document

import android.os.Parcelable
import androidx.annotation.ColorRes
import androidx.annotation.Keep
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize
import ua.gov.diia.core.models.common_compose.atm.chip.ChipStatusAtm
import ua.gov.diia.core.models.common_compose.atm.text.TickerAtm
import ua.gov.diia.core.models.common_compose.mlc.list.ListItemMlc
import ua.gov.diia.core.models.common_compose.table.tableBlockPlaneOrg.TableBlockPlaneOrg
import ua.gov.diia.core.models.common_compose.table.tableBlockTwoColumnsPlaneOrg.TableBlockTwoColumnsPlaneOrg
import ua.gov.diia.core.models.document.docgroups.TaxPayerCard
import ua.gov.diia.core.models.common_compose.org.doc.DocButtonHeadingOrg
import ua.gov.diia.core.models.common_compose.org.doc.DocHeadingOrg
import ua.gov.diia.core.models.document.docgroups.v2.SubtitleLabelMlc

@Parcelize
@JsonClass(generateAdapter = true)
data class DiiaDocumentWithMetadata(
    @Json(name = "document") val diiaDocument: DiiaDocument?,
    @Json(name = "timestamp") @get:JvmName("getDocumentTimestamp") val timestamp: String,
    @Json(name = "expirationDate") var expirationDate: String,
    @Json(name = "status") @get:JvmName("getDocumentStatus") @set:JvmName("setDocumentStatus") var status: Int,
    @Json(name = "type") val type: String,
    @Json(name = "order") var order: Int = LAST_DOC_ORDER,
    @Json(name = "eTag") @get:JvmName("getDocumentETag") @set:JvmName("setDocumentETag") var eTag: String? = null,
) : Expiring,
    WithTimestamp,
    WithStatus,
    WithId,
    WithOrder,
    WithETag,
    Parcelable {

    fun setStatus(status: Int) {
        this.status = status
    }

    fun setDocExpirationDate(expirationDate: String) {
        this.expirationDate = expirationDate
    }

    override val id: String
        get() = diiaDocument?.id ?: Preferences.DEF

    override fun getStatus() = status

    override fun getTimestamp() = timestamp

    override fun getDocExpirationDate() = expirationDate

    override fun getDocOrder() = order

    override fun getETag() = eTag.orEmpty()

    override fun setNewOrder(newOrder: Int) {
        order = if (newOrder < 0) {
            LAST_DOC_ORDER
        } else {
            newOrder
        }
    }

    companion object {

        const val LAST_DOC_ORDER = Int.MAX_VALUE - 1
        const val FIRST_DOC_ORDER = Int.MIN_VALUE + 1
    }

}

interface WithOrder {

    fun getDocOrder(): Int

    fun setNewOrder(newOrder: Int)
}

interface WithSeriesAndNumber {

    fun getSeries(): String

    fun getNumber(): String

}

interface DocumentWithPhoto {

    fun getPhoto(): String?

    fun setPhoto(photo: String?) {}

    fun getLightParcelable(): DiiaDocument
}

interface WithIssueDate {
    fun getIssueDate(): String?
}

interface WithTaxpayerCard {

    fun getTaxpayerCard(): TaxPayerCard?

    fun setTaxpayerCard(taxpayerCard: TaxPayerCard)
}

interface DiiaDocumentGroup<T : DiiaDocument?> : Expiring,
    WithStatus,
    WithTimestamp, Parcelable,
    WithType, WithOrder {

    fun getData(): List<T>
}

@Keep
interface DiiaDocument : Expiring,
    WithId,
    WithDocNum,
    WithDocName,
    WithStatus,
    WithWeight,
    WithOrder,
    WithSourceType,
    Parcelable {

    fun docId() = String()

    @ColorRes
    fun getDocColor(): Int

    fun getDocStackTitle(): String

    fun getItemType(): String

    override fun equals(other: Any?): Boolean

    fun makeCopy(): DiiaDocument

    fun verificationCodesCount(): Int

    fun getPersonName(): String

    fun getDisplayDate(): String

    fun birthCertificateId(): String

    fun getExpirationDateISO(): String

    fun localization(): LocalizationType?

    fun setLocalization(code: LocalizationType)

    fun makeCopy(title: String, dateIssued: String): DiiaDocument {
        return makeCopy()
    }
}

enum class LocalizationType {
    ua, eng
}

interface Expiring {

    fun getDocExpirationDate(): String
}

interface WithTimestamp {

    fun getTimestamp(): String
}

interface WithETag {
    fun getETag(): String?
}

interface WithStatus {

    fun getStatus(): Int
}

interface WithWeight {

    fun getWeight(): Int
}

interface WithType {

    fun getItemType(): String
}

interface WithId {

    val id: String?
}

interface WithDocNum {

    fun getDocNum(): String?
}

interface WithDocName {

    fun getDocName(): String?

    fun getDocOrderLabel(): String?

    fun getDocOrderDescription(): String?
}

interface WithSourceType {
    fun getSourceType(): SourceType
}

enum class SourceType {
    STATIC, DYNAMIC
}

interface WithRegistrationPlace {
    var currentRegistrationPlaceUA: String?
}

interface WithQrCode {
    val qrCode: String?
    val qr: String?
}

interface Passport : WithRegistrationPlace, WithTaxpayerCard {

    val recordNumber: String?

}

interface WithFrontCard {
    fun getTicker(): TickerAtm?

    fun getDocHeading(): DocHeadingOrg?

    fun getDocButtonHeading(): DocButtonHeadingOrg?

    fun getTableBlockTwoColumnsPlane(): List<TableBlockTwoColumnsPlaneOrg>?

    fun getTableBlockPlaneOrg(): List<TableBlockPlaneOrg>?

    fun getSubtitleLabel(): SubtitleLabelMlc?

    fun getChipStatus(): ChipStatusAtm?
}

interface WithListItemGroupOrgPreview {

    var dataForDisplayingAsListItem: ListItemMlc?

    fun getListItemDocPreview(): ListItemMlc?

    fun setItemPreview(itemPreview: ListItemMlc)
}