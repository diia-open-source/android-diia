package ua.gov.diia.ps_criminal_cert.ui.steps.contacts

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.asFlow
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
import ua.gov.diia.core.models.dialogs.TemplateDialogButton
import ua.gov.diia.core.models.dialogs.TemplateDialogData
import ua.gov.diia.core.models.dialogs.TemplateDialogModel
import ua.gov.diia.core.models.rating_service.RatingRequest
import ua.gov.diia.core.ui.dynamicdialog.ActionsConst
import ua.gov.diia.core.util.delegation.WithRatingDialog
import ua.gov.diia.core.util.delegation.WithRetryLastAction
import ua.gov.diia.ps_criminal_cert.models.response.CriminalCertContacts
import ua.gov.diia.ps_criminal_cert.network.ApiCriminalCert
import ua.gov.diia.ps_criminal_cert.rules.MainDispatcherRule
import ua.gov.diia.ps_criminal_cert.ui.CriminalCertConst
import ua.gov.diia.ps_criminal_cert.util.StubContextMenu
import ua.gov.diia.ps_criminal_cert.util.StubErrorHandler
import ua.gov.diia.ps_criminal_cert.util.awaitEvent

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class CriminalCertStepContactsVMTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var api: ApiCriminalCert

    @Mock
    private lateinit var retryLastAction: WithRetryLastAction

    @Mock
    private lateinit var withRatingDialog: WithRatingDialog

    private lateinit var errorHandler: StubErrorHandler

    private lateinit var viewModel: CriminalCertStepContactsVM

    @Before
    fun before() {
        errorHandler = StubErrorHandler()
        viewModel = CriminalCertStepContactsVM(
            api = api,
            contextMenuDelegate = StubContextMenu(),
            errorHandlingDelegate = errorHandler,
            retryActionDelegate = retryLastAction,
            withRatingDialog = withRatingDialog,
            navigationHelper = mock()
        )
    }

    @Test
    fun `get data success`() = runTest {
        val data = data()

        whenever(api.getCriminalCertContacts()) doReturn data

        viewModel.loadContent()
        viewModel.isLoading.asFlow().first { !it }

        Assert.assertEquals(data, viewModel.state.asFlow().first())
        Assert.assertEquals("999999999", viewModel.phoneInput.asFlow().first())
    }

    @Test
    fun `get data error`() = runTest {
        val template = template()
        val data = data(template = template)

        whenever(api.getCriminalCertContacts()) doReturn data

        viewModel.loadContent()
        viewModel.isLoading.asFlow().first { !it }

        Assert.assertEquals(template, viewModel.showTemplateDialog.awaitEvent())
    }

    @Test
    fun `go next`() = runTest {
        val data = data()

        whenever(api.getCriminalCertContacts()) doReturn data

        viewModel.loadContent()
        viewModel.state.asFlow().first()
        viewModel.phoneInput.value = "991111111"

        Assert.assertTrue(viewModel.isNextAvailable.asFlow().first())
        viewModel.onNext()

        Assert.assertEquals("+380991111111", viewModel.onNextEvent.awaitEvent())
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
        template: TemplateDialogModel? = null
    ) = CriminalCertContacts(
        title = null,
        text = null,
        phoneNumber = "+380999999999",
        email = null,
        template = template
    )

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
