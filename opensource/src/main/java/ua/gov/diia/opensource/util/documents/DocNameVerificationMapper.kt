package ua.gov.diia.opensource.util.documents

import ua.gov.diia.core.models.document.DiiaDocument
import ua.gov.diia.doc_driver_license.models.DocName
import ua.gov.diia.doc_driver_license.models.v2.DriverLicenseV2
import javax.inject.Inject

interface DocNameVerificationMapper {

    fun mapDocGeneral(doc: DiiaDocument): String

    fun mapDocFullInfo(doc: DiiaDocument): String

}

class DocNameVerificationMapperImpl @Inject constructor() : DocNameVerificationMapper {

    override fun mapDocGeneral(doc: DiiaDocument): String {
        return when (doc) {
            is DriverLicenseV2.Data -> DocName.DRIVER_LICENSE
            else -> ""
        }
    }

    override fun mapDocFullInfo(doc: DiiaDocument): String {
        return when (doc) {
            is DriverLicenseV2.Data -> DocName.DRIVER_LICENSE
            else -> ""
        }
    }
}