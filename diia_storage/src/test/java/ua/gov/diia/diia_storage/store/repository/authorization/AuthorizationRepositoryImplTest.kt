package ua.gov.diia.diia_storage.store.repository.authorization

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import ua.gov.diia.core.models.TokenData
import ua.gov.diia.core.models.UserType
import ua.gov.diia.core.util.DispatcherProvider
import ua.gov.diia.diia_storage.Base64Wrapper
import ua.gov.diia.diia_storage.CommonPreferenceKeys
import ua.gov.diia.diia_storage.DiiaStorage
import ua.gov.diia.diia_storage.MainDispatcherRule
import ua.gov.diia.diia_storage.store.datasource.preferences.PreferenceDataSource
import java.util.Date

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class AuthorizationRepositoryImplTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    lateinit var authorizationRepositoryImpl: AuthorizationRepositoryImpl

    lateinit var preferenceDataSource: PreferenceDataSource
    lateinit var diiaStorage: DiiaStorage
    lateinit var dispatcherProvider: DispatcherProvider
    lateinit var base64Wrapper: Base64Wrapper

    @Before
    fun before() {
        preferenceDataSource = mockk(relaxed = true)
        diiaStorage = mockk(relaxed = true)
        dispatcherProvider = mockk(relaxed = true)
        base64Wrapper = mockk(relaxed = true)

        every { dispatcherProvider.work } returns UnconfinedTestDispatcher()

        authorizationRepositoryImpl = AuthorizationRepositoryImpl(
            preferenceDataSource,
            diiaStorage,
            dispatcherProvider,
            base64Wrapper
        )
    }

    @Test
    fun `setIsServiceUser set data with PREFERENCE_KEY_SERVICE_USER key`() = runTest {
        authorizationRepositoryImpl.setIsServiceUser(true)

        coVerify(exactly = 1) { preferenceDataSource.setBoolean("is_service_user", true) }
    }

    @Test
    fun `isServiceUser get data with PREFERENCE_KEY_SERVICE_USER key`() = runTest {

        coEvery { preferenceDataSource.getBoolean("is_service_user") } returns true

        assertTrue(authorizationRepositoryImpl.isServiceUser())

        coVerify(exactly = 1) { preferenceDataSource.getBoolean("is_service_user") }
    }

    @Test
    fun `getUserType get relevant user type`() = runTest {

        coEvery { preferenceDataSource.getBoolean("is_service_user") } returns true
        assertEquals(UserType.SERVICE_USER, authorizationRepositoryImpl.getUserType())

        coEvery { preferenceDataSource.getBoolean("is_service_user") } returns false
        assertEquals(UserType.PRIMARY_USER, authorizationRepositoryImpl.getUserType())
    }

    @Test
    fun `getMobileUuid load data from storage`() = runTest {
        val mobileId = "mobile_id"

        coEvery { diiaStorage.getMobileUuid() } returns mobileId
        val result = authorizationRepositoryImpl.getMobileUuid()

        coVerify(exactly = 1) { diiaStorage.getMobileUuid() }
        assertEquals(mobileId, result)
    }

    @Test
    fun `setMobileUuid save id in storage`() = runTest {
        val mobileId = "mobile_id"

        authorizationRepositoryImpl.setMobileUuid(mobileId)

        coVerify(exactly = 1) { diiaStorage.set(CommonPreferenceKeys.UUID, mobileId) }
    }

    @Test
    fun `isUserAuthorized check if storage has token`() = runTest {
        coEvery { diiaStorage.containsKey(CommonPreferenceKeys.Token) } returns true

        assertTrue(authorizationRepositoryImpl.isUserAuthorized())

        coVerify(exactly = 1) { diiaStorage.containsKey(CommonPreferenceKeys.Token) }
    }

    @Test
    fun `logoutUser trigger logout in storage`() = runTest {
        authorizationRepositoryImpl.logoutUser()

        coVerify(exactly = 1) { diiaStorage.userLogOut() }
    }

    @Test
    fun `getTokenData returns empty token if token is not saved in storage`() = runTest {
        every {
            diiaStorage.getString(
                CommonPreferenceKeys.Token,
                "def_string"
            )
        } returns "def_string"
        val result = authorizationRepositoryImpl.getTokenData()

        assertEquals(TokenData.EMPTY_TOKEN, result.token)
    }

    @Test
    fun `getTokenData returns token decode and split`() = runTest {
        val token =
            "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9" +
                    "lIiwiaWF0IjoxNTE2MjM5MDIyLCJleHAiOjE2MTYyMzkwMjJ9.SflKxwRJSMeKKF2QT4fwpMeJf" +
                    "36POk6yJV_adQssw5c"
        val expectedTokenData = TokenData(token, Date(1616239022000))
        coEvery { diiaStorage.getString(CommonPreferenceKeys.Token, "def_string") } returns token
        val byteArray = "{\n" +
                "  \"sub\": \"1234567890\",\n" +
                "  \"name\": \"John Doe\",\n" +
                "  \"iat\": 1516239022,\n" +
                "  \"exp\": 1616239022\n" +
                "}"
        coEvery { base64Wrapper.decode(token.split(".")[1].toByteArray()) } returns byteArray.toByteArray()

        val result = authorizationRepositoryImpl.getTokenData()

        coVerify(exactly = 1) { diiaStorage.getString(CommonPreferenceKeys.Token, "def_string") }
        coVerify(exactly = 1) { base64Wrapper.decode(token.split(".")[1].toByteArray()) }
        assertEquals(expectedTokenData, result)
    }

    @Test
    fun `getToken returns null if token is not saved in storage`() = runTest {
        coEvery { diiaStorage.getString(CommonPreferenceKeys.Token, "def_string") } returns "def_string"
        val result = authorizationRepositoryImpl.getToken()

        coVerify(exactly = 1) { diiaStorage.getString(CommonPreferenceKeys.Token, "def_string") }
        assertNull(result)
    }

    @Test
    fun `getToken returns token from storage`() = runTest {
        val token = "token"
        coEvery { diiaStorage.getString(CommonPreferenceKeys.Token, "def_string") } returns token
        val result = authorizationRepositoryImpl.getToken()

        coVerify(exactly = 1) { diiaStorage.getString(CommonPreferenceKeys.Token, "def_string") }
        assertEquals(token, result)
    }

    @Test
    fun `setToken save toekn in storage`() = runTest {
        val token = "token"
        authorizationRepositoryImpl.setToken(token)

        coVerify(exactly = 1) { diiaStorage.set(CommonPreferenceKeys.Token, token) }
    }
}