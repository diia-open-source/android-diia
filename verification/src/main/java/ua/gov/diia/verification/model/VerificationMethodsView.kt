package ua.gov.diia.verification.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class VerificationMethodsView(
    val title: String?,
    val methods: List<VerificationMethodView>,
    val buttonAction: String,
    val schema: String
): Parcelable
