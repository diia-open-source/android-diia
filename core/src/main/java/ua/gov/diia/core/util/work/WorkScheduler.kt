package ua.gov.diia.core.util.work

import androidx.work.WorkManager

interface WorkScheduler {

    fun enqueue(workManager: WorkManager)
}