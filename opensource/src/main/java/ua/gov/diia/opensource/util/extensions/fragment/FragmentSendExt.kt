package ua.gov.diia.opensource.util.extensions.fragment

import android.content.Intent
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import ua.gov.diia.core.util.file.AndroidInternalFileManager
import ua.gov.diia.opensource.BuildConfig
import ua.gov.diia.diia_storage.AndroidBase64Wrapper

fun Fragment.sendZip(base64zip: String, fileName: String) {
    val zipInBytes = AndroidBase64Wrapper().decode(base64zip.toByteArray())
    val fileManager = AndroidInternalFileManager(requireContext(), "docs")
    //activityComponent.addLifecycleDependency(lifecycle, fileManager, FileManager::class.java)
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

fun Fragment.sendZip(byte: ByteArray, fileName: String) {
    val fileManager =
        ua.gov.diia.core.util.file.AndroidInternalFileManager(requireContext(), "docs")
    fileManager.saveFile(fileName, byte)

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
