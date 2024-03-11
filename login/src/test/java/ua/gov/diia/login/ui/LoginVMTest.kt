package ua.gov.diia.login.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.cash.turbine.test
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.clearInvocations
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.never
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.times
import org.mockito.junit.MockitoJUnitRunner
import ua.gov.diia.diia_storage.store.repository.authorization.AuthorizationRepository
import ua.gov.diia.core.models.ActionDataLazy
import ua.gov.diia.core.models.Token
import ua.gov.diia.core.models.dialogs.TemplateDialogModelWithProcessCode
import ua.gov.diia.core.network.apis.ApiAuth
import ua.gov.diia.core.util.CommonConst
import ua.gov.diia.core.util.alert.ClientAlertDialogsFactory
import ua.gov.diia.core.util.delegation.WithBuildConfig
import ua.gov.diia.core.util.delegation.WithCrashlytics
import ua.gov.diia.core.util.delegation.WithErrorHandlingOnFlow
import ua.gov.diia.core.util.delegation.WithRetryLastAction
import ua.gov.diia.core.util.event.UiDataEvent
import ua.gov.diia.core.util.system.application.ApplicationLauncher
import ua.gov.diia.core.util.system.application.InstalledApplicationInfoProvider
import ua.gov.diia.core.util.system.service.SystemServiceProvider
import ua.gov.diia.login.model.LoginToken
import ua.gov.diia.login.network.ApiLogin
import ua.gov.diia.login.rules.MainDispatcherRule
import ua.gov.diia.pin.repository.LoginPinRepository
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.event.UIActionKeysCompose
import ua.gov.diia.ui_base.components.infrastructure.state.UIState
import ua.gov.diia.ui_base.components.molecule.checkbox.CheckboxBorderedMlcData
import ua.gov.diia.ui_base.components.organism.list.ListItemGroupOrgData
import ua.gov.diia.verification.model.VerificationMethodsData
import ua.gov.diia.verification.model.VerificationUrl
import ua.gov.diia.verification.network.ApiVerification
import ua.gov.diia.verification.ui.methods.VerificationMethod
import ua.gov.diia.verification.ui.methods.VerificationRequest


@RunWith(MockitoJUnitRunner::class)
class LoginVMTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val actionLazy = MutableSharedFlow<UiDataEvent<ActionDataLazy>>()

    @Mock
    lateinit var apiAuth: ApiAuth

    @Mock
    lateinit var apiLogin: ApiLogin

    @Mock
    lateinit var loginPinRepository: LoginPinRepository

    @Mock
    lateinit var authorizationRepository: ua.gov.diia.diia_storage.store.repository.authorization.AuthorizationRepository

    @Mock
    lateinit var clientAlertDialogsFactory: ClientAlertDialogsFactory

    @Mock
    lateinit var retryErrorBehavior: WithRetryLastAction

    @Mock
    lateinit var errorHandlingBehaviour: WithErrorHandlingOnFlow

    @Mock
    lateinit var applicationInfoProvider: InstalledApplicationInfoProvider

    @Mock
    lateinit var systemServiceProvider: SystemServiceProvider

    @Mock
    lateinit var applicationLauncher: ApplicationLauncher

    @Mock
    lateinit var withCrashlytics: WithCrashlytics

    @Mock
    lateinit var withBuildConfig: WithBuildConfig

    private lateinit var verificationMethod: VerificationMethod
    private lateinit var unavailableVerificationMethod: VerificationMethod
    private lateinit var apiVerification: ApiVerification
    private lateinit var viewModel: LoginVM

    @Before
    fun setUp() {
        verificationMethod = StubVerificationMethod(
            name = "test",
            isUsesUrl = false,
            isAvailableForAuth = true
        )
        unavailableVerificationMethod = StubVerificationMethod(
            name = "unavailable",
            isUsesUrl = true,
            isAvailableForAuth = false,
        )
        apiVerification = StubApiVerification()
        viewModel = LoginVM(
            actionLazy = actionLazy,
            apiAuth = apiAuth,
            apiVerification = apiVerification,
            apiLogin = apiLogin,
            loginPinRepository = loginPinRepository,
            authorizationRepository = authorizationRepository,
            postLoginActions = emptySet(),
            clientAlertDialogsFactory = clientAlertDialogsFactory,
            retryErrorBehavior = retryErrorBehavior,
            errorHandlingBehaviour = errorHandlingBehaviour,
            applicationInfoProvider = applicationInfoProvider,
            systemServiceProvider = systemServiceProvider,
            applicationLauncher = applicationLauncher,
            verificationMethods = mapOf(
                verificationMethod.name to verificationMethod,
                unavailableVerificationMethod.name to unavailableVerificationMethod,
            ),
            withCrashlytics = withCrashlytics,
            withBuildConfig = withBuildConfig,
        )
    }

    @Test
    fun `open policy`() = runTest {
        viewModel.navigation.test {
            viewModel.onUIAction(UIAction(LoginConst.ACTION_NAVIGATE_TO_POLICY))
            Assert.assertEquals(LoginVM.Navigation.ToPolicy, awaitItem())
        }
    }

    @Test
    fun `policy check box`() = runTest {
        Assert.assertFalse(viewModel.isCheckBoxSelected())
        viewModel.isLoading.first { !it.second }
        viewModel.onUIAction(UIAction(LoginConst.ACTION_CHECKBOX_CHECKED))
        Assert.assertEquals(
            UIState.Selection.Unselected,
            viewModel.bodyData.firstNotNullOf { it as? CheckboxBorderedMlcData }.data.selectionState,
        )
        viewModel.bodyData.firstNotNullOf { it as? ListItemGroupOrgData }.itemsList.forEach {
            Assert.assertEquals(UIState.Interaction.Disabled, it.interactionState)
        }
        Assert.assertFalse(viewModel.isCheckBoxSelected())
        viewModel.onUIAction(UIAction(LoginConst.ACTION_CHECKBOX_CHECKED))
        Assert.assertEquals(
            UIState.Selection.Selected,
            viewModel.bodyData.firstNotNullOf { it as? CheckboxBorderedMlcData }.data.selectionState,
        )
        viewModel.bodyData.firstNotNullOf { it as? ListItemGroupOrgData }.itemsList.forEach {
            Assert.assertEquals(UIState.Interaction.Enabled, it.interactionState)
        }
        Assert.assertTrue(viewModel.isCheckBoxSelected())
    }

    @Test
    fun `full login flow`() = runTest {
        whenever(apiLogin.getAuthenticationToken(any())).thenReturn(LoginToken("testtoken"))
        viewModel.navigateToVerification.test {
            viewModel.onUIAction(
                UIAction(UIActionKeysCompose.LIST_ITEM_GROUP_ORG, data = verificationMethod.name)
            )
            awaitItem()
        }
        viewModel.navigation.test {
            viewModel.completeVerification()
            Assert.assertEquals(LoginVM.Navigation.ToPinCreation, awaitItem())
        }
        // get pin code
        viewModel.navigation.test {
            viewModel.setPinCode("1234")
            Assert.assertEquals(LoginVM.Navigation.ToHome, awaitItem())
        }
        verify(loginPinRepository).setUserAuthorized("1234")
    }

    @Test
    fun `test user auth`() = runTest {
        whenever(apiAuth.getTestToken(any(), any())).thenReturn(Token("token", null))
        val data = ActionDataLazy(
            hard = "senectus",
            hardMap = mapOf("name" to "lkfore")
        )
        val buildTypes = listOf(CommonConst.BUILD_TYPE_DEBUG, CommonConst.BUILD_TYPE_STAGE, CommonConst.BUILD_TYPE_RELEASE)
        for (type in buildTypes) {
            whenever(withBuildConfig.getBuildType()).thenReturn(type)
            clearInvocations(apiAuth)
            val actionLazy = MutableSharedFlow<UiDataEvent<ActionDataLazy>>()
            val vm = LoginVM(
                actionLazy = actionLazy,
                apiAuth = apiAuth,
                apiVerification = apiVerification,
                apiLogin = apiLogin,
                loginPinRepository = loginPinRepository,
                authorizationRepository = authorizationRepository,
                postLoginActions = emptySet(),
                clientAlertDialogsFactory = clientAlertDialogsFactory,
                retryErrorBehavior = retryErrorBehavior,
                errorHandlingBehaviour = errorHandlingBehaviour,
                applicationInfoProvider = applicationInfoProvider,
                systemServiceProvider = systemServiceProvider,
                applicationLauncher = applicationLauncher,
                verificationMethods = mapOf(
                    verificationMethod.name to verificationMethod,
                    unavailableVerificationMethod.name to unavailableVerificationMethod,
                ),
                withCrashlytics = withCrashlytics,
                withBuildConfig = withBuildConfig
            )
            vm.isLoading.first { !it.second }
            vm.navigation.test {
                actionLazy.emit(UiDataEvent(data))
                if (type == CommonConst.BUILD_TYPE_RELEASE) {
                    expectNoEvents()
                } else {
                    Assert.assertEquals(LoginVM.Navigation.ToPinCreation, awaitItem())
                }
            }
            val verificationMode = if (type == CommonConst.BUILD_TYPE_RELEASE) {
                never()
            } else {
                times(1)
            }
            verify(apiAuth, verificationMode).getTestToken(data.hard, data.hardMap)
        }
    }

    private class StubVerificationMethod(
        override val name: String,
        private val isUsesUrl: Boolean,
        override val isAvailableForAuth: Boolean,
    ) : VerificationMethod() {

        override val isAvailable = true
        override val iconResId = 0
        override val titleResId = 0
        override val descriptionResId = 0

        override suspend fun getVerificationRequest(
            verificationSchema: String,
            processId: String
        ) = VerificationRequest(
            navRequest = { _, _ -> mock() },
            url = if (isUsesUrl) {
                VerificationUrl("http://test.com", "tk2", null)
            } else {
                null
            },
            shouldLaunchUrl = isUsesUrl,
        )
    }

    private class StubApiVerification : ApiVerification {

        override suspend fun getVerificationMethods(
            schema: String,
            processId: String?
        ) = VerificationMethodsData(
            title = "Test",
            methods = listOf("test", "unavailable"),
            actionButton = null,
            processId = "dl4lee",
            skipAuthMethods = null,
            template = null,
        )

        override suspend fun getAuthUrl(
            verificationMethodCode: String,
            processId: String,
            bankCode: String?
        ): VerificationUrl = VerificationUrl(
            authUrl = null,
            token = "testtoken",
            template = null,
        )

        override suspend fun completeVerificationStep(
            verificationMethodCode: String,
            requestId: String,
            processId: String,
            bankCode: String?
        ): TemplateDialogModelWithProcessCode = mock()
    }
}