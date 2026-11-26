package ua.gov.diia.documents.models

import com.squareup.moshi.JsonClass

/** Presentation data for the proposed template, not a registry API contract.
 * Dates are display strings prepared by the document mapper, without timezone conversion.
 */
@JsonClass(generateAdapter = true)
data class PassportBookletData(
    val identity: Identity,
    val issuance: Issuance,
    val additionalFields: List<Field>? = null
) {
    @JsonClass(generateAdapter = true)
    data class Identity(
        val lastName: String,
        val firstName: String,
        val middleName: String? = null,
        val birthDate: String,
        val birthPlace: String,
        val sex: String? = null
    )

    @JsonClass(generateAdapter = true)
    data class Issuance(
        val series: String,
        val number: String,
        val issuedAt: String,
        val issuedBy: String,
        val validUntil: String? = null
    )

    @JsonClass(generateAdapter = true)
    data class Field(val label: String, val value: String)

    val displayNumber: String
        get() = listOf(issuance.series, issuance.number)
            .map { it.trim() }
            .filter { it.isNotEmpty() }
            .joinToString(" ")
}
