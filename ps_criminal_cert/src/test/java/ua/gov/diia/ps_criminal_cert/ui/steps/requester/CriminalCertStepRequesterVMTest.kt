package ua.gov.diia.ps_criminal_cert.ui.steps.requester

import android.app.Application
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
import ua.gov.diia.ui_base.views.NameModel
import ua.gov.diia.core.models.rating_service.RatingRequest
import ua.gov.diia.ui_base.views.common.card_item.DiiaCardInputField
import ua.gov.diia.core.util.delegation.WithRatingDialog
import ua.gov.diia.core.util.delegation.WithRetryLastAction
import ua.gov.diia.ps_criminal_cert.models.enums.CriminalCertScreen
import ua.gov.diia.ps_criminal_cert.models.response.CriminalCertRequester
import ua.gov.diia.ps_criminal_cert.network.ApiCriminalCert
import ua.gov.diia.ps_criminal_cert.rules.MainDispatcherRule
import ua.gov.diia.ps_criminal_cert.ui.CriminalCertConst
import ua.gov.diia.ps_criminal_cert.util.StubContextMenu
import ua.gov.diia.ps_criminal_cert.util.StubErrorHandler
import ua.gov.diia.ps_criminal_cert.util.awaitEvent
import ua.gov.diia.publicservice.helper.PSNavigationHelper

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class CriminalCertStepRequesterVMTest {

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
    private lateinit var application: Application

    @Mock
    private lateinit var navigationHelper: PSNavigationHelper

    private lateinit var viewModel: CriminalCertStepRequesterVM

    @Before
    fun before() {
        viewModel = CriminalCertStepRequesterVM(
            application = application,
            api = api,
            contextMenuDelegate = StubContextMenu(),
            errorHandlingDelegate = StubErrorHandler(),
            retryActionDelegate = retryLastAction,
            withRatingDialog = withRatingDialog,
            navigationHelper = navigationHelper
        )
        whenever(application.getString(any())) doReturn ""
    }

    @Test
    fun `get data success`() = runTest {
        val state = initRequesterMock()

        viewModel.loadContent()
        Assert.assertEquals(state, viewModel.state.asFlow().first())
    }

    @Test
    fun `add names and refresh data`() = runTest {
        initRequesterMock()
        viewModel.loadContent()
        viewModel.state.asFlow().first()

        viewModel.addFirstName()
        viewModel.addMiddleName()
        viewModel.addLastName()
        viewModel.loadContent()
        viewModel.isLoading.asFlow().first { !it }

        val currentState = viewModel.state.asFlow().first()
        Assert.assertEquals(1, currentState.prevFirstNameData.list.size)
        Assert.assertEquals(1, currentState.prevMiddleNameData.list.size)
        Assert.assertEquals(1, currentState.prevLastNameData.list.size)
    }

    @Test
    fun `names modification`() = runTest {
        initRequesterMock()
        viewModel.addFirstName()
        viewModel.addMiddleName()
        viewModel.addLastName()
        Assert.assertNull(viewModel.state.value)

        viewModel.loadContent()
        viewModel.isLoading.asFlow().first { !it }

        repeat(2) {
            viewModel.addFirstName()
            viewModel.addMiddleName()
            viewModel.addLastName()
        }

        val state = checkNotNull(viewModel.state.value)
        val firstName = state.prevFirstNameData.list.first().copy(name = "firstName_test")
        val middleName = state.prevMiddleNameData.list.first().copy(name = "middleName_test")
        val lastName = state.prevLastNameData.list.first().copy(name = "lastName_test")

        viewModel.updateFirstName(firstName)
        viewModel.updateMiddleName(middleName)
        viewModel.updateLastName(lastName)

        Assert.assertEquals(
            firstName,
            viewModel.state.value?.prevFirstNameData?.list?.firstOrNull()
        )
        Assert.assertEquals(
            middleName,
            viewModel.state.value?.prevMiddleNameData?.list?.firstOrNull()
        )
        Assert.assertEquals(lastName, viewModel.state.value?.prevLastNameData?.list?.firstOrNull())

        viewModel.removeFirstName(firstName)
        viewModel.removeMiddleName(middleName)
        viewModel.removeLastName(lastName)

        val currentState = viewModel.state.asFlow().first()
        Assert.assertEquals(1, currentState.prevFirstNameData.list.size)
        Assert.assertEquals(1, currentState.prevMiddleNameData.list.size)
        Assert.assertEquals(1, currentState.prevLastNameData.list.size)
    }

    @Test
    fun `go next`() = runTest {
        initRequesterMock()
        viewModel.loadContent()
        viewModel.isLoading.asFlow().first { !it }

        repeat(2) {
            viewModel.addFirstName()
            viewModel.addMiddleName()
            viewModel.addLastName()
        }

        val state = viewModel.state.asFlow().first()
        viewModel.onNext()

        val event = viewModel.onNextEvent.awaitEvent()
        Assert.assertEquals(
            state.prevFirstNameData.list.map { it.name },
            event.second.previousFirstNameList
        )
        Assert.assertEquals(
            state.prevMiddleNameData.list.map { it.name },
            event.second.previousMiddleNameList
        )
        Assert.assertEquals(
            state.prevLastNameData.list.map { it.name },
            event.second.previousLastNameList
        )
    }

    @Test
    fun `empty state modification`() = runTest {
        val model = NameModel(
            id = "montes",
            name = "Helene Blanchard",
            title = "ignota",
            hint = "pertinax",
            fieldMode = DiiaCardInputField.FieldMode.BUTTON,
            withRemove = false
        )
        viewModel.removeFirstName(model)
        viewModel.removeMiddleName(model)
        viewModel.removeLastName(model)
        viewModel.updateFirstName(model)
        viewModel.updateMiddleName(model)
        viewModel.updateLastName(model)
        viewModel.onNext()
        Assert.assertNull(viewModel.state.value)
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

    private suspend fun initRequesterMock(): CriminalCertRequester {
        val state = CriminalCertRequester(
            requesterDataScreen = CriminalCertRequester.RequesterDataScreen(
                title = "saepe",
                attentionMessage = null,
                fullName = CriminalCertRequester.Name(
                    label = "fusce",
                    value = "justo"
                ),
                nextScreen = CriminalCertScreen.NATIONALITIES,
            ),
            processCode = null,
            template = null
        )

        whenever(api.getCriminalCertRequester()) doReturn state
        return state
    }
}
