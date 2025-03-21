package ua.gov.diia.core.util.extensions.fragment

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import ua.gov.diia.core.util.delegation.WithCrashlytics
import ua.gov.diia.core.util.file.AndroidInternalFileManager

fun Fragment.sendImage(byte: ByteArray, fileName: String, applicationId: String) {
    context?.sendImage(byte, fileName, applicationId)
}

fun Context.sendImage(byte: ByteArray, fileName: String, applicationId: String) {
    val fileManager = AndroidInternalFileManager(this, "docs")
    fileManager.saveFile(fileName, byte)

    val intent = Intent(Intent.ACTION_SEND).apply {
        type = "image/*"
        putExtra(
            Intent.EXTRA_STREAM, FileProvider.getUriForFile(
                this@sendImage,
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
    context?.sendPdf(pdfInBytes, fileName, applicationId)
}

fun Context.sendPdf(pdfInBytes: ByteArray, fileName: String, applicationId: String) {
    val fileManager = AndroidInternalFileManager(this, "docs")
    fileManager.saveFile(fileName, pdfInBytes)

    val intent = Intent(Intent.ACTION_SEND).apply {
        type = "application/pdf"
        putExtra(
            Intent.EXTRA_STREAM, FileProvider.getUriForFile(
                this@sendPdf,
                applicationId,
                fileManager.getFile(fileName)
            )
        )
        putExtra(Intent.EXTRA_SUBJECT, fileName)
    }
    startActivity(intent)
}

fun Fragment.openPdfFile(fileUri: String, crashlytics: WithCrashlytics) {
    try {
        val intent = Intent(Intent.ACTION_VIEW).apply {
            setDataAndType(Uri.parse(fileUri), "application/pdf")
        }
        val appsToOpenPDFExist = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            context?.packageManager?.queryIntentActivities(
                intent,
                PackageManager.ResolveInfoFlags.of(PackageManager.MATCH_DEFAULT_ONLY.toLong())
            )?.size != 0
        } else {
            context?.packageManager?.queryIntentActivities(
                intent,
                PackageManager.GET_META_DATA
            )?.size != 0
        }
        if (!appsToOpenPDFExist) {
            intent.setDataAndType(Uri.parse(fileUri), null)
        }
        val chooser = Intent.createChooser(intent, "")
        startActivity(chooser)
    } catch (e: Exception) {
        crashlytics.sendNonFatalError(e)
    }
}