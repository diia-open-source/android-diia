package ua.gov.diia.bankid.ui.selection

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.cash.turbine.test
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.eq
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import ua.gov.diia.bankid.BankIdConst
import ua.gov.diia.bankid.model.AuthBank
import ua.gov.diia.bankid.model.AuthBanks
import ua.gov.diia.bankid.model.BankSelectionRequest
import ua.gov.diia.bankid.network.ApiBankId
import ua.gov.diia.bankid.rules.MainDispatcherRule
import ua.gov.diia.ui_base.navigation.BaseNavigation
import ua.gov.diia.core.util.delegation.WithErrorHandlingOnFlow
import ua.gov.diia.core.util.delegation.WithRetryLastAction
import ua.gov.diia.ui_base.components.infrastructure.DataActionWrapper
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.event.UIActionKeysCompose
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiText
import ua.gov.diia.ui_base.components.organism.list.PlainListWithSearchOrganismData
import ua.gov.diia.verification.model.VerificationUrl
import ua.gov.diia.verification.network.ApiVerification
import ua.gov.diia.verification.ui.VerificationSchema

@RunWith(MockitoJUnitRunner::class)
class BankSelectionVMTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    lateinit var apiBankId: ApiBankId

    @Mock
    lateinit var apiVerification: ApiVerification

    @Mock
    lateinit var errorHandling: WithErrorHandlingOnFlow

    @Mock
    lateinit var retryLastAction: WithRetryLastAction

    private lateinit var viewModel: BankSelectionVM

    @Before
    fun setUp() {
        viewModel = BankSelectionVM(
            apiBankId = apiBankId,
            apiVerification = apiVerification,
            errorHandling = errorHandling,
            retryLastAction = retryLastAction
        )
        viewModel.doInit(
            data = BankSelectionRequest(
                schema = VerificationSchema.AUTHORIZATION,
                processId = "12931",
                verificationMethodCode = BankIdConst.METHOD_BANK_ID
            )
        )
    }

    @Test
    fun `banks list`() = runTest {
        val banks = listOf(
            AuthBank(
                id = "12e4",
                logoUrl = null,
                name = "Test bank",
                workable = true
            ), AuthBank(
                id = "11rr4",
                logoUrl = null,
                name = null,
                workable = false,
            )
        )
        val bankList = AuthBanks(value = banks)
        whenever(apiBankId.getBanksList()).thenReturn(bankList)
        viewModel.loadBanks()
        viewModel.contentLoaded.first { it.second }
        val data = viewModel.bodyData.toList()
        val item = data.firstNotNullOf { it as? PlainListWithSearchOrganismData }
        val bank = item.fullList.itemsList.first()
        Assert.assertEquals(banks[0].id, bank.id)
        Assert.assertEquals(banks[0].name, (bank.label as UiText.DynamicString).value)
    }

    @Test
    fun `navigate back`() = runTest {
        viewModel.navigation.test {
            viewModel.onUIAction(UIAction(UIActionKeysCompose.TOOLBAR_NAVIGATION_BACK))
            Assert.assertEquals(BaseNavigation.Back, awaitItem())
        }
        viewModel.navigation.test {
            val action = DataActionWrapper(UIActionKeysCompose.TOOLBAR_NAVIGATION_BACK)
            viewModel.onUIAction(UIAction(UIActionKeysCompose.TITLE_GROUP_MLC, action = action))
            Assert.assertEquals(BaseNavigation.Back, awaitItem())
        }
        viewModel.navigation.test {
            viewModel.onUIAction(UIAction(UIActionKeysCompose.TITLE_GROUP_MLC))
            expectNoEvents()
        }
    }

    @Test
    fun `navigate to bank`() = runTest {
        val sampleBank = AuthBank(
            id = "12e4",
            logoUrl = null,
            name = "Test bank",
            workable = true
        )
        val sampleAuthUrl = "https://test"
        val sampleToken = "1424r"
        val bankList = AuthBanks(value = listOf(sampleBank))
        whenever(apiBankId.getBanksList()).thenReturn(bankList)
        whenever(apiVerification.getAuthUrl(any(), any(), eq(sampleBank.id)))
            .thenReturn(VerificationUrl(sampleAuthUrl, sampleToken, null))
        viewModel.loadBanks()
        viewModel.contentLoaded.first { it.second }
        viewModel.navigation.test {
            viewModel.onUIAction(
                UIAction(
                    UIActionKeysCompose.LIST_ITEM_GROUP_ORG,
                    data = sampleBank.id
                )
            )
            val navRequest = awaitItem() as BankSelectionVM.Navigation.ToBankAuth
            Assert.assertEquals(sampleAuthUrl, navRequest.data.authUrl)
            Assert.assertEquals(sampleBank.id, navRequest.data.bankCode)
        }
    }

    @Test
    fun `bank search`() = runTest {
        val bankList = AuthBanks(
            value = listOf(
                AuthBank(id = "1", logoUrl = null, name = "Test 1", workable = true),
                AuthBank(id = "1", logoUrl = null, name = "Test 2", workable = true),
                AuthBank(id = "1", logoUrl = null, name = "Test 3", workable = true),
                AuthBank(id = "1", logoUrl = null, name = "Test 4", workable = true)
            )
        )
        val searchQuery = "3"
        whenever(apiBankId.getBanksList()).thenReturn(bankList)
        viewModel.loadBanks()
        viewModel.contentLoaded.first { it.second }
        val data = viewModel.bodyData.toList()
        val item = data.firstNotNullOf { it as? PlainListWithSearchOrganismData }
        Assert.assertEquals(4, item.displayedList.itemsList.size)
        viewModel.onUIAction(UIAction(UIActionKeysCompose.SEARCH_INPUT, data = searchQuery))
        val uiData = viewModel.bodyData.firstNotNullOf { it as? PlainListWithSearchOrganismData }
        Assert.assertEquals(1, uiData.displayedList.itemsList.size)
        Assert.assertEquals(
            searchQuery,
            (uiData.searchData.searchFieldValue as UiText.DynamicString).value
        )
    }

    @Test
    fun `empty actions`() = runTest {
        viewModel.onUIAction(UIAction(UIActionKeysCompose.LIST_ITEM_GROUP_ORG))
        Assert.assertFalse(viewModel.progressIndicator.value)

        viewModel.navigation.test {
            viewModel.onUIAction(UIAction(UIActionKeysCompose.TITLE_GROUP_MLC))
            expectNoEvents()
        }
    }
}
