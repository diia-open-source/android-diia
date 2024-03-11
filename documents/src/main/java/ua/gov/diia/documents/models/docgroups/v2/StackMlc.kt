package ua.gov.diia.documents.models.docgroups.v2


import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize
import ua.gov.diia.core.models.common_compose.atm.icon.SmallIconAtm

@Parcelize
@JsonClass(generateAdapter = true)
data class StackMlc(
    @Json(name = "amount")
    val amount: Int?,
    @Json(name = "smallIconAtm")
    val smallIconAtm: SmallIconAtm?
): Parcelable