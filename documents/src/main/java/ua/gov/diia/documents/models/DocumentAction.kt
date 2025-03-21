package ua.gov.diia.documents.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class DocumentAction(
    val actionKey: String,
    val docType: String? = null,
    val position: String? = null,
    val id: String? = null,
    val data: String? = null,
) : Parcelable