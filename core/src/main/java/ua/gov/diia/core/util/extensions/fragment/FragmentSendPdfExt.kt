package ua.gov.diia.core.util.extensions.fragment

import android.content.Intent
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import ua.gov.diia.core.util.file.AndroidInternalFileManager

fun Fragment.sendImage(byte: ByteArray, fileName: String, applicationId: String) {
    val fileManager = AndroidInternalFileManager(requireContext(), "docs")
    fileManager.saveFile(fileName, byte)

    val intent = Intent(Intent.ACTION_SEND).apply {
        type = "image/*"
        putExtra(
            Intent.EXTRA_STREAM, FileProvider.getUriForFile(
                requireContext(),
                applicationId,
                fileManager.getFile(fileName),
            )
        )
        //display name
        //putExtra(Intent.EXTRA_SUBJECT, "")
    }
    startActivity(Intent.createChooser(intent, null))
}

fun Fragment.sendPdf(pdfInBytes: ByteArray, fileName: String, applicationId: String) {
    val fileManager = AndroidInternalFileManager(requireContext(), "docs")
    fileManager.saveFile(fileName, pdfInBytes)

    val intent = Intent(Intent.ACTION_SEND).apply {
        type = "application/pdf"
        putExtra(
            Intent.EXTRA_STREAM, FileProvider.getUriForFile(
                requireContext(),
                applicationId,
                fileManager.getFile(fileName)
            )
        )
        putExtra(Intent.EXTRA_SUBJECT, fileName)
    }
    startActivity(intent)
}