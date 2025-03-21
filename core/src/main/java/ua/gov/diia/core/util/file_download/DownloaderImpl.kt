package ua.gov.diia.core.util.file_download

import android.app.DownloadManager
import android.content.Context
import android.os.Environment
import androidx.core.net.toUri
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import okhttp3.ResponseBody
import ua.gov.diia.core.util.extensions.context.serviceDownloadManager
import java.io.BufferedInputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.zip.ZipEntry
import java.util.zip.ZipInputStream

class DownloaderImpl(val context: Context) : Downloader {

    private val downloadManager = context.serviceDownloadManager
    override fun downloadFile(url: String, id: String, title: String?): Long {
        val uri = url.toUri()
        deleteDirectory(id)
        val request = DownloadManager.Request(uri)
            .setMimeType("application/json")
            .setTitle(title)
            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            .setDestinationInExternalFilesDir(context, null, ROOT_DIR + "/" + id + "/" + uri.lastPathSegment)
        return downloadManager.enqueue(request)
    }

    override fun isFileSaved(id: String): Boolean {
        val dir = File(context.getExternalFilesDir(null), "$ROOT_DIR/$id")
        return dir.exists() && dir.isDirectory && !dir.listFiles().isNullOrEmpty()
    }

    override suspend fun subscribeForUpdate(
        downloadID: Long,
        statusCallback: (DownloadItem) -> Unit
    ) {
        if (downloadID == -1L) return
        var finishLoop = false
        while (!finishLoop) {
            val q = DownloadManager.Query().setFilterById(downloadID)
            val c = downloadManager.query(q)
            c.use { cursor ->
                if (cursor.moveToFirst()) {
                    val statusIndex = cursor.getColumnIndex(DownloadManager.COLUMN_STATUS)
                    val progressIndex = cursor.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR)
                    val totalIndex = cursor.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES)

                    if (statusIndex == -1 || progressIndex == -1 || totalIndex == -1) {
                        finishLoop = true
                    } else {
                        when (cursor.getInt(statusIndex)) {
                            DownloadManager.STATUS_PAUSED -> {
                                statusCallback(
                                    DownloadItem(reason = FailReason.NO_INTERNET, status = DownloadManager.STATUS_FAILED)
                                )
                            }
                            DownloadManager.STATUS_RUNNING,
                            DownloadManager.STATUS_PENDING -> {
                                val downloadedBytes = cursor.getInt(progressIndex)
                                val totalBytes = cursor.getInt(totalIndex)
                                statusCallback(
                                    DownloadItem(
                                        progressValue = calculateProgress(
                                            downloadedBytes,
                                            totalBytes
                                        ),
                                        status = DownloadManager.STATUS_RUNNING
                                    )
                                )
                            }

                            DownloadManager.STATUS_SUCCESSFUL -> {
                                finishLoop = true
                                statusCallback(
                                    DownloadItem(
                                        100,
                                        status = DownloadManager.STATUS_SUCCESSFUL
                                    )
                                )
                            }

                            DownloadManager.STATUS_FAILED -> {
                                val columnReason = cursor.getColumnIndex(DownloadManager.COLUMN_REASON)
                                val reason = when(cursor.getInt(columnReason)) {
                                    DownloadManager.ERROR_INSUFFICIENT_SPACE -> {
                                        FailReason.INSUFFICIENT_SPACE
                                    }
                                    DownloadManager.ERROR_HTTP_DATA_ERROR,
                                    DownloadManager.ERROR_TOO_MANY_REDIRECTS,
                                    DownloadManager.ERROR_UNHANDLED_HTTP_CODE -> {
                                        FailReason.HTTP_DATA_ERROR
                                    }
                                    else -> {
                                        FailReason.DOWNLOAD_FAILS
                                    }
                                }

                                finishLoop = true
                                statusCallback(DownloadItem(status = DownloadManager.STATUS_FAILED, reason = reason))
                            }
                        }
                    }
                    delay(300)
                } else {
                    finishLoop = true
                    statusCallback(DownloadItem(status = DownloadManager.STATUS_FAILED))
                }
            }
        }
    }

    override fun isFileInSavingState(id: String): Boolean {
        val q = DownloadManager.Query()
        val c = downloadManager.query(q)
        c.use { cursor ->
            while (cursor.moveToNext()) {
                val localUriColumnIndex = cursor.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI)
                val statusColumnIndex = cursor.getColumnIndex(DownloadManager.COLUMN_STATUS)
                if (localUriColumnIndex != -1) {
                    val localUri = cursor.getString(localUriColumnIndex)
                    val status = cursor.getInt(statusColumnIndex)
                    if (localUri != null && localUri.contains(id)) {
                        return (status == DownloadManager.STATUS_RUNNING || status == DownloadManager.STATUS_PENDING || status == DownloadManager.STATUS_PAUSED)
                    }
                }
            }
        }
        return false
    }

    override fun deleteDirectory(id: String) : Boolean {
        val dir = File(context.getExternalFilesDir(null), "$ROOT_DIR/$id")
        if (dir.exists() && dir.isDirectory) {
            dir.listFiles()?.forEach {
                it.delete()
            }
            return true
        }
        return false
    }

    override suspend fun clear() {
        val dir = File(context.getExternalFilesDir(null), ROOT_DIR)
        deleteRecursive(dir)
    }

    override suspend fun saveFile(fileBody: ResponseBody) {
        withContext(Dispatchers.IO) {
            val folder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
            var zipFileObject: ZipInputStream? = null
            try {
                val inputStream = fileBody.byteStream()
                zipFileObject = ZipInputStream(BufferedInputStream(inputStream))

                var entry: ZipEntry? = zipFileObject.nextEntry
                while (entry != null) {
                    if (!entry.isDirectory) {
                        val newPdfFileName = entry.name.substringBeforeLast(".") + ".pdf"
                        val newPdfFile = File(folder, newPdfFileName)
                        FileOutputStream(newPdfFile).use { output ->
                            zipFileObject.copyTo(output)
                        }
                    }
                    entry = zipFileObject.nextEntry
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                try {
                    zipFileObject?.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
    }

    private fun deleteRecursive(dir: File) {
        if (dir.isDirectory) {
            dir.listFiles()?.forEach {
                deleteRecursive(it)
            }
        } else {
            dir.delete()
        }
    }

    private fun calculateProgress(downloadedBytes: Int, totalBytes: Int): Int {
        return if (downloadedBytes > 0 && totalBytes > 0) {
            ((downloadedBytes.toDouble() / totalBytes.toDouble()) * 100).toInt()
        } else {
            0
        }
    }

    companion object {
        private const val ROOT_DIR = "invincibility"
    }
}

data class DownloadItem(val progressValue: Int = -1, val status: Int, val reason: FailReason? = null)

enum class FailReason {
    INSUFFICIENT_SPACE,
    HTTP_DATA_ERROR,
    DOWNLOAD_FAILS,
    NO_INTERNET
}