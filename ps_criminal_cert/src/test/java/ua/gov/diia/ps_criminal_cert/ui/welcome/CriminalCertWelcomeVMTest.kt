package ua.gov.diia.ps_criminal_cert.ui.welcome

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.asFlow
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.doReturn
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
import ua.gov.diia.core.models.dialogs.TemplateDialogButton
import ua.gov.diia.core.models.dialogs.TemplateDialogData
import ua.gov.diia.core.models.dialogs.TemplateDialogModel
import ua.gov.diia.core.models.rating_service.RatingRequest
import ua.gov.diia.core.ui.dynamicdialog.ActionsConst
import ua.gov.diia.core.util.delegation.WithRatingDialog
import ua.gov.diia.core.util.delegation.WithRetryLastAction
import ua.gov.diia.ps_criminal_cert.helper.PSCriminalCertHelper
import ua.gov.diia.ps_criminal_cert.models.enums.CriminalCertApplicationInfoNextStep
import ua.gov.diia.ps_criminal_cert.models.response.CriminalCertInfo
import ua.gov.diia.ps_criminal_cert.network.ApiCriminalCert
import ua.gov.diia.ps_criminal_cert.rules.MainDispatcherRule
import ua.gov.diia.ps_criminal_cert.ui.CriminalCertConst
import ua.gov.diia.ps_criminal_cert.util.StubContextMenu
import ua.gov.diia.ps_criminal_cert.util.StubErrorHandler
import ua.gov.diia.ps_criminal_cert.util.awaitEvent
import ua.gov.diia.publicservice.helper.PSNavigationHelper

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class CriminalCertWelcomeVMTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var api: ApiCriminalCert

    @Mock
    private lateinit var retryLastAction: WithRetryLastAction

    @Mock
    private lateinit var navigationHelper: PSNavigationHelper

    @Mock
    private lateinit var criminalCertHelper: PSCriminalCertHelper

    @Mock
    private lateinit var withRatingDialog: WithRatingDialog

    private lateinit var errorHandler: StubErrorHandler

    private lateinit var contextMenu: StubContextMenu<ContextMenuField>

    private lateinit var viewModel: CriminalCertWelcomeVM

    @Before
    fun before() {
        errorHandler = StubErrorHandler()
        contextMenu = StubContextMenu()
        viewModel = CriminalCertWelcomeVM(
            api = api,
            contextMenuDelegate = contextMenu,
            errorHandlingDelegate = errorHandler,
            retryActionDelegate = retryLastAction,
            withRatingDialog = withRatingDialog,
            navigationHelper = navigationHelper,
            criminalCertHelper = criminalCertHelper
        )
    }

    @Test
    fun `get data success`() = runTest {
        val state = data(showContextMenu = true)

        whenever(api.getCriminalCertInfo(any())) doReturn state

        viewModel.loadContent("test", true, "123")
        viewModel.isLoading.asFlow().first { !it }

        Assert.assertEquals(state, viewModel.state.asFlow().first())
        Assert.assertTrue(viewModel.directionFlag.value == true)
        Assert.assertEquals("123", viewModel.resId.value)
    }

    @Test
    fun `get data success no context menu`() = runTest {
        val state = data(showContextMenu = false)

        whenever(api.getCriminalCertInfo(any())) doReturn state

        viewModel.loadContent("test", true, "123")
        viewModel.isLoading.asFlow().first { !it }

        Assert.assertEquals(state, viewModel.state.asFlow().first())
        Assert.assertNull(contextMenu.getMenu())
    }

    @Test
    fun `get data error`() = runTest {
        val template = template()
        val state = data(showContextMenu = true, template = template)

        whenever(api.getCriminalCertInfo(any())) doReturn state

        viewModel.loadContent("test", true, "123")
        viewModel.isLoading.asFlow().first { !it }

        Assert.assertEquals(template, viewModel.showTemplateDialog.awaitEvent())
    }

    @Test
    fun `handle notification`() = runTest {
        viewModel.handleNotification(null)
        Assert.assertNull(viewModel.navigateToDetails.value)

        viewModel.handleNotification("cert_id")
        Assert.assertEquals("cert_id", viewModel.navigateToDetails.value?.getContentIfNotHandled())

        viewModel.handleNotification("cert_id")
        Assert.assertNull(viewModel.navigateToDetails.value?.getContentIfNotHandled())
    }

    @Test
    fun `go to next screen requester`() = runTest {
        val state = data(nextScreen = CriminalCertApplicationInfoNextStep.requester.name)
        whenever(api.getCriminalCertInfo(any())) doReturn state

        viewModel.loadContent("test", true, "123")
        viewModel.isLoading.asFlow().first { !it }

        viewModel.onNext()
        Assert.assertTrue(viewModel.navigateToRequester.asFlow().first().notHandedYet)
    }

    @Test
    fun `go to next screen reasons`() = runTest {
        val state = data(nextScreen = CriminalCertApplicationInfoNextStep.reasons.name)
        whenever(api.getCriminalCertInfo(any())) doReturn state

        viewModel.loadContent("test", true, "123")
        viewModel.isLoading.asFlow().first { !it }

        viewModel.onNext()
        Assert.assertTrue(viewModel.navigateToReasons.asFlow().first().notHandedYet)
    }

    @Test
    fun `go to next screen none`() = runTest {
        val state = data(nextScreen = null)
        whenever(api.getCriminalCertInfo(any())) doReturn state

        viewModel.loadContent("test", true, "123")
        viewModel.isLoading.asFlow().first { !it }

        viewModel.onNext()
        Assert.assertNull(viewModel.navigateToRequester.value)
        Assert.assertNull(viewModel.navigateToReasons.value)
    }

    @Test
    fun rating() = runTest {
        viewModel.getRatingForm()
        verify(withRatingDialog) {
            with(viewModel) {
                getRating(
                    CriminalCertConst.RATING_SERVICE_CATEGORY,
                    CriminalCertConst.RATING_SERVICE_CODE
                )
            }
        }
        val request = mock<RatingRequest>()
        viewModel.sendRatingRequest(request)
        verify(withRatingDialog) {
            with(viewModel) {
                sendRating(
                    request,
                    CriminalCertConst.RATING_SERVICE_CATEGORY,
                    CriminalCertConst.RATING_SERVICE_CODE
                )
            }
        }
    }

    private fun data(
        showContextMenu: Boolean = false,
        template: TemplateDialogModel? = null,
        nextScreen: String? = null
    ) = CriminalCertInfo(
        showContextMenu = showContextMenu,
        title = "",
        text = "",
        attentionMessage = null,
        template = template,
        nextScreen = nextScreen
    )

    private fun template() = TemplateDialogModel(
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
