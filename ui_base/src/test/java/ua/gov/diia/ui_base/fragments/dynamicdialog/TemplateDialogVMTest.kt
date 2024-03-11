package ua.gov.diia.ui_base.fragments.dynamicdialog

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asFlow
import app.cash.turbine.test
import io.mockk.clearAllMocks
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import org.junit.runners.Parameterized.Parameters
import ua.gov.diia.core.models.dialogs.TemplateDialogButton
import ua.gov.diia.core.models.dialogs.TemplateDialogData
import ua.gov.diia.core.models.dialogs.TemplateDialogModel
import ua.gov.diia.core.ui.dynamicdialog.ActionsConst
import ua.gov.diia.core.util.event.UiEvent
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.event.UIActionKeysCompose
import ua.gov.diia.ui_base.getOrAwaitValue
import ua.gov.diia.ui_base.rules.MainDispatcherRule
import java.util.concurrent.TimeoutException

@RunWith(Parameterized::class)
class TemplateDialogVMTest(
    private val dialogType: String,
) {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    lateinit var actionLogout: MutableLiveData<UiEvent>
    lateinit var templateDialogVM: TemplateDialogVM

    @Before
    fun before() {
        actionLogout = MutableLiveData()
        templateDialogVM = TemplateDialogVM(actionLogout)
        val model = TemplateDialogModel(
            key = null,
            type = dialogType,
            isClosable = dialogType == TemplateDialogConst.ALERT_TYPE_ALERT_REVIEW,
            data = TemplateDialogData(
                icon = null,
                title = "tale",
                description = null,
                mainButton = TemplateDialogButton(
                    name = "main",
                    icon = null,
                    action = ActionsConst.GENERAL_RETRY,
                    link = null
                ),
                alternativeButton = if (dialogType == TemplateDialogConst.ALERT_TYPE_HORIZONTAL_BUTTONS) {
                    TemplateDialogButton(
                        name = "alt",
                        icon = null,
                        action = ActionsConst.DIALOG_ACTION_CANCEL,
                        link = null
                    )
                } else {
                    null
                }
            )
        )
        templateDialogVM.doInit(model)
    }

    @Test
    fun `perform logout`() = runTest {
        val action = ActionsConst.DIALOG_ACTION_CODE_LOGOUT
        Assert.assertThrows(Throwable::class.java) {
            runBlocking {
                templateDialogVM.navigation.test {
                    templateDialogVM.onUIAction(
                        UIAction(
                            UIActionKeysCompose.BUTTON_REGULAR,
                            action
                        )
                    )
                    awaitItem()
                }
            }
        }
        Assert.assertTrue(actionLogout.asFlow().first().notHandedYet)
    }

    @Test
    fun `perform any other action`() = runTest {
        val action = "any action"
        templateDialogVM.navigation.test {
            templateDialogVM.onUIAction(UIAction(UIActionKeysCompose.BTN_PLAIN_ATM, action))
            Assert.assertEquals(
                TemplateDialogVM.Navigation.OnDialogAction(action),
                awaitItem()
            )
        }
        Assert.assertThrows(TimeoutException::class.java) {
            actionLogout.getOrAwaitValue()
        }
    }

    @Test
    fun `close dialog`() = runTest {
        templateDialogVM.navigation.test {
            templateDialogVM.onUIAction(UIAction(UIActionKeysCompose.CLOSE_BUTTON))
            Assert.assertEquals(
                TemplateDialogVM.Navigation.DismissDialog,
                awaitItem()
            )
        }
    }

    @After
    fun after() {
        clearAllMocks()
    }

    companion object {

        @JvmStatic
        @Parameters
        fun parameters() = listOf(
            TemplateDialogConst.ALERT_TYPE_LARGE,
            TemplateDialogConst.ALERT_TYPE_LARGE_MIDDLE,
            TemplateDialogConst.ALERT_TYPE_SMALL,
            TemplateDialogConst.ALERT_TYPE_LEFT_MIDDLE,
            TemplateDialogConst.ALERT_TYPE_ALERT_REVIEW,
            TemplateDialogConst.ALERT_TYPE_HORIZONTAL_BUTTONS,
            TemplateDialogConst.ALERT_TYPE_HORIZONTAL_BUTTON,
        )
    }
}
