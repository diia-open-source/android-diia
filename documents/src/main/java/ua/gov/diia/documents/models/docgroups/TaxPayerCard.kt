package ua.gov.diia.documents.models.docgroups

import android.content.Context
import android.os.Parcelable
import androidx.annotation.DrawableRes
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize
import ua.gov.diia.documents.R

@Parcelize
@JsonClass(generateAdapter = true)
data class TaxPayerCard(
    @Json(name = "status")
    val status: Int,
    @Json(name = "number")
    val number: String,
    @Json(name = "creationDate")
    val creationDate: String? = null
) : Parcelable {

    fun getVerificationStatusSting(context: Context): String {
        return when (status) {
            STATUS_OK -> context.getString(R.string.taxpayer_card_status_ok, creationDate)
            STATUS_ON_VERIFICATION -> context.getString(R.string.taxpayer_card_status_in_progress)
            STATUS_NOT_VERIFIED -> context.getString(R.string.taxpayer_card_status_failed)
            else -> {
                throw IllegalArgumentException("Unknown taxpayer card status: $status")
            }
        }
    }

    @DrawableRes
    fun getStatusIcon(): Int {
        return if (status == STATUS_OK) {
            R.drawable.ic_checked
        } else {
            R.drawable.ic_alert
        }
    }

    fun displayNumberOrPlaceholder(): String {
        return if (status == STATUS_OK) {
            number
        } else {
            "ХХХХХХХХХХ"
        }
    }

    companion object {
        const val STATUS_OK = 200
        const val STATUS_ON_VERIFICATION = 1014
        const val STATUS_NOT_VERIFIED = 1015
    }
}