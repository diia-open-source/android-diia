package ua.gov.diia.ps_criminal_cert.util

import androidx.lifecycle.LiveData
import androidx.lifecycle.asFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.mapNotNull
import ua.gov.diia.core.util.event.UiDataEvent

suspend fun <T> LiveData<UiDataEvent<T>>.awaitEvent(): T {
    return asFlow().mapNotNull { it.getContentIfNotHandled() }.first()
}
