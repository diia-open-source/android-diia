package ua.gov.diia.core.models.common_compose.org.container

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize
import ua.gov.diia.core.models.common_compose.mlc.checkbox.TableItemCheckboxMlc
import ua.gov.diia.core.models.common_compose.table.TableMainHeadingMlc
import ua.gov.diia.core.models.common_compose.table.TableSecondaryHeadingMlc

@Parcelize
@JsonClass(generateAdapter = true)
data class BackgroundWhiteOrg(
    @Json(name = "componentId")
    val componentId: String? = null,
    @Json(name = "items")
    val items: List<Item>? = null,
) : Parcelable {

    @Parcelize
    @JsonClass(generateAdapter = true)
    data class Item(
        @Json(name = "tableMainHeadingMlc")
        val tableMainHeadingMlc: TableMainHeadingMlc? = null,
        @Json(name = "tableSecondaryHeadingMlc")
        val tableSecondaryHeadingMlc: TableSecondaryHeadingMlc? = null,
        @Json(name = "tableItemCheckboxMlc")
        val tableItemCheckboxMlc: TableItemCheckboxMlc? = null,
    ) : Parcelable
}