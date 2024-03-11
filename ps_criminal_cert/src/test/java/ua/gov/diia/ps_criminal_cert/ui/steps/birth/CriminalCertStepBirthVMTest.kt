package ua.gov.diia.ps_criminal_cert.ui.steps.birth

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
import ua.gov.diia.address_search.models.AddressNationality
import ua.gov.diia.address_search.models.NationalityItem
import ua.gov.diia.address_search.network.ApiAddressSearch
import ua.gov.diia.core.models.dialogs.TemplateDialogButton
import ua.gov.diia.core.models.dialogs.TemplateDialogData
import ua.gov.diia.core.models.dialogs.TemplateDialogModel
import ua.gov.diia.core.models.rating_service.RatingRequest
import ua.gov.diia.core.ui.dynamicdialog.ActionsConst
import ua.gov.diia.core.util.delegation.WithRatingDialog
import ua.gov.diia.core.util.delegation.WithRetryLastAction
import ua.gov.diia.ps_criminal_cert.models.Birth
import ua.gov.diia.ps_criminal_cert.models.enums.CriminalCertScreen
import ua.gov.diia.ps_criminal_cert.models.response.CriminalCertBirthPlace
import ua.gov.diia.ps_criminal_cert.network.ApiCriminalCert
import ua.gov.diia.ps_criminal_cert.rules.MainDispatcherRule
import ua.gov.diia.ps_criminal_cert.ui.CriminalCertConst
import ua.gov.diia.ps_criminal_cert.util.StubContextMenu
import ua.gov.diia.ps_criminal_cert.util.StubErrorHandler
import ua.gov.diia.ps_criminal_cert.util.awaitEvent
import ua.gov.diia.publicservice.helper.PSNavigationHelper

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class CriminalCertStepBirthVMTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var api: ApiCriminalCert

    @Mock
    private lateinit var apiAddressSearch: ApiAddressSearch

    @Mock
    private lateinit var retryLastAction: WithRetryLastAction

    @Mock
    private lateinit var withRatingDialog: WithRatingDialog

    @Mock
    private lateinit var navigationHelper: PSNavigationHelper

    private lateinit var errorHandler: StubErrorHandler

    private lateinit var viewModel: CriminalCertStepBirthVM

    @Before
    fun before() {
        errorHandler = StubErrorHandler()
        viewModel = CriminalCertStepBirthVM(
            api = api,
            apiAddressSearch = apiAddressSearch,
            contextMenuDelegate = StubContextMenu(),
            errorHandlingDelegate = errorHandler,
            retryActionDelegate = retryLastAction,
            withRatingDialog = withRatingDialog,
            navigationHelper = navigationHelper
        )
    }

    @Test
    fun `get data success`() = runTest {
        val nationalities = addressNationality()
        val birthPlace = birthPlace()

        whenever(apiAddressSearch.getNationalities()) doReturn nationalities
        whenever(api.getCriminalCertBirthPlace()) doReturn birthPlace

        viewModel.loadContent()

        Assert.assertEquals(nationalities, viewModel.nationalities.asFlow().first())
        Assert.assertEquals(birthPlace, viewModel.birthPlace.asFlow().first())
    }

    @Test
    fun `get data success with country defined`() = runTest {
        val nationalities = addressNationality()
        val birthPlace = CriminalCertBirthPlace(
            data = CriminalCertBirthPlace.BirthPlaceDataScreen(
                title = "",
                country = CriminalCertBirthPlace.Country(
                    label = "",
                    value = "value",
                    hint = "",
                    checkbox = "",
                    otherCountry = null
                ),
                city = null,
                nextScreen = null
            )
        )

        whenever(apiAddressSearch.getNationalities()) doReturn nationalities
        whenever(api.getCriminalCertBirthPlace()) doReturn birthPlace

        viewModel.loadContent()
        viewModel.isLoading.asFlow().first { !it }

        Assert.assertFalse(viewModel.isOtherCountryChecked.asFlow().first())
    }

    @Test
    fun `get data error`() = runTest {
        val nationalities = addressNationality()
        val template = template()
        val birthPlace = birthPlace(template = template)

        whenever(apiAddressSearch.getNationalities()) doReturn nationalities
        whenever(api.getCriminalCertBirthPlace()) doReturn birthPlace

        viewModel.loadContent()

        Assert.assertEquals(template, viewModel.showTemplateDialog.awaitEvent())
    }

    @Test
    fun `check other country`() = runTest {
        viewModel.checkOtherCountry()

        Assert.assertTrue(viewModel.isOtherCountryChecked.asFlow().first())
        Assert.assertNull(viewModel.countryInput.asFlow().first())

        viewModel.checkOtherCountry()

        Assert.assertFalse(viewModel.isOtherCountryChecked.asFlow().first())
        Assert.assertNull(viewModel.countryInput.asFlow().first())
    }

    @Test
    fun `go next with country`() = runTest {
        val nationalities = addressNationality()
        val birthPlace = CriminalCertBirthPlace(
            data = CriminalCertBirthPlace.BirthPlaceDataScreen(
                title = "",
                country = null,
                city = null,
                nextScreen = CriminalCertScreen.BIRTH_PLACE
            )
        )

        whenever(apiAddressSearch.getNationalities()) doReturn nationalities
        whenever(api.getCriminalCertBirthPlace()) doReturn birthPlace

        viewModel.loadContent()
        viewModel.birthPlace.asFlow().first()

        val country = NationalityItem(code = "", name = "country")
        viewModel.setCountry(country)
        viewModel.otherCountryInput.value = "otherCountry"

        viewModel.onNext()

        Assert.assertEquals(
            CriminalCertScreen.BIRTH_PLACE to Birth(
                country = "country",
                city = ""
            ),
            viewModel.onNextEvent.awaitEvent()
        )
    }

    @Test
    fun `go next with other country`() = runTest {
        val nationalities = addressNationality()
        val birthPlace = CriminalCertBirthPlace(
            data = CriminalCertBirthPlace.BirthPlaceDataScreen(
                title = "",
                country = null,
                city = null,
                nextScreen = CriminalCertScreen.BIRTH_PLACE
            )
        )

        whenever(apiAddressSearch.getNationalities()) doReturn nationalities
        whenever(api.getCriminalCertBirthPlace()) doReturn birthPlace

        viewModel.loadContent()
        viewModel.birthPlace.asFlow().first()

        val country = NationalityItem(code = "", name = "country")
        viewModel.setCountry(country)

        viewModel.checkOtherCountry()
        viewModel.onNext()

        Assert.assertEquals(
            CriminalCertScreen.BIRTH_PLACE to Birth(
                country = "",
                city = ""
            ),
            viewModel.onNextEvent.awaitEvent()
        )

        viewModel.otherCountryInput.value = "otherCountry"
        viewModel.cityInput.value = "somecity"
        viewModel.onNext()

        Assert.assertEquals(
            CriminalCertScreen.BIRTH_PLACE to Birth(
                country = "otherCountry",
                city = "somecity"
            ),
            viewModel.onNextEvent.awaitEvent()
        )
    }

    @Test
    fun `select country`() = runTest {
        viewModel.selectCountry()
        Assert.assertNull(viewModel.navigateToCountrySelection.value?.getContentIfNotHandled())

        whenever(apiAddressSearch.getNationalities()) doReturn addressNationality()
        whenever(api.getCriminalCertBirthPlace()) doReturn birthPlace()

        viewModel.loadContent()
        viewModel.isLoading.asFlow().first { !it }

        viewModel.selectCountry()
        Assert.assertEquals(
            addressNationality().nationalities,
            viewModel.navigateToCountrySelection.value?.getContentIfNotHandled()
        )
    }

    @Test
    fun `not go next`() = runTest {
        Assert.assertFalse(viewModel.isNextAvailable.value == true)
        viewModel.onNext()
        Assert.assertNull(viewModel.onNextEvent.value?.getContentIfNotHandled())

        whenever(api.getCriminalCertBirthPlace()) doReturn birthPlace()
        viewModel.loadContent()
        viewModel.birthPlace.asFlow().first()

        viewModel.onNext()
        Assert.assertNull(viewModel.onNextEvent.value?.getContentIfNotHandled())
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

    private fun addressNationality() =
        AddressNationality(
            nationalities = emptyList()
        )

    private fun birthPlace(
        template: TemplateDialogModel? = null
    ) = CriminalCertBirthPlace(
        data = template?.let {
            CriminalCertBirthPlace.BirthPlaceDataScreen(
                title = "",
                country = null,
                city = null,
                nextScreen = null
            )
        },
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
