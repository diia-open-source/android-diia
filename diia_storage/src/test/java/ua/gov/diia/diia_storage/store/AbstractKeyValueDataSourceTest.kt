package ua.gov.diia.diia_storage.store

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.squareup.moshi.JsonAdapter
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import ua.gov.diia.core.util.delegation.WithCrashlytics
import ua.gov.diia.diia_storage.MainDispatcherRule
import ua.gov.diia.diia_storage.model.KeyValueStore
import ua.gov.diia.diia_storage.model.PreferenceKey

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class AbstractKeyValueDataSourceTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()


    lateinit var preferenceKeyMock: PreferenceKey
    lateinit var jsonAdapterMock: JsonAdapter<String>
    lateinit var store: KeyValueStore
    lateinit var withCrashlytics: WithCrashlytics
    lateinit var abstractKeyValueDataSource: AbstractKeyValueDataSource<String>

    @Before
    fun before() {
        preferenceKeyMock = mockk(relaxed = true)
        jsonAdapterMock = mockk(relaxed = true)
        store = mockk(relaxed = true)
        withCrashlytics = mockk(relaxed = true)

        abstractKeyValueDataSource = object : AbstractKeyValueDataSource<String>(store, withCrashlytics) {
            override val preferenceKey: PreferenceKey
                get() = preferenceKeyMock
            override val jsonAdapter: JsonAdapter<String>
                get() = jsonAdapterMock
        }
    }

    @Test
    fun `test saveDataToStore set serialized data into storage`() = runTest {
        every { jsonAdapterMock.toJson("data") } returns "value"

        abstractKeyValueDataSource.saveDataToStore("data")

        coVerify(exactly = 1) { store.set(preferenceKeyMock, "value") }
        coVerify(exactly = 1) { jsonAdapterMock.toJson("data") }
    }

    @Test
    fun `test loadData from store and deserialize data`() = runTest {
        every { store.containsKey(preferenceKeyMock) } returns true
        every { store.getString(preferenceKeyMock, Preferences.DEF) } returns "data"

        abstractKeyValueDataSource.loadData()

        coVerify(exactly = 1) { store.getString(preferenceKeyMock, Preferences.DEF) }
        coVerify(exactly = 1) { jsonAdapterMock.fromJson("data") }
    }

    @Test
    fun `test loadData handle error by setting empty data and call sendNonFatalError`() = runTest {
        val error = RuntimeException("error")
        every { store.containsKey(preferenceKeyMock) } returns true
        every { store.getString(preferenceKeyMock, Preferences.DEF) } throws error

        abstractKeyValueDataSource.loadData()

        coVerify(exactly = 1) { store.set(preferenceKeyMock, "") }
        coVerify(exactly = 1) { withCrashlytics.sendNonFatalError(error)}
    }

    @Test
    fun `test loadData returns null if store not contains data`() = runTest {
        every { store.containsKey(preferenceKeyMock) } returns false

        assertNull(abstractKeyValueDataSource.loadData())
    }
}

