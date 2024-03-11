package ua.gov.diia.opensource.util.ext

import android.content.Intent
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import ua.gov.diia.diia_storage.AndroidBase64Wrapper
import ua.gov.diia.opensource.BuildConfig
import ua.gov.diia.opensource.util.file.AndroidInternalFileManager

fun Fragment.sendPdf(base64pdf: String, fileName: String) {
    val pdfInBytes = AndroidBase64Wrapper().decode(base64pdf.toByteArray())
    sendPdf(pdfInBytes, fileName)
}

fun Fragment.sendPdf(pdfInBytes: ByteArray, fileName: String) {
    val fileManager = AndroidInternalFileManager(requireContext(), "docs")
    fileManager.saveFile(fileName, pdfInBytes)

    val intent = Intent(Intent.ACTION_SEND).apply {
        type = "application/pdf"
        putExtra(
            Intent.EXTRA_STREAM, FileProvider.getUriForFile(
                requireContext(),
                BuildConfig.APPLICATION_ID,
                fileManager.getFile(fileName)
            )
        )
        putExtra(Intent.EXTRA_SUBJECT, fileName)
    }
    startActivity(intent)
}

fun Fragment.sendZip(base64zip: String, fileName: String) {
    val zipInBytes = AndroidBase64Wrapper().decode(base64zip.toByteArray())
    val fileManager = AndroidInternalFileManager(requireContext(), "docs")

    fileManager.saveFile(fileName, zipInBytes)

    val intent = Intent(Intent.ACTION_SEND).apply {
        type = "application/zip"
        putExtra(
            Intent.EXTRA_STREAM, FileProvider.getUriForFile(
                requireContext(),
                BuildConfig.APPLICATION_ID,
                fileManager.getFile(fileName)
            )
        )
        putExtra(Intent.EXTRA_SUBJECT, fileName)
    }
    startActivity(intent)

}
