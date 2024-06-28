package ua.gov.diia.ps_criminal_cert.ui.steps.reason

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.asFlow
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.filterNotNull
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
import ua.gov.diia.ps_criminal_cert.models.response.CriminalCertReasons
import ua.gov.diia.ps_criminal_cert.network.ApiCriminalCert
import ua.gov.diia.ps_criminal_cert.rules.MainDispatcherRule
import ua.gov.diia.ps_criminal_cert.ui.CriminalCertConst
import ua.gov.diia.ps_criminal_cert.util.StubContextMenu
import ua.gov.diia.ps_criminal_cert.util.StubErrorHandler
import ua.gov.diia.ps_criminal_cert.util.awaitEvent
import ua.gov.diia.publicservice.helper.PSNavigationHelper

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class CriminalCertStepReasonsVMTest {

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

    @Mock
    private lateinit var navigationHelper: PSNavigationHelper

    private lateinit var viewModel: CriminalCertStepReasonsVM

    @Before
    fun before() {
        viewModel = CriminalCertStepReasonsVM(
            api = api,
            contextMenuDelegate = StubContextMenu(),
            errorHandlingDelegate = StubErrorHandler(),
            retryActionDelegate = retryLastAction,
            withRatingDialog = withRatingDialog,
            navigationHelper = navigationHelper
        )
    }

    @Test
    fun `get data success`() = runTest {
        val reasons = CriminalCertReasons(
            title = "title",
            subtitle = "subtitle",
            reasons = listOf(
                CriminalCertReasons.Reason(
                    code = "0",
                    name = "name 0",
                    isSelected = false
                ),
                CriminalCertReasons.Reason(
                    code = "1",
                    name = "name 1",
                    isSelected = false
                )
            )
        )

        whenever(api.getCriminalCertReasons()) doReturn reasons

        viewModel.loadContent()
        Assert.assertEquals(reasons, viewModel.state.asFlow().first())
    }

    @Test
    fun `refresh data success`() = runTest {
        val reasons = CriminalCertReasons(
            title = "title",
            subtitle = "subtitle",
            reasons = listOf(
                CriminalCertReasons.Reason(
                    code = "0",
                    name = "name 0",
                    isSelected = false
                ),
                CriminalCertReasons.Reason(
                    code = "1",
                    name = "name 1",
                    isSelected = false
                )
            )
        )

        whenever(api.getCriminalCertReasons()) doReturn reasons

        viewModel.loadContent()
        viewModel.isLoading.asFlow().first { !it }
        viewModel.selectReason(
            CriminalCertReasons.Reason(
                code = "1",
                name = "name 1",
                isSelected = false
            )
        )
        viewModel.loadContent()
        viewModel.isLoading.asFlow().first { !it }
        Assert.assertEquals(
            CriminalCertReasons(
                title = "title",
                subtitle = "subtitle",
                reasons = listOf(
                    CriminalCertReasons.Reason(
                        code = "0",
                        name = "name 0",
                        isSelected = false
                    ),
                    CriminalCertReasons.Reason(
                        code = "1",
                        name = "name 1",
                        isSelected = true
                    )
                )
            ),
            viewModel.state.value
        )
    }

    @Test
    fun `get data error`() = runTest {
        val template = template()
        val reasons = CriminalCertReasons(
            title = "title",
            subtitle = "subtitle",
            reasons = listOf(
                CriminalCertReasons.Reason(
                    code = "0",
                    name = "name 0",
                    isSelected = false
                ),
                CriminalCertReasons.Reason(
                    code = "1",
                    name = "name 1",
                    isSelected = false
                )
            ),
            template = template
        )

        whenever(api.getCriminalCertReasons()) doReturn reasons

        viewModel.loadContent()

        Assert.assertEquals(template, viewModel.showTemplateDialog.awaitEvent())
    }

    @Test
    fun `select item and go next`() = runTest {
        val reasons = CriminalCertReasons(
            title = "title",
            subtitle = "subtitle",
            reasons = listOf(
                CriminalCertReasons.Reason(
                    code = "0",
                    name = "name 0",
                    isSelected = false
                ),
                CriminalCertReasons.Reason(
                    code = "1",
                    name = "name 1",
                    isSelected = false
                )
            )
        )

        whenever(api.getCriminalCertReasons()) doReturn reasons

        viewModel.onNext()
        Assert.assertNull(viewModel.onNextEvent.value?.getContentIfNotHandled())

        viewModel.loadContent()
        viewModel.isLoading.asFlow().first { !it }
        viewModel.selectReason(
            CriminalCertReasons.Reason(
                code = "1",
                name = "name 1",
                isSelected = false
            )
        )
        Assert.assertTrue(viewModel.isNextAvailable.value == true)
        viewModel.onNext()

        Assert.assertEquals("1", viewModel.onNextEvent.value?.getContentIfNotHandled())
    }

    @Test
    fun `reason selection`() = runTest {
        val reasons = CriminalCertReasons(
            title = "title",
            subtitle = "subtitle",
            reasons = listOf(
                CriminalCertReasons.Reason(
                    code = "0",
                    name = "name 0",
                    isSelected = false
                ),
                CriminalCertReasons.Reason(
                    code = "1",
                    name = "name 1",
                    isSelected = false
                ),
                CriminalCertReasons.Reason(
                    code = "2",
                    name = "name 2",
                    isSelected = false
                )
            )
        )

        whenever(api.getCriminalCertReasons()) doReturn reasons

        viewModel.selectReason(checkNotNull(reasons.reasons)[0])
        Assert.assertNull(viewModel.state.value)
        Assert.assertFalse(viewModel.isNextAvailable.value == true)
        viewModel.loadContent()
        viewModel.isLoading.asFlow().first { !it }
        val state = viewModel.state.asFlow().filterNotNull().first()

        viewModel.selectReason(checkNotNull(state.reasons)[0])
        viewModel.selectReason(checkNotNull(state.reasons)[0])
        viewModel.selectReason(checkNotNull(state.reasons)[1])
        Assert.assertTrue(viewModel.isNextAvailable.value == true)
    }

    @Test
    fun `no reason`() = runTest {
        val reasons = CriminalCertReasons(
            title = "title",
            subtitle = "subtitle",
            reasons = null,
        )

        whenever(api.getCriminalCertReasons()) doReturn reasons
        viewModel.loadContent()
        viewModel.state.asFlow().filterNotNull().first()
        Assert.assertFalse(viewModel.isNextAvailable.value == true)

        viewModel.selectReason(
            CriminalCertReasons.Reason(
                code = "2",
                name = "name 2",
                isSelected = false
            )
        )
        Assert.assertFalse(viewModel.isNextAvailable.value == true)
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
