package ua.gov.diia.ui_base.fragments.errordialog

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import org.junit.runners.Parameterized.Parameters
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.event.UIActionKeysCompose.CLOSE_BUTTON
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiText
import ua.gov.diia.ui_base.rules.MainDispatcherRule

@RunWith(Parameterized::class)
class ErrorDVMTest(
    private val error: DialogError,
) {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: ErrorDVM

    @Before
    fun setUp() {
        viewModel = ErrorDVM()
    }

    @Test
    fun `construct dialog`() {
        viewModel.constructDialog(error)
        val uiData = viewModel.uiData.value
        Assert.assertTrue(uiData.icon is UiText.StringResource)
        Assert.assertNotNull(uiData.titleText)
    }

    @Test
    fun `buttons actions`() {
        viewModel.onUIAction(UIAction(CLOSE_BUTTON))
        Assert.assertEquals(ErrorDVM.ErrorAction.FINISH, viewModel.errorAction.value?.getContentIfNotHandled())

        viewModel.onUIAction(UIAction(ErrorDialogConst.ACTION_FINISH))
        Assert.assertEquals(ErrorDVM.ErrorAction.FINISH, viewModel.errorAction.value?.getContentIfNotHandled())

        viewModel.onUIAction(UIAction(ErrorDialogConst.ACTION_RETRY))
        Assert.assertEquals(ErrorDVM.ErrorAction.RETRY, viewModel.errorAction.value?.getContentIfNotHandled())
    }

    companion object {

        @JvmStatic
        @Parameters
        fun parameters() = DialogError.values()
    }
}