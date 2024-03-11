package ua.gov.diia.core.util

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import kotlinx.coroutines.Dispatchers
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import ua.gov.diia.core.rules.MainDispatcherRule

class DiiaDispatcherProviderTest {

    private lateinit var dispatcherProvider: DiiaDispatcherProvider


    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setup() {
        dispatcherProvider = DiiaDispatcherProvider()
    }

    @Test
    fun `ioDispatcher should return Dispatchers IO`() {
        assertEquals(Dispatchers.IO, dispatcherProvider.ioDispatcher())
    }

    @Test
    fun `main should return Dispatchers Main`() {
        assertEquals(Dispatchers.Main, dispatcherProvider.main)
    }

    @Test
    fun `work should return Dispatchers Default`() {
        assertEquals(Dispatchers.Default, dispatcherProvider.work)
    }

}