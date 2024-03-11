package ua.gov.diia.pin.ui.create.create

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.cash.turbine.test
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import org.junit.runners.Parameterized.Parameters
import org.mockito.MockitoAnnotations
import ua.gov.diia.pin.model.CreatePinFlowType
import ua.gov.diia.pin.rules.MainDispatcherRule
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.event.UIActionKeysCompose
import ua.gov.diia.ui_base.components.molecule.text.TextLabelMlcData
import ua.gov.diia.ui_base.components.organism.header.TopGroupOrgData
import ua.gov.diia.ui_base.components.organism.tile.NumButtonTileOrganismData

@RunWith(Parameterized::class)
class CreatePinVMTest(
    private val flowType: CreatePinFlowType
) {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: CreatePinVM

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        viewModel = CreatePinVM()
        viewModel.doInit(flowType)
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
    fun `create pin`() = runTest {
        viewModel.navigation.test {
            viewModel.onUIAction(
                UIAction(
                    actionKey = UIActionKeysCompose.PIN_CREATED_NUM_BUTTON_ORGANISM,
                    data = "1234"
                )
            )
            Assert.assertEquals(CreatePinVM.Navigation.ToPinConformation("1234"), awaitItem())
        }
    }

    @Test
    fun `create no pin`() = runTest {
        viewModel.navigation.test {
            viewModel.onUIAction(
                UIAction(
                    actionKey = UIActionKeysCompose.PIN_CREATED_NUM_BUTTON_ORGANISM,
                )
            )
            expectNoEvents()
        }
    }

    companion object {

        @JvmStatic
        @Parameters
        fun parameters() = CreatePinFlowType.values()
    }
}