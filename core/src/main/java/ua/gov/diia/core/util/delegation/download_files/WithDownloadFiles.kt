package ua.gov.diia.core.util.delegation.download_files

import android.net.Uri
import androidx.fragment.app.Fragment
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LiveData

sealed class DownloadFilesResult {
    data class Success(val uris: List<Uri>) : DownloadFilesResult()
    data class Failed(val exception: Exception) : DownloadFilesResult()
}

interface WithDownloadFiles<Request> {

    suspend fun <T : Fragment> T.downloadFiles(request: Request): DownloadFilesResult

    val saving: LiveData<Boolean>
}

interface WithDownloadFile<Request> : DefaultLifecycleObserver {

    fun <T : Fragment> T.downloadFile(request: Request)

    val saving: LiveData<Boolean>
}

