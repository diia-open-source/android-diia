package ua.gov.diia.core.models.document.docgroups.v2

import androidx.annotation.StringRes

data class DocCover(
    @StringRes val title: Int,
    @StringRes val description: Int,
    val buttonTitle: String?,
    val actionKey: String,
    val verificationCodesCount: Int,
    val data: String? = null
)