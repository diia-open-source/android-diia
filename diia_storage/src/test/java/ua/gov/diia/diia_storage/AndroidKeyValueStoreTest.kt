package ua.gov.diia.diia_storage

import android.content.SharedPreferences
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import ua.gov.diia.diia_storage.model.PreferenceKey

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class AndroidKeyValueStoreTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    lateinit var androidKeyValueStoreImpl: AndroidKeyValueStoreImpl
    lateinit var sharedPreferences: SharedPreferences
    lateinit var allowedScopes: Set<String>

    @Before
    fun before() {
        sharedPreferences = mockk(relaxed = true)
        allowedScopes = mutableSetOf("scope")

        androidKeyValueStoreImpl = AndroidKeyValueStoreImpl(sharedPreferences, allowedScopes)
    }

    @Test
    fun `test getBoolean get data from preferences if key is in scope`() = runTest {
        val key: PreferenceKey = mockk(relaxed = true)
        every { key.name } returns "key"
        every { key.scope } returns "scope"

        every { sharedPreferences.getBoolean("key", true) } returns false

        assertFalse(androidKeyValueStoreImpl.getBoolean(key, true))

        coVerify(exactly = 1) { sharedPreferences.getBoolean("key", true) }
    }


    @Test(expected = IllegalAccessException::class)
    fun `test getBoolean not get data from preferences if key is not in scope`() = runTest {
        val key: PreferenceKey = mockk(relaxed = true)
        every { key.name } returns "key"
        every { key.scope } returns "scope2"

        androidKeyValueStoreImpl.getBoolean(key, true)
    }

    @Test
    fun `test getFloat get data from preferences if key is in scope`() = runTest {
        val key: PreferenceKey = mockk(relaxed = true)
        every { key.name } returns "key"
        every { key.scope } returns "scope"

        every { sharedPreferences.getFloat("key", 0f) } returns 1f

        assertEquals(1f, androidKeyValueStoreImpl.getFloat(key, 0f))

        coVerify(exactly = 1) { sharedPreferences.getFloat("key", 0f) }
    }


    @Test(expected = IllegalAccessException::class)
    fun `test getFloat not get data from preferences if key is not in scope`() = runTest {
        val key: PreferenceKey = mockk(relaxed = true)
        every { key.name } returns "key"
        every { key.scope } returns "scope2"

        androidKeyValueStoreImpl.getFloat(key, 0f)
    }

    @Test
    fun `test getLong get data from preferences if key is in scope`() = runTest {
        val key: PreferenceKey = mockk(relaxed = true)
        every { key.name } returns "key"
        every { key.scope } returns "scope"

        every { sharedPreferences.getLong("key", 0) } returns 1

        assertEquals(1, androidKeyValueStoreImpl.getLong(key, 0))

        coVerify(exactly = 1) { sharedPreferences.getLong("key", 0) }
    }


    @Test(expected = IllegalAccessException::class)
    fun `test getLong not get data from preferences if key is not in scope`() = runTest {
        val key: PreferenceKey = mockk(relaxed = true)
        every { key.name } returns "key"
        every { key.scope } returns "scope2"

        androidKeyValueStoreImpl.getLong(key, 0)
    }

    @Test
    fun `test getString get data from preferences if key is in scope`() = runTest {
        val key: PreferenceKey = mockk(relaxed = true)
        every { key.name } returns "key"
        every { key.scope } returns "scope"

        every { sharedPreferences.getString("key", "empty") } returns "data"

        assertEquals("data", androidKeyValueStoreImpl.getString(key, "empty"))

        coVerify(exactly = 1) { sharedPreferences.getString("key", "empty") }
    }


    @Test(expected = IllegalAccessException::class)
    fun `test getString not get data from preferences if key is not in scope`() = runTest {
        val key: PreferenceKey = mockk(relaxed = true)
        every { key.name } returns "key"
        every { key.scope } returns "scope2"

        androidKeyValueStoreImpl.getString(key, "empty")
    }

    @Test
    fun `test getInt get data from preferences if key is in scope`() = runTest {
        val key: PreferenceKey = mockk(relaxed = true)
        every { key.name } returns "key"
        every { key.scope } returns "scope"

        every { sharedPreferences.getInt("key", 0) } returns 1

        assertEquals(1, androidKeyValueStoreImpl.getInt(key, 0))

        coVerify(exactly = 1) { sharedPreferences.getInt("key", 0) }
    }

    @Test(expected = IllegalAccessException::class)
    fun `test getInt not get data from preferences if key is not in scope`() = runTest {
        val key: PreferenceKey = mockk(relaxed = true)
        every { key.name } returns "key"
        every { key.scope } returns "scope2"

        androidKeyValueStoreImpl.getInt(key, 0)
    }

    @Test
    fun `test contains check datain shared preferences`() = runTest {
        val key: PreferenceKey = mockk(relaxed = true)
        every { key.name } returns "key"
        every { key.scope } returns "scope2"

        androidKeyValueStoreImpl.containsKey(key)
        verify(exactly = 1) { sharedPreferences.contains("key") }
    }

    @Test
    fun `test clear call clear function in shared preference`() = runTest {
        val editor: SharedPreferences.Editor = mockk(relaxed = true)
        every { sharedPreferences.edit() } returns editor
        every { editor.clear() } returns editor

        androidKeyValueStoreImpl.clear()
        verify(exactly = 1) { sharedPreferences.edit() }
        verify(exactly = 1) { editor.clear() }
        verify(exactly = 1) { editor.commit() }
    }

    @Test(expected = IllegalAccessException::class)
    fun `test set check scope`() = runTest {
        val key: PreferenceKey = mockk(relaxed = true)
        every { key.name } returns "key"
        every { key.scope } returns "scope2"

        androidKeyValueStoreImpl.set(key, "data")
    }

    @Test
    fun `test delete by ket in shared preference`() = runTest {
        val editor: SharedPreferences.Editor = mockk(relaxed = true)
        every { sharedPreferences.edit() } returns editor
        every { editor.remove(any()) } returns editor
        val key: PreferenceKey = mockk(relaxed = true)
        every { key.name } returns "key"
        every { key.scope } returns "scope"

        androidKeyValueStoreImpl.delete(key)
        verify(exactly = 1) { sharedPreferences.edit() }
        verify(exactly = 1) { editor.remove("key") }
        verify(exactly = 1) { editor.apply() }
    }

    @Test
    fun `test set save data in integer put method corresponded put method in shared preference`() = runTest {
        val editor: SharedPreferences.Editor = mockk(relaxed = true)
        every { sharedPreferences.edit() } returns editor
        every { editor.putBoolean(any(), any()) } returns editor
        every { editor.putInt(any(), any()) } returns editor
        every { editor.putFloat(any(), any()) } returns editor
        every { editor.putLong(any(), any()) } returns editor
        every { editor.putString(any(), any()) } returns editor
        val key: PreferenceKey = mockk(relaxed = true)
        every { key.name } returns "key"
        every { key.scope } returns "scope"


        every { key.dataType } returns Int::class.java
        val intValue = 1
        androidKeyValueStoreImpl.set(key, intValue)
        coVerify(exactly = 1) { sharedPreferences.edit() }
        coVerify(exactly = 1) { editor.putInt("key", intValue) }
        coVerify(exactly = 1) { editor.apply() }
    }

    @Test
    fun `test set save data in float put method corresponded put method in shared preference`() = runTest {
        val editor: SharedPreferences.Editor = mockk(relaxed = true)
        every { sharedPreferences.edit() } returns editor
        every { editor.putFloat(any(), any()) } returns editor
        val key: PreferenceKey = mockk(relaxed = true)
        every { key.name } returns "key"
        every { key.scope } returns "scope"

        every { key.dataType } returns Float::class.java
        androidKeyValueStoreImpl.set(key, 1f)
        coVerify(exactly = 1) { sharedPreferences.edit() }
        coVerify(exactly = 1) { editor.putFloat("key", 1f) }
        coVerify(exactly = 1) { editor.apply() }
    }

    @Test
    fun `test set save data in long put method corresponded put method in shared preference`() = runTest {
        val editor: SharedPreferences.Editor = mockk(relaxed = true)
        every { sharedPreferences.edit() } returns editor
        every { editor.putLong(any(), any()) } returns editor
        val key: PreferenceKey = mockk(relaxed = true)
        every { key.name } returns "key"
        every { key.scope } returns "scope"

        every { key.dataType } returns Long::class.java
        androidKeyValueStoreImpl.set(key, 1L)
        coVerify(exactly = 1) { sharedPreferences.edit() }
        coVerify(exactly = 1) { editor.putLong("key", 1L) }
        coVerify(exactly = 1) { editor.apply() }

    }

    @Test
    fun `test set save data in boolean put method corresponded put method in shared preference`() = runTest {
        val editor: SharedPreferences.Editor = mockk(relaxed = true)
        every { sharedPreferences.edit() } returns editor
        every { editor.putBoolean(any(), any()) } returns editor
        val key: PreferenceKey = mockk(relaxed = true)
        every { key.name } returns "key"
        every { key.scope } returns "scope"

        every { key.dataType } returns Boolean::class.java
        androidKeyValueStoreImpl.set(key, true)
        coVerify(exactly = 1) { sharedPreferences.edit() }
        coVerify(exactly = 1) { editor.putBoolean("key", true) }
        coVerify(exactly = 1) { editor.apply() }
    }

    @Test
    fun `test set save data in string put method corresponded put method in shared preference`() = runTest {
        val editor: SharedPreferences.Editor = mockk(relaxed = true)
        every { sharedPreferences.edit() } returns editor
        every { editor.putString(any(), any()) } returns editor
        val key: PreferenceKey = mockk(relaxed = true)
        every { key.name } returns "key"
        every { key.scope } returns "scope"

        every { key.dataType } returns String::class.java
        androidKeyValueStoreImpl.set(key, "data")
        coVerify(exactly = 1) { sharedPreferences.edit() }
        coVerify(exactly = 1) { editor.putString("key", "data") }
        coVerify(exactly = 1) { editor.apply() }

    }

    @Test
    fun `test set save data in other object put method corresponded put method in shared preference`() = runTest {
        val editor: SharedPreferences.Editor = mockk(relaxed = true)
        every { sharedPreferences.edit() } returns editor
        every { editor.putString(any(), any()) } returns editor
        val key: PreferenceKey = mockk(relaxed = true)
        every { key.name } returns "key"
        every { key.scope } returns "scope"

        every { key.dataType } returns Object::class.java
        val obj = Object()
        androidKeyValueStoreImpl.set(key, obj)
        coVerify(exactly = 1) { sharedPreferences.edit() }
        coVerify(exactly = 1) { editor.putString("key", obj.toString()) }
        coVerify(exactly = 1) { editor.apply() }
    }
}

class AndroidKeyValueStoreImpl(val preferences: SharedPreferences, val allowedScopes: Set<String>): AndroidKeyValueStore() {
    override fun getSharedPreferences(): SharedPreferences {
        return preferences
    }

    override fun allowedScopes(): Set<String> {
        return allowedScopes
    }

}