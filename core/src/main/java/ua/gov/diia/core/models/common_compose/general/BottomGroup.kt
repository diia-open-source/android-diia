package ua.gov.diia.core.models.common_compose.general

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import ua.gov.diia.core.models.common_compose.org.bottom.BottomGroupOrg
import ua.gov.diia.core.models.common_compose.org.checkbox.CheckboxBtnOrg
import ua.gov.diia.core.models.common_compose.org.list.ListItemGroupOrg

@JsonClass(generateAdapter = true)
data class BottomGroup(
    @Json(name = "listItemGroupOrg")
    val listItemGroupOrg: ListItemGroupOrg?,
    @Json(name = "bottomGroupOrg")
    val bottomGroupOrg: BottomGroupOrg?,
    @Json(name = "checkboxBtnOrg")
    val checkboxBtnOrg: CheckboxBtnOrg? = null,
)