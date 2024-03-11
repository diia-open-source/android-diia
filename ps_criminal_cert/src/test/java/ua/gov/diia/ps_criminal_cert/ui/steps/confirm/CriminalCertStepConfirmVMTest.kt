package ua.gov.diia.ps_criminal_cert.ui.steps.confirm

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.asFlow
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.argThat
import com.nhaarman.mockitokotlin2.clearInvocations
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
import ua.gov.diia.ps_criminal_cert.helper.PSCriminalCertHelper
import ua.gov.diia.ps_criminal_cert.models.Birth
import ua.gov.diia.ps_criminal_cert.models.CriminalCertUserData
import ua.gov.diia.ps_criminal_cert.models.PreviousNames
import ua.gov.diia.ps_criminal_cert.models.request.CriminalCertConfirmationRequest
import ua.gov.diia.ps_criminal_cert.models.response.CriminalCertConfirmation
import ua.gov.diia.ps_criminal_cert.models.response.CriminalCertConfirmed
import ua.gov.diia.ps_criminal_cert.network.ApiCriminalCert
import ua.gov.diia.ps_criminal_cert.rules.MainDispatcherRule
import ua.gov.diia.ps_criminal_cert.ui.CriminalCertConst
import ua.gov.diia.ps_criminal_cert.util.StubContextMenu
import ua.gov.diia.ps_criminal_cert.util.StubErrorHandler
import ua.gov.diia.ps_criminal_cert.util.awaitEvent
import ua.gov.diia.publicservice.helper.PSNavigationHelper

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class CriminalCertStepConfirmVMTest {

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

    @Mock
    private lateinit var criminalCertHelper: PSCriminalCertHelper

    private lateinit var errorHandler: StubErrorHandler

    private lateinit var viewModel: CriminalCertStepConfirmVM

    @Before
    fun before() {
        errorHandler = StubErrorHandler()
        viewModel = CriminalCertStepConfirmVM(
            api = api,
            contextMenuDelegate = StubContextMenu(),
            errorHandlingDelegate = errorHandler,
            retryActionDelegate = retryLastAction,
            withRatingDialog = withRatingDialog,
            navigationHelper = navigationHelper,
            criminalCertHelper = criminalCertHelper
        )
    }

    @Test
    fun `get data success`() = runTest {
        val request = request()
        val data = data()
        val userData = userData()

        whenever(api.getCriminalCertConfirmationData(request)) doReturn data

        viewModel.load(userData)
        viewModel.isLoading.asFlow().first { !it }

        Assert.assertEquals(data, viewModel.state.value)
    }

    @Test
    fun `get data error`() = runTest {
        val request = request()
        val template = template()
        val data = data(template = template)
        val userData = userData()

        whenever(api.getCriminalCertConfirmationData(request)) doReturn data

        viewModel.load(userData)

        Assert.assertEquals(template, viewModel.showTemplateDialog.awaitEvent())
    }

    @Test
    fun `update requester data`() = runTest {
        whenever(api.orderCriminalCert(any())) doReturn confirmedData()

        viewModel.confirm(userData())
        viewModel.isOrdering.asFlow().first { !it }

        clearInvocations(api)
        viewModel.confirm(userDataFilled())
        viewModel.isOrdering.asFlow().first { !it }

        verify(api).orderCriminalCert(
            argThat { x ->
                val data = userDataFilled()
                Assert.assertEquals(data.birth?.country, x.birthPlace?.country)
                Assert.assertEquals(data.birth?.city, x.birthPlace?.city)
                Assert.assertEquals(data.prevNames?.previousFirstNameList?.single(), x.previousFirstName)
                Assert.assertEquals(data.prevNames?.previousMiddleNameList?.single(), x.previousMiddleName)
                Assert.assertEquals(data.prevNames?.previousLastNameList?.single(), x.previousLastName)
                true
            }
        )
    }

    @Test
    fun `confirm success`() = runTest {
        val request = request()
        val confirmedData = confirmedData()
        val userData = userData()

        whenever(api.orderCriminalCert(request)) doReturn confirmedData

        viewModel.confirm(userData)

        Assert.assertEquals(confirmedData.template, viewModel.showTemplateDialog.awaitEvent())

        viewModel.navigateToDetails()
        Assert.assertEquals(confirmedData.applicationId, viewModel.navigateToDetails.awaitEvent())
    }

    @Test
    fun `open link`() = runTest {
        val link = "http://test.com/"
        viewModel.openLinkListenerAM(link)
        Assert.assertEquals(link, viewModel.openLinkAM.awaitEvent())
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
    ) = CriminalCertConfirmation(
        application = null,
        processCode = null,
        template = template
    )

    private fun userData() = CriminalCertUserData(
        reasonId = null,
        certificateType = null,
        prevNames = null,
        birth = null,
        nationalities = null,
        registrationAddressId = null,
        phoneNumber = null
    )

    private fun userDataFilled() = CriminalCertUserData(
        reasonId = null,
        certificateType = null,
        prevNames = PreviousNames(
            previousFirstNameList = listOf("FirstName"),
            previousMiddleNameList = listOf("MiddleName"),
            previousLastNameList = listOf("LastName")
        ),
        birth = Birth(
            country = "Ukraine",
            city = ""
        ),
        nationalities = null,
        registrationAddressId = null,
        phoneNumber = null
    )

    private fun request() = CriminalCertConfirmationRequest(
        reasonId = null,
        certificateType = null,
        previousFirstName = null,
        previousMiddleName = null,
        previousLastName = null,
        birthPlace = null,
        nationalities = null,
        registrationAddressId = null,
        phoneNumber = null,
        email = null,
        publicService = null
    )

    private fun confirmedData() = CriminalCertConfirmed(
        applicationId = "applicationId",
        processCode = null,
        template = template(),
        navigationPanel = null
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
