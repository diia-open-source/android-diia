package ua.gov.diia.splash

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.work.WorkManager
import app.cash.turbine.test
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.justRun
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import ua.gov.diia.core.models.Token
import ua.gov.diia.core.models.UserType
import ua.gov.diia.core.models.dialogs.TemplateDialogModel
import ua.gov.diia.core.network.apis.ApiAuth
import ua.gov.diia.core.util.delegation.WithCrashlytics
import ua.gov.diia.core.util.delegation.WithErrorHandlingOnFlow
import ua.gov.diia.core.util.delegation.WithRetryLastAction
import ua.gov.diia.core.util.work.WorkScheduler
import ua.gov.diia.diia_storage.store.repository.authorization.AuthorizationRepository
import ua.gov.diia.splash.helper.SplashHelper
import ua.gov.diia.splash.rules.MainDispatcherRule
import ua.gov.diia.splash.ui.SplashFVM
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction

private const val ANIMATION_END_KEY = "animation_end_key"

class SplashFVMTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val apiAuth = mockk<ApiAuth>()

    private val authorizationRepository = mockk<AuthorizationRepository>(relaxed = true)

    private val splashHelper = mockk<SplashHelper>(relaxed = true)

    private val workManager = mockk<WorkManager>()

    private val workToSchedule = mockk<WorkScheduler>()

    private val withErrorHandlingOnFlow = mockk<WithErrorHandlingOnFlow>(relaxed = true)

    private val withRetryLastAction = mockk<WithRetryLastAction>(relaxed = true)

    private val withCrashlytics = mockk<WithCrashlytics>()

    private lateinit var viewModel: SplashFVM

    @Before
    fun before() {
        viewModel = SplashFVM(
            apiAuth = apiAuth,
            authorizationRepository = authorizationRepository,
            splashHelper = splashHelper,
            workManager = workManager,
            errorHandling = withErrorHandlingOnFlow,
            retryLastAction = withRetryLastAction,
            crashlytics = withCrashlytics,
            worksToSchedule = setOf(workToSchedule)
        )
    }

    @Test
    fun `init with service user success`() = runTest {
        val serviceUserId = "123"
        val mobileUid = "321"
        val serviceUserToken = mockk<Token>()
        val tokenData = "token"
        coEvery { authorizationRepository.getMobileUuid() } returns mobileUid
        coEvery {
            apiAuth.getServiceAccountToken(
                serviceUserId,
                mobileUid
            )
        } returns serviceUserToken
        every { serviceUserToken.token } returns tokenData
        every { serviceUserToken.template } returns null
        justRun { workToSchedule.enqueue(workManager) }
        viewModel.navigation.test {
            viewModel.doInit(
                serviceUserUuid = serviceUserId,
                skipInitialization = false
            )
            viewModel.onUIAction(UIAction(actionKey = ANIMATION_END_KEY))
            Assert.assertEquals(awaitItem(), SplashFVM.Navigation.ToPinCreation)
        }
        viewModel.navigation.test {
            val pin = "1234"
            viewModel.setServiceUserPin(pin)
            coVerify(exactly = 1) { authorizationRepository.setToken(any()) }
            coVerify(exactly = 1) { authorizationRepository.setIsServiceUser(true) }
            coVerify(exactly = 1) { splashHelper.setUserAuthorized(pin) }
            Assert.assertEquals(awaitItem(), SplashFVM.Navigation.ToQrScanner)
        }
    }

    @Test
    fun `init with service user failure`() = runTest {
        val serviceUserId = "123"
        val mobileUid = "321"
        val serviceUserToken = mockk<Token>()
        val tokenData = "token"
        val template = mockk<TemplateDialogModel>()
        coEvery { authorizationRepository.getMobileUuid() } returns mobileUid
        coEvery {
            apiAuth.getServiceAccountToken(
                serviceUserId,
                mobileUid
            )
        } returns serviceUserToken
        every { serviceUserToken.token } returns tokenData
        every { serviceUserToken.template } returns template
        justRun { workToSchedule.enqueue(workManager) }
        viewModel.doInit(
            serviceUserUuid = serviceUserId,
            skipInitialization = false
        )
        viewModel.onUIAction(UIAction(actionKey = ANIMATION_END_KEY))
        verify(atLeast = 1) { withCrashlytics.sendNonFatalError(any()) }
    }

    @Test
    fun `init with skip initialization for service user with protection`() = runTest {
        coEvery { authorizationRepository.getUserType() } returns UserType.SERVICE_USER
        coEvery { splashHelper.isProtectionExists() } returns true
        viewModel.navigation.test {
            viewModel.doInit(
                serviceUserUuid = null,
                skipInitialization = true
            )
            viewModel.onUIAction(UIAction(actionKey = ANIMATION_END_KEY))
            Assert.assertEquals(awaitItem(), SplashFVM.Navigation.ToProtection)
        }
    }

    @Test
    fun `init with skip initialization for primary user with protection`() = runTest {
        coEvery { authorizationRepository.getUserType() } returns UserType.PRIMARY_USER
        coEvery { splashHelper.isProtectionExists() } returns true
        viewModel.navigation.test {
            viewModel.doInit(
                serviceUserUuid = null,
                skipInitialization = true
            )
            viewModel.onUIAction(UIAction(actionKey = ANIMATION_END_KEY))
            Assert.assertEquals(awaitItem(), SplashFVM.Navigation.ToProtection)
        }
    }

    @Test
    fun `init with skip initialization for primary user without protection`() = runTest {
        coEvery { authorizationRepository.getUserType() } returns UserType.PRIMARY_USER
        coEvery { splashHelper.isProtectionExists() } returns false
        viewModel.navigation.test {
            viewModel.doInit(
                serviceUserUuid = null,
                skipInitialization = true
            )
            viewModel.onUIAction(UIAction(actionKey = ANIMATION_END_KEY))
            Assert.assertEquals(awaitItem(), SplashFVM.Navigation.ToLogin)
        }
    }

    @Test
    fun `test null check in setServiceUserPin`() = runBlocking {
        val pin = "1234"
        viewModel.setServiceUserPin(pin)
        coVerify(exactly = 0) { authorizationRepository.setToken(any()) }
        coVerify(exactly = 0) { authorizationRepository.setIsServiceUser(true) }
        coVerify(exactly = 0) { splashHelper.setUserAuthorized(pin) }
    }

    @After
    fun after() {
        clearAllMocks()
    }
}