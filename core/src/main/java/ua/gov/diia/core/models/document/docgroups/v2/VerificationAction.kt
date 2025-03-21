package ua.gov.diia.core.models.document.docgroups.v2

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class VerificationAction(
    val actionKey: String,
    val position: Int,
    val id: String,
    val docName: String
) : Parcelable