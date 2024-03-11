package ua.gov.diia.search.util

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.test.StandardTestDispatcher
import ua.gov.diia.core.util.DispatcherProvider

class TestDispatcherProvider : DispatcherProvider {

    private val testDispatcher = StandardTestDispatcher()

    override fun ioDispatcher(): CoroutineDispatcher = StandardTestDispatcher()
    override val main: CoroutineDispatcher = StandardTestDispatcher()
    override val work: CoroutineDispatcher = StandardTestDispatcher()
}
