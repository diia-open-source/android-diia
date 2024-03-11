package ua.gov.diia.verification.ui.controller

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.cash.turbine.test
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.anyOrNull
import com.nhaarman.mockitokotlin2.never
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import org.junit.runners.Parameterized.Parameters
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import ua.gov.diia.core.models.dialogs.TemplateDialogModelWithProcessCode
import ua.gov.diia.core.util.alert.ClientAlertDialogsFactory
import ua.gov.diia.core.util.delegation.WithErrorHandlingOnFlow
import ua.gov.diia.core.util.delegation.WithRetryLastAction
import ua.gov.diia.core.util.system.application.ApplicationLauncher
import ua.gov.diia.core.util.system.application.InstalledApplicationInfoProvider
import ua.gov.diia.core.util.system.service.SystemServiceProvider
import ua.gov.diia.verification.model.ActivityViewActionButton
import ua.gov.diia.verification.model.VerificationFlowResult
import ua.gov.diia.verification.model.VerificationMethodsData
import ua.gov.diia.verification.model.VerificationResult
import ua.gov.diia.verification.network.ApiVerification
import ua.gov.diia.verification.rules.MainDispatcherRule
import ua.gov.diia.verification.ui.VerificationSchema
import ua.gov.diia.verification.ui.methods.VerificationMethod
import ua.gov.diia.verification.util.StubErrorHandlerOnFlow
import ua.gov.diia.verification.util.StubVerificationMethod
import ua.gov.diia.verification.util.dummyTemplateDialog

@RunWith(Parameterized::class)
class VerificationControllerOnFlowVMTest(
    private val schema: String,
) {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    lateinit var clientAlertDialogsFactory: ClientAlertDialogsFactory

    @Mock
    lateinit var retryErrorBehavior: WithRetryLastAction

    @Mock
    lateinit var applicationInfoProvider: InstalledApplicationInfoProvider

    @Mock
    lateinit var systemServiceProvider: SystemServiceProvider

    @Mock
    lateinit var applicationLauncher: ApplicationLauncher

    @Mock
    lateinit var apiVerification: ApiVerification

    private val onVerificationCompleted = MutableSharedFlow<VerificationResult>()
    private lateinit var errorHandlingBehaviour: WithErrorHandlingOnFlow
    private lateinit var verificationMethods: List<VerificationMethod>
    private lateinit var viewModel: TestVerificationControllerOnFlowVM

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        errorHandlingBehaviour = StubErrorHandlerOnFlow()
        verificationMethods = listOf(
            StubVerificationMethod(isUsesUrl = false, name = "test_nav", isAvailable = true),
            StubVerificationMethod(isUsesUrl = true, name = "test_url", isAvailable = true),
            StubVerificationMethod(isUsesUrl = false, name = "test_disabled", isAvailable = false)
        )
        viewModel = TestVerificationControllerOnFlowVM(
            apiVerification = apiVerification,
            clientAlertDialogsFactory = clientAlertDialogsFactory,
            retryErrorBehavior = retryErrorBehavior,
            errorHandlingBehaviour = errorHandlingBehaviour,
            applicationInfoProvider = applicationInfoProvider,
            systemServiceProvider = systemServiceProvider,
            applicationLauncher = applicationLauncher,
            verificationMethods = verificationMethods.associateBy { it.name },
            onVerificationCompleted = onVerificationCompleted,
        )
    }

    @Test
    fun `get single verification method`() = runTest {
        whenever(apiVerification.getVerificationMethods(any(), anyOrNull()))
            .thenReturn(
                VerificationMethodsData(
                    title = null,
                    methods = listOf(verificationMethods[0].name),
                    actionButton = null,
                    processId = "1234",
                    skipAuthMethods = null,
                    template = null
                )
            )
        viewModel.navigateToVerification.test {
            viewModel.startVerification(schema)
            Assert.assertNotNull(awaitItem().peekContent())
        }
    }

    @Test
    fun `multiple verification methods`() = runTest {
        whenever(apiVerification.getVerificationMethods(any(), anyOrNull()))
            .thenReturn(
                VerificationMethodsData(
                    title = null,
                    methods = verificationMethods.map { it.name },
                    actionButton = ActivityViewActionButton("test"),
                    processId = "1234",
                    skipAuthMethods = null,
                    template = null
                )
            )
        viewModel.navigateToMethodsSelectionDialog.test {
            viewModel.startVerification(schema)
            val request = awaitItem().peekContent()
            Assert.assertEquals(2, request.methods.size)
            Assert.assertEquals(schema, request.schema)
        }
    }

    @Test
    fun `no available verification methods`() = runTest {
        whenever(apiVerification.getVerificationMethods(any(), anyOrNull()))
            .thenReturn(
                VerificationMethodsData(
                    title = null,
                    methods = listOf(verificationMethods[2].name),
                    actionButton = null,
                    processId = "1234",
                    skipAuthMethods = null,
                    template = null
                )
            )
        whenever(clientAlertDialogsFactory.getNoVerificationMethodsDialog())
            .thenReturn(dummyTemplateDialog())
        viewModel.showTemplateDialog.test {
            viewModel.loadData(schema)
            viewModel.processVerificationMethods()
            Assert.assertEquals(
                dummyTemplateDialog(VerificationControllerConst.VERIFICATION_ALERT_DIALOG_ACTION),
                awaitItem().peekContent()
            )
            verify(clientAlertDialogsFactory).getNoVerificationMethodsDialog()
        }
    }

    @Test
    fun `external verification`() = runTest {
        val methodName = verificationMethods[1].name
        whenever(apiVerification.getVerificationMethods(any(), anyOrNull()))
            .thenReturn(
                VerificationMethodsData(
                    title = null,
                    methods = listOf(methodName),
                    actionButton = null,
                    processId = "1234",
                    skipAuthMethods = null,
                    template = null
                )
            )
        whenever(apiVerification.completeVerificationStep(any(), any(), any(), anyOrNull()))
            .thenReturn(
                TemplateDialogModelWithProcessCode(
                    processCode = null,
                    template = dummyTemplateDialog()
                )
            )

        // check for noop
        viewModel.completeBankAppVerificationStep()
        Assert.assertFalse(viewModel.verifyingUserValue)

        viewModel.cleanUpAndLaunchVerificationMethod(
            schema,
            VerificationFlowResult.VerificationMethod(methodName)
        )
        viewModel.awaitUserVerifying()
        Assert.assertEquals(methodName, viewModel.preferredVerificationMethodCodeVar)
        verify(applicationLauncher).launch("http://test.com/1329")

        viewModel.showTemplateDialog.test {
            viewModel.completeBankAppVerificationStep()
            Assert.assertEquals(
                dummyTemplateDialog(key = VerificationControllerConst.VERIFICATION_ALERT_DIALOG_ACTION),
                awaitItem().getContentIfNotHandled()
            )
        }
    }

    @Test
    fun `complete verification`() = runTest {
        val processId = "14342"
        val method = verificationMethods[1]
        whenever(apiVerification.getVerificationMethods(any(), anyOrNull()))
            .thenReturn(
                VerificationMethodsData(
                    title = null,
                    methods = verificationMethods.map { it.name },
                    actionButton = null,
                    processId = processId,
                    skipAuthMethods = false,
                    template = null
                )
            )
        // check for noop
        viewModel.handleVerificationResult(
            VerificationFlowResult.CompleteVerificationStep(
                "123",
                "2102"
            )
        )
        Assert.assertFalse(viewModel.verifyingUserValue)

        viewModel.cleanUpAndLaunchVerificationMethod(
            schema,
            VerificationFlowResult.VerificationMethod(method.name)
        )
        viewModel.awaitUserVerifying()
        onVerificationCompleted.test {
            viewModel.completeVerification()
            val expected = if (schema == VerificationSchema.GENERATE_SIGNATURE) {
                VerificationResult.GenerateSignature("1329", processId)
            } else {
                VerificationResult.Common(processId)
            }
            Assert.assertEquals(expected, awaitItem())
        }

        onVerificationCompleted.test {
            viewModel.completedVerifyResidentPermit()
            Assert.assertEquals(VerificationResult.Common(processId), awaitItem())
        }
    }

    @Test
    fun `non-production user`() = runTest {
        val processId = "3243s00"
        whenever(apiVerification.getVerificationMethods(any(), anyOrNull()))
            .thenReturn(
                VerificationMethodsData(
                    title = null,
                    methods = null,
                    actionButton = null,
                    processId = processId,
                    skipAuthMethods = true,
                    template = null
                )
            )
        whenever(apiVerification.completeVerificationStep(any(), any(), any(), anyOrNull()))
            .thenReturn(
                TemplateDialogModelWithProcessCode(
                    processCode = 1340,
                    template = dummyTemplateDialog()
                )
            )

        onVerificationCompleted.test {
            viewModel.startVerification(schema)
            if (schema == VerificationSchema.GENERATE_SIGNATURE) {
                expectNoEvents()
            } else {
                Assert.assertEquals(VerificationResult.Common(processId), awaitItem())
            }
        }
        viewModel.awaitUserVerifying()
        Assert.assertTrue(viewModel.authMethodSkippedVar)
    }

    @Test
    fun `no methods`() = runTest {
        val processId = "3243s00"
        whenever(apiVerification.getVerificationMethods(any(), anyOrNull()))
            .thenReturn(
                VerificationMethodsData(
                    title = null,
                    methods = null,
                    actionButton = null,
                    processId = processId,
                    skipAuthMethods = false,
                    template = null
                )
            )
        whenever(clientAlertDialogsFactory.getNoVerificationMethodsDialog())
            .thenReturn(dummyTemplateDialog())

        viewModel.showTemplateDialog.test {
            viewModel.startVerification(schema)
            Assert.assertEquals(
                dummyTemplateDialog(VerificationControllerConst.VERIFICATION_ALERT_DIALOG_ACTION),
                awaitItem().peekContent()
            )
        }
    }

    @Test
    fun `invalid method url`() = runTest {
        val method = verificationMethods[1]
        (method as StubVerificationMethod).authUrl = "http://test.com/"
        whenever(apiVerification.getVerificationMethods(any(), anyOrNull()))
            .thenReturn(
                VerificationMethodsData(
                    title = null,
                    methods = listOf(method.name),
                    actionButton = null,
                    processId = "1234",
                    skipAuthMethods = null,
                    template = null
                )
            )
        viewModel.cleanUpAndLaunchVerificationMethod(
            schema,
            VerificationFlowResult.VerificationMethod(method.name)
        )
        viewModel.awaitUserVerifying()
        viewModel.handleVerificationResult(VerificationFlowResult.CompleteVerificationStep(null, null))
        Assert.assertFalse(viewModel.verifyingUserValue)
    }

    @Test
    fun `unsupported method`() = runTest {
        val methodName = "nosuchmethod"
        whenever(apiVerification.getVerificationMethods(any(), anyOrNull()))
            .thenReturn(
                VerificationMethodsData(
                    title = null,
                    methods = listOf(methodName),
                    actionButton = null,
                    processId = "1234",
                    skipAuthMethods = null,
                    template = null
                )
            )
        whenever(clientAlertDialogsFactory.getNoVerificationMethodsDialog())
            .thenReturn(dummyTemplateDialog())
        viewModel.showTemplateDialog.test {
            viewModel.startVerification(schema)
            viewModel.awaitUserVerifying()
            viewModel.handleVerificationResult(VerificationFlowResult.VerificationMethod(methodName))
            viewModel.awaitUserVerifying()
            Assert.assertEquals(
                dummyTemplateDialog(key = VerificationControllerConst.VERIFICATION_ALERT_DIALOG_ACTION),
                awaitItem().getContentIfNotHandled()
            )
        }
        verify(applicationLauncher, never()).launch(any())
    }

    companion object {

        @JvmStatic
        @Parameters
        fun parameters() = listOf(
            VerificationSchema.PROLONG,
            VerificationSchema.GENERATE_SIGNATURE,
            VerificationSchema.SIGNING,
            VerificationSchema.AUTHORIZATION,
        )
    }
}
