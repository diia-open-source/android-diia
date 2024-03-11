package ua.gov.diia.core.models.common_compose.table.tableBlockOrg

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize
import ua.gov.diia.core.models.common_compose.table.Item
import ua.gov.diia.core.models.common_compose.table.TableMainHeadingMlc
import ua.gov.diia.core.models.common_compose.table.TableSecondaryHeadingMlc

@Parcelize
@JsonClass(generateAdapter = true)
data class TableBlockOrg(
    @Json(name = "componentId")
    val componentId: String? = null,
    @Json(name = "items")
    val items: List<Item>? = null,
    @Json(name = "tableMainHeadingMlc")
    val tableMainHeadingMlc: TableMainHeadingMlc? = null,
    @Json(name = "tableSecondaryHeadingMlc")
    val tableSecondaryHeadingMlc: TableSecondaryHeadingMlc? = null,
) : Parcelable