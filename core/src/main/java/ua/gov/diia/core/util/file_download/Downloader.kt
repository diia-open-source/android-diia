package ua.gov.diia.core.util.file_download

import okhttp3.ResponseBody

interface Downloader {

    fun downloadFile(url: String, id: String, title: String?): Long

    fun isFileSaved(id: String): Boolean

    fun isFileInSavingState(id: String): Boolean

    fun deleteDirectory(id: String) : Boolean

    suspend fun subscribeForUpdate(downloadID: Long, statusCallback: (DownloadItem) -> Unit)

    suspend fun clear()

    suspend fun saveFile(fileBody: ResponseBody)

}