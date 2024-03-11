package ua.gov.diia.bankid.ui.auth

import app.cash.turbine.test
import kotlinx.coroutines.test.runTest
import okhttp3.HttpUrl
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import ua.gov.diia.ui_base.navigation.BaseNavigation
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.event.UIActionKeysCompose
import ua.gov.diia.verification.model.VerificationFlowResult

@RunWith(MockitoJUnitRunner::class)
class BankAuthVMTest {

    private lateinit var viewModel: BankAuthVM

    @Before
    fun setUp() {
        viewModel = BankAuthVM()
        viewModel.doInit(
            bankCode = "test"
        )
    }

    @Test
    fun `parse auth code`() = runTest {
        val url = HttpUrl.Builder()
            .scheme("https")
            .host("test")
            .addQueryParameter(BankAuthConst.CODE, "1234")
            .build()
            .toString()
        viewModel.navigation.test {
            viewModel.parseAuthCode(url)
            val navRequest = awaitItem() as BankAuthVM.Navigation.CompleteAuth
            val data = (navRequest.data as VerificationFlowResult.CompleteVerificationStep)
            Assert.assertEquals("test", data.bankCode)
            Assert.assertEquals("1234", data.requestId)
        }
    }

    @Test
    fun `navigate back`() = runTest {
        viewModel.navigation.test {
            viewModel.onUIAction(UIAction(UIActionKeysCompose.TOOLBAR_NAVIGATION_BACK))
            Assert.assertEquals(BaseNavigation.Back, awaitItem())
        }
    }

    @Test
    fun `loading state`() = runTest {
        Assert.assertTrue(viewModel.uiData.value.progressLoadState)
        viewModel.onUIAction(UIAction(BankAuthConst.PROGRESS_INACTIVE))
        Assert.assertFalse(viewModel.uiData.value.progressLoadState)
        viewModel.onUIAction(UIAction(BankAuthConst.PROGRESS_ACTIVE))
        Assert.assertTrue(viewModel.uiData.value.progressLoadState)
    }
}