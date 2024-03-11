package ua.gov.diia.ps_criminal_cert.ui.steps.nationality

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
import ua.gov.diia.ui_base.views.NameModel
import ua.gov.diia.core.models.dialogs.TemplateDialogButton
import ua.gov.diia.core.models.dialogs.TemplateDialogData
import ua.gov.diia.core.models.dialogs.TemplateDialogModel
import ua.gov.diia.core.models.rating_service.RatingRequest
import ua.gov.diia.core.ui.dynamicdialog.ActionsConst
import ua.gov.diia.ui_base.views.common.card_item.DiiaCardInputField
import ua.gov.diia.core.util.delegation.WithRatingDialog
import ua.gov.diia.core.util.delegation.WithRetryLastAction
import ua.gov.diia.ps_criminal_cert.models.enums.CriminalCertScreen
import ua.gov.diia.ps_criminal_cert.models.response.CriminalCertNationalities
import ua.gov.diia.ps_criminal_cert.network.ApiCriminalCert
import ua.gov.diia.ps_criminal_cert.rules.MainDispatcherRule
import ua.gov.diia.ps_criminal_cert.ui.CriminalCertConst
import ua.gov.diia.ps_criminal_cert.util.StubContextMenu
import ua.gov.diia.ps_criminal_cert.util.StubErrorHandler
import ua.gov.diia.ps_criminal_cert.util.awaitEvent
import ua.gov.diia.publicservice.helper.PSNavigationHelper

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class CriminalCertStepNationalityVMTest {

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

    private lateinit var viewModel: CriminalCertStepNationalityVM

    @Before
    fun before() {
        viewModel = CriminalCertStepNationalityVM(
            api = api,
            apiAddressSearch = apiAddressSearch,
            contextMenuDelegate = StubContextMenu(),
            errorHandlingDelegate = StubErrorHandler(),
            retryActionDelegate = retryLastAction,
            withRatingDialog = withRatingDialog,
            navigationHelper = navigationHelper
        )
    }

    @Test
    fun `get data success`() = runTest {
        val data = data()
        val addressNationality = addressNationality()

        whenever(apiAddressSearch.getNationalities()) doReturn addressNationality
        whenever(api.getCriminalCertNationalities()) doReturn data

        viewModel.loadContent()
        viewModel.isLoading.asFlow().first { !it }

        Assert.assertEquals(addressNationality, viewModel.nationalities.value)

        val listModel = NameModel(
            id = "0",
            name = "",
            title = "",
            hint = "",
            fieldMode = DiiaCardInputField.FieldMode.BUTTON,
            withRemove = false
        )
        Assert.assertEquals(data.copy(countryList = listOf(listModel)), viewModel.state.value)
    }

    @Test
    fun `prevent add if prev country is empty`() = runTest {
        val state = CriminalCertNationalities(
            data = CriminalCertNationalities.NationalitiesScreen(
                title = null,
                attentionMessage = null,
                country = null,
                maxNationalitiesCount = 10,
                nextScreen = null
            )
        )
        val addressNationality = addressNationality()

        whenever(apiAddressSearch.getNationalities()) doReturn addressNationality
        whenever(api.getCriminalCertNationalities()) doReturn state

        viewModel.addCountry()
        Assert.assertNull(viewModel.state.value?.countryList)

        viewModel.loadContent()
        viewModel.state.asFlow().first()
        viewModel.addCountry()
        Assert.assertEquals(
            state.countryList.size + 1,
            viewModel.state.asFlow().first().countryList.size
        )
    }

    @Test
    fun `prevent add if we have Ukraine`() = runTest {
        val state = CriminalCertNationalities(
            data = CriminalCertNationalities.NationalitiesScreen(
                title = null,
                attentionMessage = null,
                country = null,
                maxNationalitiesCount = 10,
                nextScreen = null
            )
        )

        whenever(apiAddressSearch.getNationalities()) doReturn addressNationality()
        whenever(api.getCriminalCertNationalities()) doReturn state

        viewModel.loadContent()
        viewModel.state.asFlow().first()
        viewModel.selectCountry(NameModel("0", "", "", ""))
        viewModel.setCountry(NationalityItem(code = "0", name = "Україна"))

        val countriesSize = viewModel.state.asFlow().first().countryList.size
        viewModel.addCountry()

        Assert.assertEquals(countriesSize, viewModel.state.asFlow().first().countryList.size)
    }

    @Test
    fun `prevent add more than allowed`() = runTest {
        val maxCount = 2

        val state = CriminalCertNationalities(
            data = CriminalCertNationalities.NationalitiesScreen(
                title = null,
                attentionMessage = null,
                country = null,
                maxNationalitiesCount = maxCount,
                nextScreen = null
            )
        )

        whenever(apiAddressSearch.getNationalities()) doReturn addressNationality()
        whenever(api.getCriminalCertNationalities()) doReturn state

        viewModel.loadContent()
        viewModel.state.asFlow().first()
        viewModel.selectCountry(NameModel("0", "", "", ""))
        viewModel.setCountry(NationalityItem(code = "0", name = "name"))

        viewModel.addCountry()
        viewModel.selectCountry(NameModel("1", "", "", ""))
        viewModel.setCountry(NationalityItem(code = "1", name = "name"))

        viewModel.addCountry()

        Assert.assertEquals(maxCount, viewModel.state.asFlow().first().countryList.size)
    }

    @Test
    fun `select country with filtered country list`() = runTest {
        val state = CriminalCertNationalities(
            data = CriminalCertNationalities.NationalitiesScreen(
                title = null,
                attentionMessage = null,
                country = null,
                maxNationalitiesCount = 10,
                nextScreen = null
            )
        )
        val addressNationality = AddressNationality(
            nationalities = listOf(
                NationalityItem(code = "0", name = "name 0"),
                NationalityItem(code = "1", name = "name 1"),
                NationalityItem(code = "2", name = "name 2"),
                NationalityItem(code = "3", name = "Україна")
            )
        )

        whenever(apiAddressSearch.getNationalities()) doReturn addressNationality
        whenever(api.getCriminalCertNationalities()) doReturn state

        viewModel.loadContent()
        viewModel.state.asFlow().first()
        viewModel.selectCountry(NameModel("0", "name 0", "", ""))
        viewModel.setCountry(NationalityItem(code = "0", name = "name 0"))
        viewModel.addCountry()
        viewModel.selectCountry(NameModel("1", "name 1", "", ""))
        viewModel.setCountry(NationalityItem(code = "1", name = "name 1"))

        viewModel.selectCountry(
            NameModel("2", name = "name 2", "", "")
        )

        Assert.assertEquals(
            listOf(NationalityItem(code = "2", name = "name 2")),
            viewModel.navigateToCountrySelection.awaitEvent()
        )
    }

    @Test
    fun `remove country`() = runTest {
        val state = CriminalCertNationalities(
            data = CriminalCertNationalities.NationalitiesScreen(
                title = null,
                attentionMessage = null,
                country = null,
                maxNationalitiesCount = 10,
                nextScreen = null
            )
        )
        whenever(apiAddressSearch.getNationalities()) doReturn addressNationality()
        whenever(api.getCriminalCertNationalities()) doReturn state

        viewModel.loadContent()
        viewModel.isLoading.asFlow().first { !it }

        viewModel.addCountry()
        val country = viewModel.state.asFlow().first().countryList.single()
        viewModel.removeCountry(country)
        Assert.assertEquals(0, viewModel.state.value?.countryList?.size)
    }

    @Test
    fun `get data error`() = runTest {
        val template = template()
        val data = data(template = template)
        val addressNationality = addressNationality()

        whenever(apiAddressSearch.getNationalities()) doReturn addressNationality
        whenever(api.getCriminalCertNationalities()) doReturn data

        viewModel.loadContent()

        Assert.assertEquals(template, viewModel.showTemplateDialog.awaitEvent())
    }

    @Test
    fun `go next`() = runTest {
        whenever(apiAddressSearch.getNationalities()) doReturn addressNationality()
        whenever(api.getCriminalCertNationalities()) doReturn data()

        viewModel.onNext()
        Assert.assertNull(viewModel.onNextEvent.value?.getContentIfNotHandled())

        viewModel.loadContent()
        viewModel.isLoading.asFlow().first { !it }
        viewModel.addCountry()
        viewModel.onNext()
        val event = viewModel.onNextEvent.awaitEvent()
        Assert.assertEquals(listOf(""), event.second)
        Assert.assertEquals(data().data?.nextScreen, event.first)
    }

    @Test
    fun `no next screen`() = runTest {
        whenever(apiAddressSearch.getNationalities()) doReturn addressNationality()
        whenever(api.getCriminalCertNationalities()) doReturn data(nextScreen = null)

        viewModel.onNext()
        Assert.assertNull(viewModel.onNextEvent.value?.getContentIfNotHandled())

        viewModel.loadContent()
        viewModel.isLoading.asFlow().first { !it }
        viewModel.addCountry()
        viewModel.onNext()
        Assert.assertNull(viewModel.onNextEvent.value?.getContentIfNotHandled())
    }

    @Test
    fun `modification before loading`() = runTest {
        viewModel.removeCountry(
            NameModel(
                id = "liber",
                name = "Florine Morales",
                title = "id",
                hint = "numquam",
                fieldMode = DiiaCardInputField.FieldMode.BUTTON,
                withRemove = false
            )
        )
        viewModel.setCountry(NationalityItem(code = "evertitur", name = "Ofelia Baxter"))
        viewModel.selectCountry(
            NameModel(
                id = "sed",
                name = "Eugenia McKinney",
                title = "eum",
                hint = "causae",
                fieldMode = DiiaCardInputField.FieldMode.EDITABLE,
                withRemove = false
            )
        )
        Assert.assertNull(viewModel.state.value)
        Assert.assertNull(viewModel.nationalities.value)
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
        template: TemplateDialogModel? = null,
        nextScreen: CriminalCertScreen? = CriminalCertScreen.REGISTRATION_PLACE,
    ) = CriminalCertNationalities(
        data = CriminalCertNationalities.NationalitiesScreen(
            title = "Title",
            attentionMessage = null,
            country = null,
            maxNationalitiesCount = null,
            nextScreen = nextScreen,
        ),
        template = template
    )

    private fun addressNationality() = AddressNationality(
        nationalities = listOf(
            NationalityItem(
                code = "0",
                name = "name 0"
            ),
            NationalityItem(
                code = "1",
                name = "name 1"
            ),
            NationalityItem(
                code = "2",
                name = "name 2"
            )
        )
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
