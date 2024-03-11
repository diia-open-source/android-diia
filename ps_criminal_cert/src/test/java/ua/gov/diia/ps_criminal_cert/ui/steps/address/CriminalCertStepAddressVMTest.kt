package ua.gov.diia.ps_criminal_cert.ui.steps.address

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.asFlow
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner
import ua.gov.diia.address_search.models.AddressFieldRequest
import ua.gov.diia.address_search.models.AddressFieldRequestValue
import ua.gov.diia.address_search.network.ApiAddressSearch
import ua.gov.diia.address_search.ui.AddressParameterMapper
import ua.gov.diia.core.models.rating_service.RatingRequest
import ua.gov.diia.core.util.delegation.WithErrorHandling
import ua.gov.diia.core.util.delegation.WithRatingDialog
import ua.gov.diia.core.util.delegation.WithRetryLastAction
import ua.gov.diia.ps_criminal_cert.rules.MainDispatcherRule
import ua.gov.diia.ps_criminal_cert.ui.CriminalCertConst
import ua.gov.diia.ps_criminal_cert.util.StubContextMenu
import ua.gov.diia.ps_criminal_cert.util.StubErrorHandler
import ua.gov.diia.publicservice.helper.PSNavigationHelper

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class CriminalCertStepAddressVMTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var apiAddressSearch: ApiAddressSearch

    @Mock
    private lateinit var retryLastAction: WithRetryLastAction

    @Mock
    private lateinit var withRatingDialog: WithRatingDialog

    @Mock
    private lateinit var addressParameterMapper: AddressParameterMapper

    @Mock
    private lateinit var navigationHelper: PSNavigationHelper

    private lateinit var errorHandlingDelegate: WithErrorHandling
    private lateinit var viewModel: CriminalCertStepAddressVM

    @Before
    fun before() {
        errorHandlingDelegate = StubErrorHandler()
        viewModel = CriminalCertStepAddressVM(
            apiAddressSearch = apiAddressSearch,
            contextMenuDelegate = StubContextMenu(),
            errorHandlingDelegate = errorHandlingDelegate,
            retryActionDelegate = retryLastAction,
            withRatingDialog = withRatingDialog,
            navigationHelper = navigationHelper,
            addressParameterMapper = addressParameterMapper
        )
    }

    @Test
    fun `get data success`() = runTest {
        viewModel.isLoading.asFlow().first { !it }
        Mockito.verify(apiAddressSearch).getFieldContext(
            featureCode = CriminalCertConst.ADDRESS_FEATURE_CODE,
            addressTemplateCode = CriminalCertConst.ADDRESS_SCHEMA
        )

        Mockito.verify(apiAddressSearch).getFieldContext(
            featureCode = CriminalCertConst.ADDRESS_FEATURE_CODE,
            addressTemplateCode = CriminalCertConst.ADDRESS_SCHEMA,
            request = AddressFieldRequest(
                values = listOf(
                    AddressFieldRequestValue(
                        id = "804",
                        type = "country",
                        value = "УКРАЇНА"
                    )
                )
            )
        )
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
}