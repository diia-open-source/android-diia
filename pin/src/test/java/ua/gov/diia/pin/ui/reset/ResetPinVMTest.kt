package ua.gov.diia.pin.ui.reset

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import app.cash.turbine.test
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import ua.gov.diia.core.ui.dynamicdialog.ActionsConst
import ua.gov.diia.ui_base.navigation.BaseNavigation
import ua.gov.diia.core.util.alert.ClientAlertDialogsFactory
import ua.gov.diia.core.util.delegation.WithRetryLastAction
import ua.gov.diia.core.util.event.UiEvent
import ua.gov.diia.pin.repository.LoginPinRepository
import ua.gov.diia.pin.rules.MainDispatcherRule
import ua.gov.diia.pin.utils.StubErrorHandlerOnFlow
import ua.gov.diia.ui_base.components.infrastructure.DataActionWrapper
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.event.UIActionKeysCompose
import ua.gov.diia.ui_base.components.molecule.text.TextLabelMlcData
import ua.gov.diia.ui_base.components.organism.header.TopGroupOrgData
import ua.gov.diia.ui_base.components.organism.tile.NumButtonTileOrganismData

@RunWith(MockitoJUnitRunner::class)
class ResetPinVMTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    lateinit var loginPinRepository: LoginPinRepository

    @Mock
    lateinit var clientAlertDialogsFactory: ClientAlertDialogsFactory

    @Mock
    lateinit var retryLastAction: WithRetryLastAction

    private val actionLogout = MutableLiveData<UiEvent>()

    private lateinit var viewModel: ResetPinVM

    @Before
    fun setUp() {
        viewModel = ResetPinVM(
            actionLogout = actionLogout,
            loginPinRepository = loginPinRepository,
            clientAlertDialogsFactory = clientAlertDialogsFactory,
            errorHandling = StubErrorHandlerOnFlow(),
            retryLastAction = retryLastAction,
        )
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
    fun `navigate back`() = runTest {
        viewModel.navigation.test {
            viewModel.onUIAction(UIAction(UIActionKeysCompose.TOOLBAR_NAVIGATION_BACK))
            Assert.assertEquals(BaseNavigation.Back, awaitItem())
        }
        viewModel.navigation.test {
            viewModel.onUIAction(UIAction(
                actionKey = UIActionKeysCompose.TITLE_GROUP_MLC,
                action = DataActionWrapper(type = ActionsConst.ACTION_NAVIGATE_BACK)
            ))
            Assert.assertEquals(BaseNavigation.Back, awaitItem())
        }
    }

    @Test
    fun `reset valid pin`() = runTest {
        whenever(loginPinRepository.isPinValid(any())).thenReturn(true)
        viewModel.navigation.test {
            viewModel.onUIAction(
                UIAction(
                    UIActionKeysCompose.PIN_CREATED_NUM_BUTTON_ORGANISM,
                    data = "1234"
                )
            )
            Assert.assertEquals(ResetPinVM.Navigation.CreateNewPin, awaitItem())
        }
    }

    @Test
    fun logout() = runTest {
        viewModel.resetPin()
        Assert.assertTrue(actionLogout.value?.notHandedYet == true)
    }

    @Test
    fun `reset invalid pin`() = runTest {
        whenever(loginPinRepository.isPinValid(any())).thenReturn(false)
        whenever(loginPinRepository.getPinTryCount()).thenReturn(3)
        whenever(clientAlertDialogsFactory.showAlertAfterInvalidPin()).thenReturn(mock())
        viewModel.showTemplateDialog.test {
            viewModel.onUIAction(UIAction(UIActionKeysCompose.PIN_CREATED_NUM_BUTTON_ORGANISM, "1234"))
            awaitItem()
            verify(clientAlertDialogsFactory).showAlertAfterInvalidPin()
        }
    }

    @Test
    fun `pin cleared`() = runTest {
        viewModel.onUIAction(UIAction(UIActionKeysCompose.PIN_CLEARED_NUM_BUTTON_ORGANISM))
        val data = viewModel.uiData.firstNotNullOf { it as? NumButtonTileOrganismData }
        Assert.assertFalse(data.clearPin)
        Assert.assertFalse(data.clearWithShake)
    }
}