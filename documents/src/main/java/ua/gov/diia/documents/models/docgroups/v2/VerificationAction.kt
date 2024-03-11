package ua.gov.diia.documents.models.docgroups.v2

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class VerificationAction(
    val actionKey: String,
    val position: Int,
    val id: String,
    val docName: String
) : Parcelable