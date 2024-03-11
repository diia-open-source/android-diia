package ua.gov.diia.ui_base.fragments.system

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.cash.turbine.test
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import ua.gov.diia.core.models.SystemDialog
import ua.gov.diia.ui_base.components.atom.button.ButtonSystemAtomData
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.event.UIActionKeysCompose
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiText
import ua.gov.diia.ui_base.fragments.dialog.system.DiiaSystemDFVM
import ua.gov.diia.ui_base.getOrAwaitValue
import ua.gov.diia.ui_base.rules.MainDispatcherRule

class DiiaSystemDFVMTest {


    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    lateinit var systemDialogVM: DiiaSystemDFVM

    @Before
    fun before() {
        systemDialogVM = DiiaSystemDFVM()
    }

    @Test
    fun `Do init`() {
        val data = SystemDialog(
            title = "title",
            message = "message",
            positiveButtonTitle = "positiveButtonTitle",
            negativeButtonTitle = "negativeButtonTitle",
            cancelable = false,
        )
        systemDialogVM.doInit(data)
        val uiState = systemDialogVM.uiData.value
        Assert.assertEquals(UiText.DynamicString("message"), uiState.descriptionText)
        Assert.assertEquals(UiText.DynamicString("title"), uiState.titleText)
        Assert.assertEquals(
            ButtonSystemAtomData(
            title = UiText.DynamicString("positiveButtonTitle"),
            actionKey = UIActionKeysCompose.BUTTON_REGULAR
        ), uiState.positiveButton)
        Assert.assertEquals(
            ButtonSystemAtomData(
                title = UiText.DynamicString("negativeButtonTitle"),
                actionKey = UIActionKeysCompose.BUTTON_ALTERNATIVE
            ), uiState.negativeButton)
    }

    @Test
    fun `Do init with empty data`() {
        val data = SystemDialog(
            title = null,
            message = null,
            positiveButtonTitle = null,
            negativeButtonTitle = null,
            cancelable = false,
        )
        systemDialogVM.doInit(data)
        val uiState = systemDialogVM.uiData.value
        Assert.assertNull(uiState.descriptionText)
        Assert.assertNull(uiState.titleText)
        Assert.assertNull(uiState.positiveButton)
        Assert.assertNull(uiState.negativeButton)
    }

    @Test
    fun `positive click`() = runTest {
        systemDialogVM.navigation.test {
            systemDialogVM.onUIAction(UIAction(UIActionKeysCompose.BUTTON_REGULAR))
            Assert.assertEquals(
                systemDialogVM.action.getOrAwaitValue().getContentIfNotHandled(),
                DiiaSystemDFVM.Action.POSITIVE
            )
            Assert.assertEquals(awaitItem(), DiiaSystemDFVM.Navigation.DismissDialog)
        }
    }

    @Test
    fun `negative click`() = runTest {
        systemDialogVM.navigation.test {
            systemDialogVM.onUIAction(UIAction(UIActionKeysCompose.BUTTON_ALTERNATIVE))
            Assert.assertEquals(
                systemDialogVM.action.getOrAwaitValue().getContentIfNotHandled(),
                DiiaSystemDFVM.Action.NEGATIVE
            )
            Assert.assertEquals(awaitItem(), DiiaSystemDFVM.Navigation.DismissDialog)
        }
    }
}