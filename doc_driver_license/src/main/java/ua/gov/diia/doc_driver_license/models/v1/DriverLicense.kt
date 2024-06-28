package ua.gov.diia.doc_driver_license.models.v1

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize
import ua.gov.diia.doc_driver_license.R
import ua.gov.diia.doc_driver_license.models.DocName
import ua.gov.diia.documents.models.DiiaDocument
import ua.gov.diia.documents.models.DiiaDocumentWithMetadata
import ua.gov.diia.documents.models.DocWeight
import ua.gov.diia.documents.models.DocumentWithPhoto
import ua.gov.diia.documents.models.LocalizationType
import ua.gov.diia.documents.models.Preferences
import ua.gov.diia.documents.models.SourceType
import ua.gov.diia.documents.models.WithOrder
import ua.gov.diia.documents.models.docgroups.BaseDocumentGroup

@Parcelize
@JsonClass(generateAdapter = true)
class DriverLicense(
    @Json(name = "data")
    internal val data: List<Data> = listOf(),
) : BaseDocumentGroup<DriverLicense.Data>() {

    override fun getItemType() = DocName.DRIVER_LICENSE

    @Parcelize
    @JsonClass(generateAdapter = true)
    data class Data(
        @Json(name = "docStatus")
        internal var docStatus: Int,
        @Json(name = "localization")
        internal var localization: LocalizationType?,
        @Json(name = "shareLocalization")
        internal var shareLocalization: LocalizationType?,
        @Json(name = "id")
        override val id: String?,
        @Json(name = "type")
        val type: String?,
        @Json(name = "expirationDate")
        internal val expirationDate: String = "",
        @Json(name = "categories")
        val categories: String?,
        @Json(name = "issueDate")
        val issueDate: String?,
        @Json(name = "serialNumber")
        val serialNumber: String?,
        @Json(name = "serial")
        val serial: String?,
        @Json(name = "number")
        val number: String?,
        @Json(name = "lastNameUA")
        val lastNameUA: String?,
        @Json(name = "firstNameUA")
        val firstNameUA: String?,
        @Json(name = "middleNameUA")
        val middleNameUA: String?,
        @Json(name = "birthday")
        val birthday: String?,
        @Json(name = "department")
        val department: String?,
        @Json(name = "photo")
        internal var photo: String?,
        @Json(name = "order")
        internal var order: Int = DiiaDocumentWithMetadata.LAST_DOC_ORDER,
        @Json(name = "ua")
        val ua: UA?,
        @Json(name = "eng")
        val eng: ENG?,
    ) : DiiaDocument, DocumentWithPhoto, WithOrder {

        @Parcelize
        @JsonClass(generateAdapter = true)
        data class UA(
            @Json(name = "birth")
            val birth: Birth?,
            @Json(name = "birthDate")
            val birthDate: BirthDate?,
            val category: Category?,
            @Json(name = "categoryOpeningDate")
            val categoryOpeningDate: CategoryOpeningDate?,
            @Json(name = "country")
            val country: String?,
            @Json(name = "department")
            val department: Department?,
            @Json(name = "documentNumber")
            val documentNumber: DocumentNumber?,
            @Json(name = "expiryDate")
            val expiryDate: ExpiryDate?,
            @Json(name = "firstName")
            val firstName: FirstName?,
            @Json(name = "icon")
            val icon: String?,
            @Json(name = "identifier")
            val identifier: Identifier?,
            @Json(name = "issueDate")
            val issueDate: IssueDate?,
            @Json(name = "lastName")
            val lastName: LastName?,
            @Json(name = "name")
            val name: String?,
            @Json(name = "tickerOptions")
            val tickerOptions: TickerOptions?,
            @Json(name = "card")
            val card: Card?
        ) : Parcelable {

            @JsonClass(generateAdapter = true)
            @Parcelize
            data class Card(
                @Json(name = "birthDate")
                val birthDate: BirthDate?,
                @Json(name = "category")
                val category: Category?,
                @Json(name = "documentNumber")
                val documentNumber: DocumentNumber?,
                @Json(name = "firstName")
                val firstName: String?,
                @Json(name = "icon")
                val icon: String?,
                @Json(name = "lastName")
                val lastName: String?,
                @Json(name = "middleName")
                val middleName: String?,
                @Json(name = "name")
                val name: String?
            ) : Parcelable {
                @JsonClass(generateAdapter = true)
                @Parcelize
                data class BirthDate(
                    @Json(name = "name")
                    val name: String?,
                    @Json(name = "value")
                    val value: String?
                ) : Parcelable

                @JsonClass(generateAdapter = true)
                @Parcelize
                data class Category(
                    @Json(name = "name")
                    val name: String?,
                    @Json(name = "value")
                    val value: String?
                ) : Parcelable

                @JsonClass(generateAdapter = true)
                @Parcelize
                data class DocumentNumber(
                    @Json(name = "name")
                    val name: String?,
                    @Json(name = "value")
                    val value: String?
                ) : Parcelable
            }

            @Parcelize
            @JsonClass(generateAdapter = true)
            data class Birth(
                @Json(name = "code")
                val code: String?,
                @Json(name = "name")
                val name: String?,
                @Json(name = "value")
                val value: String?
            ) : Parcelable

            @Parcelize
            @JsonClass(generateAdapter = true)
            data class BirthDate(
                @Json(name = "name")
                val name: String?,
                @Json(name = "value")
                val value: String?
            ) : Parcelable

            @Parcelize
            @JsonClass(generateAdapter = true)
            data class Category(
                @Json(name = "code")
                val code: String?,
                @Json(name = "name")
                val name: String?,
                @Json(name = "value")
                val value: String?
            ) : Parcelable

            @Parcelize
            @JsonClass(generateAdapter = true)
            data class CategoryOpeningDate(
                @Json(name = "code")
                val code: String?,
                @Json(name = "name")
                val name: String?,
                @Json(name = "value")
                val value: String?
            ) : Parcelable

            @Parcelize
            @JsonClass(generateAdapter = true)
            data class Department(
                @Json(name = "code")
                val code: String?,
                @Json(name = "name")
                val name: String?,
                @Json(name = "value")
                val value: String?
            ) : Parcelable

            @Parcelize
            @JsonClass(generateAdapter = true)
            data class DocumentNumber(
                @Json(name = "code")
                val code: String?,
                @Json(name = "name")
                val name: String?,
                @Json(name = "value")
                val value: String?
            ) : Parcelable

            @Parcelize
            @JsonClass(generateAdapter = true)
            data class ExpiryDate(
                @Json(name = "code")
                val code: String?,
                @Json(name = "name")
                val name: String?,
                @Json(name = "value")
                val value: String?
            ) : Parcelable

            @Parcelize
            @JsonClass(generateAdapter = true)
            data class FirstName(
                @Json(name = "code")
                val code: String?,
                @Json(name = "name")
                val name: String?,
                @Json(name = "value")
                val value: String?
            ) : Parcelable

            @Parcelize
            @JsonClass(generateAdapter = true)
            data class Identifier(
                @Json(name = "code")
                val code: String?,
                @Json(name = "name")
                val name: String?,
                @Json(name = "value")
                val value: String?
            ) : Parcelable

            @Parcelize
            @JsonClass(generateAdapter = true)
            data class IssueDate(
                @Json(name = "code")
                val code: String?,
                @Json(name = "name")
                val name: String?,
                @Json(name = "value")
                val value: String?
            ) : Parcelable

            @Parcelize
            @JsonClass(generateAdapter = true)
            data class LastName(
                @Json(name = "code")
                val code: String?,
                @Json(name = "name")
                val name: String?,
                @Json(name = "value")
                val value: String?
            ) : Parcelable

            @Parcelize
            @JsonClass(generateAdapter = true)
            data class TickerOptions(
                @Json(name = "text")
                val text: String?,
                @Json(name = "type")
                val type: String?
            ) : Parcelable
        }

        @Parcelize
        @JsonClass(generateAdapter = true)
        data class ENG(
            @Json(name = "birth")
            val birth: Birth?,
            @Json(name = "birthDate")
            val birthDate: BirthDate?,
            @Json(name = "category")
            val category: Category?,
            @Json(name = "categoryOpeningDate")
            val categoryOpeningDate: CategoryOpeningDate?,
            @Json(name = "country")
            val country: String?,
            @Json(name = "department")
            val department: Department?,
            @Json(name = "documentNumber")
            val documentNumber: DocumentNumber?,
            @Json(name = "expiryDate")
            val expiryDate: ExpiryDate?,
            @Json(name = "firstName")
            val firstName: FirstName?,
            @Json(name = "icon")
            val icon: String?,
            @Json(name = "identifier")
            val identifier: Identifier?,
            @Json(name = "issueDate")
            val issueDate: IssueDate?,
            @Json(name = "lastName")
            val lastName: LastName?,
            @Json(name = "name")
            val name: String?,
            @Json(name = "tickerOptions")
            val tickerOptions: TickerOptions?,
            @Json(name = "card")
            val card: Card?
        ) : Parcelable {

            @JsonClass(generateAdapter = true)
            @Parcelize
            data class Card(
                @Json(name = "birthDate")
                val birthDate: BirthDate?,
                @Json(name = "category")
                val category: Category?,
                @Json(name = "documentNumber")
                val documentNumber: DocumentNumber?,
                @Json(name = "firstName")
                val firstName: String?,
                @Json(name = "icon")
                val icon: String?,
                @Json(name = "lastName")
                val lastName: String?,
                @Json(name = "middleName")
                val middleName: String?,
                @Json(name = "name")
                val name: String?
            ) : Parcelable {
                @JsonClass(generateAdapter = true)
                @Parcelize
                data class BirthDate(
                    @Json(name = "name")
                    val name: String?,
                    @Json(name = "value")
                    val value: String?
                ) : Parcelable

                @JsonClass(generateAdapter = true)
                @Parcelize
                data class Category(
                    @Json(name = "name")
                    val name: String?,
                    @Json(name = "value")
                    val value: String?
                ) : Parcelable

                @JsonClass(generateAdapter = true)
                @Parcelize
                data class DocumentNumber(
                    @Json(name = "name")
                    val name: String?,
                    @Json(name = "value")
                    val value: String?
                ) : Parcelable
            }

            @Parcelize
            @JsonClass(generateAdapter = true)
            data class Birth(
                @Json(name = "code")
                val code: String?,
                @Json(name = "name")
                val name: String?,
                @Json(name = "value")
                val value: String?
            ) : Parcelable

            @Parcelize
            @JsonClass(generateAdapter = true)
            data class BirthDate(
                @Json(name = "name")
                val name: String?,
                @Json(name = "value")
                val value: String?
            ) : Parcelable

            @Parcelize
            @JsonClass(generateAdapter = true)
            data class Category(
                @Json(name = "code")
                val code: String?,
                @Json(name = "name")
                val name: String?,
                @Json(name = "value")
                val value: String?
            ) : Parcelable

            @Parcelize
            @JsonClass(generateAdapter = true)
            data class CategoryOpeningDate(
                @Json(name = "code")
                val code: String?,
                @Json(name = "name")
                val name: String?,
                @Json(name = "value")
                val value: String?
            ) : Parcelable

            @Parcelize
            @JsonClass(generateAdapter = true)
            data class Department(
                @Json(name = "code")
                val code: String?,
                @Json(name = "name")
                val name: String?,
                @Json(name = "value")
                val value: String?
            ) : Parcelable

            @Parcelize
            @JsonClass(generateAdapter = true)
            data class DocumentNumber(
                @Json(name = "code")
                val code: String?,
                @Json(name = "name")
                val name: String?,
                @Json(name = "value")
                val value: String?
            ) : Parcelable

            @Parcelize
            @JsonClass(generateAdapter = true)
            data class ExpiryDate(
                @Json(name = "code")
                val code: String?,
                @Json(name = "name")
                val name: String?,
                @Json(name = "value")
                val value: String?
            ) : Parcelable

            @Parcelize
            @JsonClass(generateAdapter = true)
            data class FirstName(
                @Json(name = "code")
                val code: String?,
                @Json(name = "name")
                val name: String?,
                @Json(name = "value")
                val value: String?
            ) : Parcelable

            @Parcelize
            @JsonClass(generateAdapter = true)
            data class Identifier(
                @Json(name = "code")
                val code: String?,
                @Json(name = "name")
                val name: String?,
                @Json(name = "value")
                val value: String?
            ) : Parcelable

            @Parcelize
            @JsonClass(generateAdapter = true)
            data class IssueDate(
                @Json(name = "code")
                val code: String?,
                @Json(name = "name")
                val name: String?,
                @Json(name = "value")
                val value: String?
            ) : Parcelable

            @Parcelize
            @JsonClass(generateAdapter = true)
            data class LastName(
                @Json(name = "code")
                val code: String?,
                @Json(name = "name")
                val name: String?,
                @Json(name = "value")
                val value: String?
            ) : Parcelable

            @Parcelize
            @JsonClass(generateAdapter = true)
            data class TickerOptions(
                @Json(name = "text")
                val text: String?,
                @Json(name = "type")
                val type: String?
            ) : Parcelable
        }

        fun docName(): String = when (localization) {
            LocalizationType.eng -> { eng?.card?.name + eng?.card?.icon }
            LocalizationType.ua -> { ua?.card?.name + ua?.card?.icon }
            null -> {
                when (shareLocalization) {
                    LocalizationType.eng -> { eng?.card?.name + eng?.card?.icon }
                    LocalizationType.ua -> { ua?.card?.name + ua?.card?.icon }
                    else -> { "Посвідчення водія |_|_|" }
                }
            }
        }

        fun fullName(): String = when (localization) {
            LocalizationType.eng -> { eng?.card?.lastName + "\n" + eng?.card?.firstName }
            LocalizationType.ua -> { ua?.card?.lastName + "\n" + ua?.card?.firstName + "\n" + ua?.card?.middleName}
            null -> {
                when (shareLocalization) {
                    LocalizationType.eng -> { eng?.card?.lastName + "\n" + eng?.card?.firstName }
                    LocalizationType.ua -> { ua?.card?.lastName + "\n" + ua?.card?.firstName + "\n" + ua?.card?.middleName}
                    else -> { lastNameUA + "\n" + firstNameUA + "\n" + middleNameUA }
                }
            }
        }

        override fun docId() = id ?: ""
        override fun getItemType() = DocName.DRIVER_LICENSE
        override fun getDocColor() = R.color.purple_light
        override fun getDocNum() = serialNumber
        override fun getStatus(): Int = docStatus
        override fun getDocExpirationDate() = expirationDate
        override fun getExpirationDateISO() = Preferences.DEF
        override fun localization(): LocalizationType? = localization

        override fun setLocalization(code: LocalizationType) {
            localization = code
        }

        override fun getWeight() = DocWeight.WEIGHT_4
        override fun getPhoto() = photo
        override fun getPersonName() = "$lastNameUA $firstNameUA $middleNameUA"
        override fun getDisplayDate() = expirationDate
        override fun makeCopy() = this.copy()
        override fun verificationCodesCount() = 2

        override fun getLightParcelable() = copy(photo = null)

        override fun getDocOrder() = order
        override fun setNewOrder(newOrder: Int) {
            order = newOrder
        }
        override fun getSourceType() = SourceType.DYNAMIC

        override fun getDocStackTitle() = ""

        override fun birthCertificateId() = ""

        override fun getDocName() = ""

        override fun getDocOrderDescription() = ""
        override fun getDocOrderLabel() = ""
    }

    override fun getData(): List<Data> = data
}