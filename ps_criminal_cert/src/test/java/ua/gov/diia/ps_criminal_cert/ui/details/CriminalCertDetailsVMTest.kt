package ua.gov.diia.ps_criminal_cert.ui.details

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.asFlow
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.doThrow
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
import ua.gov.diia.core.models.common.menu.ContextMenuItem
import ua.gov.diia.core.models.dialogs.TemplateDialogButton
import ua.gov.diia.core.models.dialogs.TemplateDialogData
import ua.gov.diia.core.models.dialogs.TemplateDialogModel
import ua.gov.diia.core.models.rating_service.RatingFormModel
import ua.gov.diia.core.models.rating_service.RatingRequest
import ua.gov.diia.core.ui.dynamicdialog.ActionsConst
import ua.gov.diia.core.util.DateFormats
import ua.gov.diia.core.util.delegation.WithPushNotification
import ua.gov.diia.core.util.delegation.WithRetryLastAction
import ua.gov.diia.core.util.delegation.download_files.base64.DownloadableBase64File
import ua.gov.diia.ps_criminal_cert.models.enums.CriminalCertLoadActionType
import ua.gov.diia.ps_criminal_cert.models.enums.CriminalCertLoadActionType.DOWNLOAD_ARCHIVE
import ua.gov.diia.ps_criminal_cert.models.enums.CriminalCertLoadActionType.VIEW_PDF
import ua.gov.diia.ps_criminal_cert.models.enums.CriminalCertStatus
import ua.gov.diia.ps_criminal_cert.models.response.CriminalCertDetails
import ua.gov.diia.ps_criminal_cert.models.response.CriminalCertDetails.StatusMessage
import ua.gov.diia.ps_criminal_cert.models.response.CriminalCertFileData
import ua.gov.diia.ps_criminal_cert.network.ApiCriminalCert
import ua.gov.diia.ps_criminal_cert.rules.MainDispatcherRule
import ua.gov.diia.ps_criminal_cert.ui.CriminalCertConst
import ua.gov.diia.ps_criminal_cert.util.StubContextMenu
import ua.gov.diia.ps_criminal_cert.util.StubErrorHandler
import ua.gov.diia.ps_criminal_cert.util.StubRatingDialog
import ua.gov.diia.ps_criminal_cert.util.awaitEvent
import ua.gov.diia.publicservice.helper.PSNavigationHelper
import java.util.Date

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class CriminalCertDetailsVMTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var api: ApiCriminalCert

    @Mock
    private lateinit var retryLastAction: WithRetryLastAction

    @Mock
    private lateinit var pushDelegate: WithPushNotification

    @Mock
    private lateinit var navigationHelper: PSNavigationHelper

    private lateinit var withRatingDialog: StubRatingDialog

    private lateinit var errorHandler: StubErrorHandler

    private lateinit var contextMenu: StubContextMenu<ContextMenuField>

    private lateinit var viewModel: CriminalCertDetailsVM

    @Before
    fun before() {
        errorHandler = StubErrorHandler()
        contextMenu = StubContextMenu()
        withRatingDialog = StubRatingDialog()
        viewModel = CriminalCertDetailsVM(
            api = api,
            withRatingDialog = withRatingDialog,
            contextMenuDelegate = contextMenu,
            errorHandlingDelegate = errorHandler,
            retryActionDelegate = retryLastAction,
            pushDelegate = pushDelegate,
            navigationHelper = navigationHelper
        )
    }

    @Test
    fun `get data success`() = runTest {
        val ratingForm = ratingForm()
        val status = data(
            ratingForm = ratingForm,
            contextMenu = listOf(ContextMenuItem("", "", ""))
        )

        whenever(api.getCriminalCertsDetails("id")) doReturn status

        viewModel.load("id")

        Assert.assertEquals(status, viewModel.state.asFlow().first())
        Assert.assertEquals(ratingForm, viewModel.showRatingDialog.awaitEvent())
        Assert.assertEquals(status.contextMenu, contextMenu.getMenu()?.toList())
    }

    @Test
    fun `get data error`() = runTest {
        val error = RuntimeException("error")

        whenever(api.getCriminalCertsDetails("id")) doThrow error

        viewModel.load("id")
        viewModel.isLoading.asFlow().first { !it }
        Assert.assertEquals(error, errorHandler.lastError)
    }

    @Test
    fun `download pdf success`() = runTest {
        val pdfLoadAction = CriminalCertDetails.LoadAction(
            icon = "",
            name = "",
            type = VIEW_PDF
        )
        val doc = pdf()
        val fileData = fileData()
        val data = data()

        whenever(api.getCriminalCertsDetails("id")) doReturn data
        whenever(api.getCriminalCertPdf("id")) doReturn fileData

        viewModel.loadAction(pdfLoadAction, "id")
        Assert.assertTrue(viewModel.state.value?.loadActions.isNullOrEmpty())

        viewModel.load("id")
        viewModel.state.asFlow().first()
        viewModel.loadAction(pdfLoadAction, "id")
        Assert.assertEquals(doc, viewModel.pdfEvent.awaitEvent())
    }

    @Test
    fun `download pdf error`() = runTest {
        val pdfLoadAction = CriminalCertDetails.LoadAction(
            icon = "",
            name = "",
            type = VIEW_PDF
        )
        val doc = pdf()
        val template = template()
        val fileData = fileData(template = template)
        val data = data()

        whenever(api.getCriminalCertsDetails("id")) doReturn data
        whenever(api.getCriminalCertPdf("id")) doReturn fileData

        viewModel.load("id")
        viewModel.state.asFlow().first()
        viewModel.loadAction(pdfLoadAction, "id")
        Assert.assertEquals(template, viewModel.showTemplateDialog.awaitEvent())
    }

    @Test
    fun `download zip success`() = runTest {
        val zipLoadAction = CriminalCertDetails.LoadAction(
            icon = "",
            name = "",
            type = DOWNLOAD_ARCHIVE
        )
        val doc = zip()
        val fileData = fileData()
        val data = data()

        whenever(api.getCriminalCertsDetails("id")) doReturn data
        whenever(api.getCriminalCertZip("id")) doReturn fileData

        viewModel.loadAction(zipLoadAction, "id")
        Assert.assertTrue(viewModel.state.value?.loadActions.isNullOrEmpty())

        viewModel.load("id")
        viewModel.state.asFlow().first()
        viewModel.loadAction(zipLoadAction, "id")

        Assert.assertEquals(doc, viewModel.zipEvent.awaitEvent())
    }

    @Test
    fun `download zip error`() = runTest {
        val pdfLoadAction = CriminalCertDetails.LoadAction(
            icon = "",
            name = "",
            type = DOWNLOAD_ARCHIVE
        )
        val template = template()
        val fileData = fileData(template = template)
        val data = data()

        whenever(api.getCriminalCertsDetails("id")) doReturn data
        whenever(api.getCriminalCertZip("id")) doReturn fileData

        viewModel.load("id")
        viewModel.state.asFlow().first()
        viewModel.loadAction(pdfLoadAction, "id")

        Assert.assertEquals(template, viewModel.showTemplateDialog.awaitEvent())
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
        contextMenu: List<ContextMenuItem>? = null,
        loadingType: CriminalCertLoadActionType? = null,
        ratingForm: RatingFormModel? = null
    ) = CriminalCertDetails(
        contextMenu = contextMenu,
        title = "",
        status = CriminalCertStatus.DONE,
        statusMessage = StatusMessage(
            text = "",
            title = "",
            icon = ""
        ),
        loadActions = listOf(
            CriminalCertDetails.LoadAction(
                icon = "",
                name = "",
                type = DOWNLOAD_ARCHIVE,
                isLoading = loadingType == DOWNLOAD_ARCHIVE,
                isEnabled = loadingType == null
            ),
            CriminalCertDetails.LoadAction(
                icon = "",
                name = "",
                type = VIEW_PDF,
                isLoading = loadingType == VIEW_PDF,
                isEnabled = loadingType == null
            )
        ),
        ratingForm = ratingForm,
        navigationPanel = null
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

    private fun ratingForm() = RatingFormModel(
        key = "",
        comment = null,
        mainButton = "",
        rating = null,
        title = null,
        formCode = "",
        resourceId = null
    )

    private fun pdf() = DownloadableBase64File(
        file = "",
        name = "vytiah_pro_nesudymist_${DateFormats.criminalCertFileFormat.format(Date())}.pdf",
        mimeType = "application/pdf"
    )

    private fun zip() = DownloadableBase64File(
        file = "",
        name = "vytiah_pro_nesudymist_${DateFormats.criminalCertFileFormat.format(Date())}.zip",
        mimeType = "application/zip"
    )

    private fun fileData(
        template: TemplateDialogModel? = null
    ) = CriminalCertFileData(
        file = "",
        template = template
    )
}
