package ua.gov.diia.pin.ui.create.confirm

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.cash.turbine.test
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.whenever
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
import ua.gov.diia.core.models.dialogs.TemplateDialogButton
import ua.gov.diia.core.models.dialogs.TemplateDialogData
import ua.gov.diia.core.models.dialogs.TemplateDialogModel
import ua.gov.diia.core.ui.dynamicdialog.ActionsConst
import ua.gov.diia.core.util.alert.ClientAlertDialogsFactory
import ua.gov.diia.core.util.delegation.WithRetryLastAction
import ua.gov.diia.pin.helper.PinHelper
import ua.gov.diia.pin.model.CreatePinFlowType
import ua.gov.diia.pin.rules.MainDispatcherRule
import ua.gov.diia.pin.util.AndroidClientAlertDialogsFactory.Companion.CONFIRM_PIN
import ua.gov.diia.pin.utils.StubErrorHandlerOnFlow
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.event.UIActionKeysCompose
import ua.gov.diia.ui_base.components.molecule.text.TextLabelMlcData
import ua.gov.diia.ui_base.components.organism.header.TopGroupOrgData
import ua.gov.diia.ui_base.components.organism.tile.NumButtonTileOrganismData

@RunWith(Parameterized::class)
class ConfirmPinVMTest(
    private val flowType: CreatePinFlowType
) {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var alertDialogsFactory: ClientAlertDialogsFactory

    @Mock
    private lateinit var retryLastAction: WithRetryLastAction

    @Mock
    private lateinit var pinHelper: PinHelper

    private lateinit var viewModel: ConfirmPinVM

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        viewModel = ConfirmPinVM(
            pinHelper = pinHelper,
            clientAlertDialogsFactory = alertDialogsFactory,
            errorHandling = StubErrorHandlerOnFlow(),
            retryLastAction = retryLastAction
        )
        viewModel.doInit(flowType, "1234")
        whenever(alertDialogsFactory.showCustomAlert(CONFIRM_PIN)).doReturn(template())
    }

    @Test
    fun `initial state`() = runTest {
        val snapshot = viewModel.uiData.toList()
        Assert.assertEquals(3, snapshot.size)
        Assert.assertTrue(snapshot[0] is TopGroupOrgData)
        Assert.assertTrue(snapshot[1] is TextLabelMlcData)
        Assert.assertTrue(snapshot[2] is NumButtonTileOrganismData)
    }

    @Test
    fun `valid pin confirmation`() = runTest {
        whenever(pinHelper.isAlternativeAuthAvailable()).thenReturn(false)
        if (flowType == CreatePinFlowType.AUTHORIZATION || flowType == CreatePinFlowType.GENERATE_SIGNATURE) {
            viewModel.navigation.test {
                viewModel.onUIAction(
                    UIAction(
                        actionKey = UIActionKeysCompose.PIN_CREATED_NUM_BUTTON_ORGANISM,
                        data = "1234"
                    )
                )
                val nav = awaitItem() as ConfirmPinVM.Navigation.PinCreation
                Assert.assertEquals("1234", nav.pin)
            }
        } else {
            viewModel.showTemplateDialog.test {
                viewModel.onUIAction(
                    UIAction(
                        actionKey = UIActionKeysCompose.PIN_CREATED_NUM_BUTTON_ORGANISM,
                        data = "1234"
                    )
                )
                Assert.assertEquals(template(), awaitItem().getContentIfNotHandled())
            }
        }
        val item = viewModel.uiData.firstNotNullOf { it as? NumButtonTileOrganismData }
        Assert.assertFalse(item.clearWithShake)
    }

    @Test
    fun `valid pin confirmation with alt`() = runTest {
        whenever(pinHelper.isAlternativeAuthAvailable()).thenReturn(true)
        when (flowType) {
            CreatePinFlowType.AUTHORIZATION -> viewModel.navigation.test {
                viewModel.onUIAction(
                    UIAction(
                        actionKey = UIActionKeysCompose.PIN_CREATED_NUM_BUTTON_ORGANISM,
                        data = "1234"
                    )
                )
                val nav = awaitItem() as ConfirmPinVM.Navigation.ToAlternativeAuthSetup
                Assert.assertEquals("1234", nav.pin)
            }
            CreatePinFlowType.GENERATE_SIGNATURE -> viewModel.navigation.test {
                viewModel.onUIAction(
                    UIAction(
                        actionKey = UIActionKeysCompose.PIN_CREATED_NUM_BUTTON_ORGANISM,
                        data = "1234"
                    )
                )
                val nav = awaitItem() as ConfirmPinVM.Navigation.PinCreation
                Assert.assertEquals("1234", nav.pin)
            }
            else -> viewModel.showTemplateDialog.test {
                viewModel.onUIAction(
                    UIAction(
                        actionKey = UIActionKeysCompose.PIN_CREATED_NUM_BUTTON_ORGANISM,
                        data = "1234"
                    )
                )
                Assert.assertEquals(template(), awaitItem().getContentIfNotHandled())
            }
        }
        val item = viewModel.uiData.firstNotNullOf { it as? NumButtonTileOrganismData }
        Assert.assertFalse(item.clearWithShake)
    }

    @Test
    fun `invalid pin confirmation`() = runTest {
        viewModel.onUIAction(
            UIAction(
                actionKey = UIActionKeysCompose.PIN_CREATED_NUM_BUTTON_ORGANISM,
                data = "5655"
            )
        )
        val item = viewModel.uiData.firstNotNullOf { it as? NumButtonTileOrganismData }
        Assert.assertTrue(item.clearWithShake)
    }

    @Test
    fun `pin cleared`() = runTest {
        viewModel.onUIAction(UIAction(UIActionKeysCompose.PIN_CLEARED_NUM_BUTTON_ORGANISM))
        val data = viewModel.uiData.firstNotNullOf { it as? NumButtonTileOrganismData }
        Assert.assertFalse(data.clearWithShake)
    }

    companion object {

        @JvmStatic
        @Parameters
        fun parameters() = CreatePinFlowType.values()

        private fun template() =
            TemplateDialogModel(
                key = ActionsConst.FRAGMENT_USER_ACTION_RESULT_KEY,
                type = "",
                isClosable = false,
                data = TemplateDialogData(
                    icon = null,
                    title = "",
                    description = null,
                    mainButton = TemplateDialogButton(
                        name = null,
                        icon = null,
                        action = ""
                    ),
                    alternativeButton = null
                )
            )

    }
}
