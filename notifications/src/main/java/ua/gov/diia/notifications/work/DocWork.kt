package ua.gov.diia.notifications.work

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.Data
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import androidx.work.Worker
import androidx.work.WorkerParameters
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.MutableStateFlow
import ua.gov.diia.core.di.actions.GlobalActionDeleteDocument
import ua.gov.diia.core.di.actions.GlobalActionUpdateLocalDocument
import ua.gov.diia.core.util.event.UiDataEvent

@HiltWorker
class DocWork @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    @GlobalActionDeleteDocument val globalActionDeleteDocument: MutableStateFlow<UiDataEvent<String>?>,
    @GlobalActionUpdateLocalDocument val globalActionUpdateLocalDocument: MutableStateFlow<UiDataEvent<String>?>
) : Worker(appContext, workerParams) {

    override fun doWork(): Result {
        when (inputData.getString(DOC_WORK_TYPE)) {
            UPDATE_DOC_ENGAGED -> {
                globalActionUpdateLocalDocument.tryEmit(UiDataEvent(inputData.getString(DOC_TYPE) ?: ""))
            }

            DELETE_DOC -> {
                globalActionDeleteDocument.tryEmit(UiDataEvent(inputData.getString(DOC_TYPE) ?: ""))
            }

            else -> {}
        }
        return Result.success()
    }


    companion object {
        private const val DOC_PUSH_WORK = "doc_push_work"
        private const val DOC_WORK_TYPE = "doc_work_type"
        private const val DOC_TYPE = "doc_type"

        // handled silent push types
        private const val UPDATE_DOC_ENGAGED = "updateToEngaged"
        private const val DELETE_DOC = "deleteDocument"

        fun enqueue(workManager: WorkManager, type: String, res: String) {
            val inputData = Data.Builder()
                .putString(DOC_WORK_TYPE, type)
                .putString(DOC_TYPE, res)
                .build()

            val docWork = OneTimeWorkRequest.Builder(DocWork::class.java)
                .setInputData(inputData)
                .build()

            workManager.enqueueUniqueWork(
                DOC_PUSH_WORK,
                ExistingWorkPolicy.REPLACE,
                docWork
            )
        }
    }

}