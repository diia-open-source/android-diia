package ua.gov.diia.ps_criminal_cert.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import ua.gov.diia.ps_criminal_cert.models.enums.CriminalCertType
import ua.gov.diia.ps_criminal_cert.models.request.PublicService

@Parcelize
data class CriminalCertUserData(
    val reasonId: String? = null,
    val certificateType: CriminalCertType? = null,
    val prevNames: PreviousNames? = null,
    val birth: Birth? = null,
    val nationalities: List<String>? = null,
    val registrationAddressId: String? = null,
    val phoneNumber: String? = null,
    val publicService: PublicService? = null
) : Parcelable