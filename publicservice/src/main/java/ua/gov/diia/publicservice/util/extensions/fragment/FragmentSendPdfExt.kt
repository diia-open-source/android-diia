package ua.gov.diia.publicservice.util.extensions.fragment

import androidx.fragment.app.Fragment
import ua.gov.diia.core.util.extensions.fragment.sendPdf
import ua.gov.diia.diia_storage.AndroidBase64Wrapper


fun Fragment.sendPdf(base64pdf: String, fileName: String, applicationId: String) {
    val pdfInBytes = AndroidBase64Wrapper().decode(base64pdf.toByteArray())
    context?.sendPdf(pdfInBytes, fileName, applicationId)
}
