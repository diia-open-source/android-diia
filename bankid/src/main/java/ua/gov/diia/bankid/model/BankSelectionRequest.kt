package ua.gov.diia.bankid.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class BankSelectionRequest(
    val schema: String,
    val processId: String,
    val verificationMethodCode: String
) : Parcelable
