package ua.gov.diia.core.util

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

interface DispatcherProvider {

    fun ioDispatcher(): CoroutineDispatcher

    val main: CoroutineDispatcher

    val work: CoroutineDispatcher
}

class DiiaDispatcherProvider @Inject constructor() : DispatcherProvider {

    override fun ioDispatcher(): CoroutineDispatcher = Dispatchers.IO

    override val main: CoroutineDispatcher
        get() = Dispatchers.Main

    override val work: CoroutineDispatcher
        get() = Dispatchers.Default

}