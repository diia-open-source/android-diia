package ua.gov.diia.ps_criminal_cert.models

data class CriminalCertHomeState(
    val hasDoneList: Boolean? = null,
    val hasProcessingList: Boolean? = null
) {

    val hasContent = hasDoneList == true || hasProcessingList == true
}