package ua.gov.diia.core.models.common_compose.org.checkbox

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import ua.gov.diia.core.models.common_compose.mlc.checkbox.SmallCheckIconMlc

@JsonClass(generateAdapter = true)
data class SmallCheckIconItem(
    @Json(name = "smallCheckIconMlc")
    val smallCheckIconMlc: SmallCheckIconMlc
)
