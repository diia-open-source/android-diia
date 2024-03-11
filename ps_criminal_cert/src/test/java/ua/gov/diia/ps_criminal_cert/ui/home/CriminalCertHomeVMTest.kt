package ua.gov.diia.ps_criminal_cert.ui.home

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.asFlow
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.eq
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import ua.gov.diia.core.models.ContextMenuField
import ua.gov.diia.core.models.common.NavigationPanel
import ua.gov.diia.core.models.common.menu.ContextMenuItem
import ua.gov.diia.core.models.common.message.StubMessage
import ua.gov.diia.core.models.rating_service.RatingRequest
import ua.gov.diia.core.util.delegation.WithRatingDialog
import ua.gov.diia.core.util.delegation.WithRetryLastAction
import ua.gov.diia.ps_criminal_cert.helper.PSCriminalCertHelper
import ua.gov.diia.ps_criminal_cert.models.enums.CriminalCertStatus
import ua.gov.diia.ps_criminal_cert.models.response.CriminalCertListData
import ua.gov.diia.ps_criminal_cert.network.ApiCriminalCert
import ua.gov.diia.ps_criminal_cert.rules.MainDispatcherRule
import ua.gov.diia.ps_criminal_cert.ui.CriminalCertConst
import ua.gov.diia.ps_criminal_cert.util.StubContextMenu
import ua.gov.diia.ps_criminal_cert.util.StubErrorHandler
import ua.gov.diia.ps_criminal_cert.util.awaitEvent
import ua.gov.diia.publicservice.helper.PSNavigationHelper

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class CriminalCertHomeVMTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var api: ApiCriminalCert

    @Mock
    private lateinit var retryLastAction: WithRetryLastAction

    @Mock
    private lateinit var ratingDialog: WithRatingDialog

    @Mock
    private lateinit var navigationHelper: PSNavigationHelper

    @Mock
    private lateinit var criminalCertHelper: PSCriminalCertHelper

    private lateinit var errorHandler: StubErrorHandler

    private lateinit var contextMenu: StubContextMenu<ContextMenuField>

    private lateinit var viewModel: CriminalCertHomeVM

    @Before
    fun before() {
        errorHandler = StubErrorHandler()
        contextMenu = StubContextMenu()
        viewModel = CriminalCertHomeVM(
            api = api,
            errorHandlingDelegate = errorHandler,
            contextMenuDelegate = contextMenu,
            retryActionDelegate = retryLastAction,
            withRatingDialog = ratingDialog,
            navigationHelper = navigationHelper,
            criminalCertHelper = criminalCertHelper
        )
    }

    @Test
    fun `done list load`() = runTest {
        whenever(api.getCriminalCertList(eq(CriminalCertStatus.DONE.str), any(), any()))
            .thenReturn(certList())

        val list = viewModel.fetchDoneList(0, 10)
        Assert.assertEquals(certList().certificates, list)
        val state = viewModel.state.asFlow().first()
        Assert.assertTrue(state.hasDoneList == true)
        Assert.assertFalse(state.hasProcessingList == true)
        Assert.assertTrue(state.hasContent)
    }

    @Test
    fun `processing list load`() = runTest {
        whenever(api.getCriminalCertList(eq(CriminalCertStatus.PROCESSING.str), any(), any()))
            .thenReturn(certList())

        val list = viewModel.fetchProcessingList(0, 10)
        Assert.assertEquals(certList().certificates, list)
        val state = viewModel.state.asFlow().first()
        Assert.assertTrue(state.hasProcessingList == true)
        Assert.assertFalse(state.hasDoneList == true)
        Assert.assertTrue(state.hasContent)
    }

    @Test
    fun `empty list load`() = runTest {
        whenever(api.getCriminalCertList(any(), any(), any())).thenReturn(
            CriminalCertListData(
                stubMessage = null,
                total = 0,
                certsStatus = null,
                certificates = listOf(),
                navigationPanel = null
            )
        )

        viewModel.fetchDoneList(0, 10)
        viewModel.fetchProcessingList(0, 10)

        val state = viewModel.state.asFlow().first()
        Assert.assertTrue(state.hasDoneList == false)
        Assert.assertTrue(state.hasProcessingList == false)

        Assert.assertTrue(viewModel.navigateToWelcome.awaitEvent())
    }

    @Test
    fun `handle notification`() = runTest {
        val certId = "1234"
        whenever(api.getCriminalCertList(any(), any(), any())).thenReturn(certList())

        viewModel.setCertId(certId, true, null)
        viewModel.fetchDoneList(0, 10)
        Assert.assertEquals(certId, viewModel.navigateToDetails.awaitEvent())
    }

    @Test
    fun `navigate next`() = runTest {
        viewModel.onNext()
        Assert.assertFalse(viewModel.navigateToWelcome.awaitEvent())
    }

    @Test
    fun `screen name`() = runTest {
        val name = "Test name"
        viewModel.setScreenName(name)
        Assert.assertEquals(name, viewModel.screenHeader.asFlow().first())
    }

    @Test
    fun `stub message`() = runTest {
        val certs = certList()
        whenever(api.getCriminalCertList(any(), any(), any())).thenReturn(certs)

        viewModel.fetchDoneList(0, 10)
        viewModel.fetchProcessingList(0, 10)

        Assert.assertEquals(
            certs.stubMessage,
            viewModel.stubMessage(CriminalCertStatus.PROCESSING).asFlow().first()
        )
        Assert.assertEquals(
            certs.stubMessage,
            viewModel.stubMessage(CriminalCertStatus.DONE).asFlow().first()
        )
    }

    @Test
    fun delimiters() = runTest {
        whenever(api.getCriminalCertList(any(), any(), any())).thenReturn(certList())

        Assert.assertFalse(
            viewModel.delimiterState(CriminalCertStatus.PROCESSING).asFlow().first() == true
        )
        Assert.assertFalse(
            viewModel.delimiterState(CriminalCertStatus.DONE).asFlow().first() == true
        )

        viewModel.fetchDoneList(0, 10)
        viewModel.fetchProcessingList(0, 10)

        Assert.assertTrue(
            viewModel.delimiterState(CriminalCertStatus.PROCESSING).asFlow().first() == true
        )
        Assert.assertTrue(
            viewModel.delimiterState(CriminalCertStatus.DONE).asFlow().first() == true
        )
    }

    @Test
    fun `loading state`() = runTest {
        whenever(api.getCriminalCertList(any(), any(), any())).thenReturn(certList())

        Assert.assertTrue(viewModel.isLoading.asFlow().first())
        viewModel.setDoneListLoading(false)
        viewModel.setProcessingListLoading(false)

        viewModel.setDoneListLoading(true)
        Assert.assertTrue(viewModel.isLoading.asFlow().first())

        viewModel.setProcessingListLoading(true)
        Assert.assertTrue(viewModel.isLoading.asFlow().first())

        viewModel.setDoneListLoading(false)
        Assert.assertTrue(viewModel.isLoading.asFlow().first())

        viewModel.setProcessingListLoading(false)
        Assert.assertFalse(viewModel.isLoading.asFlow().first())

        viewModel.setProcessingListLoading(true)
        viewModel.fetchDoneList(0, 10)

        Assert.assertFalse(viewModel.isLoading.asFlow().first())
    }

    @Test
    fun `screen content`() = runTest {
        val panel = NavigationPanel(
            header = "Header",
            contextMenu = listOf(
                ContextMenuItem(
                    type = "dictas",
                    name = "Carissa Moody",
                    code = null
                )
            )
        )
        whenever(api.getCriminalCertList(any(), any(), any())).thenReturn(certList(panel))

        viewModel.fetchProcessingList(0, 10)
        Assert.assertEquals(panel.header, viewModel.screenHeader.value)
        Assert.assertArrayEquals(panel.menu, viewModel.getMenu())

        viewModel.setListRefreshing()

        viewModel.fetchDoneList(0, 10)
        Assert.assertEquals(panel.header, viewModel.screenHeader.value)
        Assert.assertArrayEquals(panel.menu, viewModel.getMenu())
    }

    @Test
    fun rating() = runTest {
        viewModel.getRatingForm()
        verify(ratingDialog) {
            with(viewModel) {
                getRating(
                    CriminalCertConst.RATING_SERVICE_CATEGORY,
                    CriminalCertConst.RATING_SERVICE_CODE
                )
            }
        }
        val request = mock<RatingRequest>()
        viewModel.sendRatingRequest(request)
        verify(ratingDialog) {
            with(viewModel) {
                sendRating(
                    request,
                    CriminalCertConst.RATING_SERVICE_CATEGORY,
                    CriminalCertConst.RATING_SERVICE_CODE
                )
            }
        }
    }

    private fun certList(panel: NavigationPanel? = null) = CriminalCertListData(
        stubMessage = StubMessage(
            icon = null,
            text = "Stub message",
            canRepeat = false,
            title = "Title",
            description = null
        ),
        total = 2,
        certsStatus = null,
        certificates = listOf(
            CriminalCertListData.CertItem(
                id = "1234",
                status = CriminalCertStatus.DONE,
                reason = "Test",
                creationDate = "",
                type = "kdoe"
            ),
            CriminalCertListData.CertItem(
                id = "5949",
                status = CriminalCertStatus.DONE,
                reason = "Test2",
                creationDate = "",
                type = "fiew"
            )
        ),
        navigationPanel = panel
    )
}
