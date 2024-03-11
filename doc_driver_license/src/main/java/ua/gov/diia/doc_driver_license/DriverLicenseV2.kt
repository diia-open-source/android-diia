package ua.gov.diia.doc_driver_license

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize
import ua.gov.diia.documents.models.docgroups.BaseDocumentGroup
import ua.gov.diia.core.models.common_compose.atm.chip.ChipStatusAtm
import ua.gov.diia.core.network.Http
import ua.gov.diia.documents.models.*
import ua.gov.diia.core.models.common_compose.atm.text.TickerAtm
import ua.gov.diia.documents.models.DiiaDocumentWithMetadata.Companion.LAST_DOC_ORDER
import ua.gov.diia.documents.models.docgroups.v2.*
import ua.gov.diia.core.models.common_compose.table.tableBlockTwoColumnsPlaneOrg.TableBlockTwoColumnsPlaneOrg
import ua.gov.diia.core.models.common_compose.table.tableBlockPlaneOrg.TableBlockPlaneOrg

@Parcelize
@JsonClass(generateAdapter = true)
class DriverLicenseV2(
    @Json(name = "data")
    internal val data: List<Data> = listOf(),
) : BaseDocumentGroup<DriverLicenseV2.Data>() {

    override fun getItemType() = DriverLicenseConst.NAME

    @Parcelize
    @JsonClass(generateAdapter = true)
    data class Data(
        @Json(name = "dataForDisplayingInOrderConfigurations")
        val dataForDisplayingInOrderConfigurations: DataForDisplayingInOrderConfigurations?,
        @Json(name = "content")
        val content: List<Content>?,
        @Json(name = "docData")
        val docData: DocData?,
        @Json(name = "docNumber")
        val docNumber: String?,
        @Json(name = "docStatus")
        internal var docStatus: Int,
        @Json(name = "frontCard")
        val frontCard: FrontCard,
        @Json(name = "fullInfo")
        val fullInfo: List<FullInfo>?,
        @Json(name = "id")
        override val id: String?,
        @Json(name = "localization")
        internal var localization: LocalizationType?,
        @Json(name = "expirationDate")
        internal val expirationDate: String = "",
        @Json(name = "shareLocalization")
        internal var shareLocalization: LocalizationType?,
        @Json(name = "order")
        internal var order: Int = LAST_DOC_ORDER
    ) : DiiaDocument, DocumentWithPhoto, WithFrontCard {

        @Parcelize
        @JsonClass(generateAdapter = true)
        data class DocData(
            @Json(name = "docName")
            val docName: String?,
            @Json(name = "birthday")
            val birthday: String?,
            @Json(name = "category")
            val category: String?,
            @Json(name = "fullName")
            val fullName: String?
        ) : Parcelable

        @IgnoredOnParcel
        val photo = content?.find { it.code == "photo" }

        override fun docId() = id ?: ""
        override fun getItemType() = DriverLicenseConst.NAME
        override fun getDocColor() = R.color.purple_light
        override fun getDocNum() = docNumber
        override fun getStatus(): Int = docStatus

        fun setStatus(status: Int){
            docStatus = status
        }
        override fun getExpirationDateISO() = Preferences.DEF
        override fun localization(): LocalizationType? = localization

        override fun setLocalization(code: LocalizationType) {
            localization = code
        }

        override fun getDocExpirationDate() = expirationDate

        override fun getWeight() = DocWeight.WEIGHT_4
        override fun getPhoto() = photo?.image
        override fun getLightParcelable(): DiiaDocument {
            throw IllegalStateException()
        }

        override fun makeCopy() = this.copy()
        override fun verificationCodesCount() = 2
        override fun getPersonName(): String {
            return docData?.fullName ?: ""
        }

        override fun getDisplayDate(): String {
            return ""
        }

        override fun getDocOrder() = order
        override fun setNewOrder(newOrder: Int) {
            order = newOrder
        }

        override fun birthCertificateId() = ""

        override fun getDocName() = docData?.docName

        override fun getDocOrderDescription() =
            this.dataForDisplayingInOrderConfigurations?.description

        override fun getDocOrderLabel() = this.dataForDisplayingInOrderConfigurations?.label

        override fun getTicker(): TickerAtm? {
            return when (this.localization) {
                LocalizationType.ua -> this.frontCard.ua?.find {
                    it.tickerAtm != null
                }?.tickerAtm

                LocalizationType.eng -> this.frontCard.en?.find {
                    it.tickerAtm != null
                }?.tickerAtm

                null -> {
                    when (shareLocalization) {
                        LocalizationType.ua -> this.frontCard.ua?.find {
                            it.tickerAtm != null
                        }?.tickerAtm

                        LocalizationType.eng -> this.frontCard.en?.find {
                            it.tickerAtm != null
                        }?.tickerAtm

                        else -> {
                            this.frontCard.ua?.find {
                                it.tickerAtm != null
                            }?.tickerAtm
                        }
                    }
                }
            }
        }

        override fun getDocHeading(): DocHeadingOrg? {
            return when (this.localization) {
                LocalizationType.ua -> this.frontCard.ua?.find {
                    it.docHeadingOrg != null
                }?.docHeadingOrg

                LocalizationType.eng -> this.frontCard.en?.find {
                    it.docHeadingOrg != null
                }?.docHeadingOrg

                null -> {
                    when (shareLocalization) {
                        LocalizationType.ua -> this.frontCard.ua?.find {
                            it.docHeadingOrg != null
                        }?.docHeadingOrg

                        LocalizationType.eng -> this.frontCard.en?.find {
                            it.docHeadingOrg != null
                        }?.docHeadingOrg

                        else -> {
                            this.frontCard.ua?.find {
                                it.docHeadingOrg != null
                            }?.docHeadingOrg
                        }
                    }
                }
            }
        }

        override fun getDocButtonHeading(): DocButtonHeadingOrg? {
            return when (this.localization) {
                LocalizationType.ua -> this.frontCard.ua?.find {
                    it.docButtonHeadingOrg != null
                }?.docButtonHeadingOrg

                LocalizationType.eng -> this.frontCard.en?.find {
                    it.docButtonHeadingOrg != null
                }?.docButtonHeadingOrg

                null -> {
                    when (shareLocalization) {
                        LocalizationType.ua -> this.frontCard.ua?.find {
                            it.docButtonHeadingOrg != null
                        }?.docButtonHeadingOrg

                        LocalizationType.eng -> this.frontCard.en?.find {
                            it.docButtonHeadingOrg != null
                        }?.docButtonHeadingOrg

                        else -> {
                            this.frontCard.ua?.find {
                                it.docButtonHeadingOrg != null
                            }?.docButtonHeadingOrg
                        }
                    }
                }
            }
        }

        override fun getTableBlockTwoColumnsPlane(): List<TableBlockTwoColumnsPlaneOrg>? {
            return when (this.localization) {
                LocalizationType.ua -> this.frontCard.ua?.mapNotNull { it.tableBlockTwoColumnsPlaneOrg }
                LocalizationType.eng -> this.frontCard.en?.mapNotNull { it.tableBlockTwoColumnsPlaneOrg }
                null -> {
                    when (shareLocalization) {
                        LocalizationType.ua -> this.frontCard.ua?.mapNotNull { it.tableBlockTwoColumnsPlaneOrg }
                        LocalizationType.eng -> this.frontCard.en?.mapNotNull { it.tableBlockTwoColumnsPlaneOrg }
                        else -> {
                            this.frontCard.ua?.mapNotNull { it.tableBlockTwoColumnsPlaneOrg }

                        }
                    }
                }
            }
        }

        override fun getTableBlockPlaneOrg(): List<TableBlockPlaneOrg>? {
            return when (this.localization) {
                LocalizationType.ua -> this.frontCard.ua?.mapNotNull { it.tableBlockPlaneOrg }
                LocalizationType.eng -> this.frontCard.en?.mapNotNull { it.tableBlockPlaneOrg }
                null -> {
                    when (shareLocalization) {
                        LocalizationType.ua -> this.frontCard.ua?.mapNotNull { it.tableBlockPlaneOrg }
                        LocalizationType.eng -> this.frontCard.en?.mapNotNull { it.tableBlockPlaneOrg }
                        else -> {
                            this.frontCard.ua?.mapNotNull { it.tableBlockPlaneOrg }

                        }
                    }
                }
            }
        }

        override fun getSubtitleLabel(): SubtitleLabelMlc? {
            return when (this.localization) {
                LocalizationType.ua -> this.frontCard.ua?.find {
                    it.subtitleLabelMlc != null
                }?.subtitleLabelMlc

                LocalizationType.eng -> this.frontCard.en?.find {
                    it.subtitleLabelMlc != null
                }?.subtitleLabelMlc

                null -> {
                    when (shareLocalization) {
                        LocalizationType.ua -> this.frontCard.ua?.find {
                            it.subtitleLabelMlc != null
                        }?.subtitleLabelMlc

                        LocalizationType.eng -> this.frontCard.en?.find {
                            it.subtitleLabelMlc != null
                        }?.subtitleLabelMlc

                        else -> {
                            this.frontCard.ua?.find {
                                it.subtitleLabelMlc != null
                            }?.subtitleLabelMlc
                        }
                    }
                }
            }
        }

        override fun getChipStatus(): ChipStatusAtm? {
            return when (this.localization) {
                LocalizationType.ua -> this.frontCard.ua?.find {
                    it.chipStatusAtm != null
                }?.chipStatusAtm

                LocalizationType.eng -> this.frontCard.en?.find {
                    it.chipStatusAtm != null
                }?.chipStatusAtm

                null -> {
                    when (shareLocalization) {
                        LocalizationType.ua -> this.frontCard.ua?.find {
                            it.chipStatusAtm != null
                        }?.chipStatusAtm

                        LocalizationType.eng -> this.frontCard.en?.find {
                            it.chipStatusAtm != null
                        }?.chipStatusAtm

                        else -> {
                            this.frontCard.ua?.find {
                                it.chipStatusAtm != null
                            }?.chipStatusAtm
                        }
                    }
                }
            }
        }
    }

    override fun getData(): List<Data> = data

    companion object {
        const val STATUS_OK = Http.HTTP_200
        const val STATUS_NO_PHOTO = Http.HTTP_1010
        const val STATUS_OUTDATED = Http.HTTP_1011
        const val STATUS_NEED_VERIFICATION = Http.HTTP_1012
        const val STATUS_PHOTO_NOT_VALID = Http.HTTP_1013
        const val STATUS_IS_INVALID = Http.HTTP_1016
    }
}