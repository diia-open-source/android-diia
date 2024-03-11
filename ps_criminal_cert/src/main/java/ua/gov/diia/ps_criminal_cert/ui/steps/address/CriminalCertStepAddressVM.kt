package ua.gov.diia.ps_criminal_cert.ui.steps.address

import androidx.lifecycle.MutableLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import ua.gov.diia.address_search.models.AddressFieldRequest
import ua.gov.diia.address_search.models.AddressFieldRequestValue
import ua.gov.diia.address_search.network.ApiAddressSearch
import ua.gov.diia.address_search.ui.AddressParameterMapper
import ua.gov.diia.address_search.ui.AddressSearchVM
import ua.gov.diia.core.di.data_source.http.AuthorizedClient
import ua.gov.diia.core.models.ContextMenuField
import ua.gov.diia.core.models.rating_service.RatingRequest
import ua.gov.diia.core.util.delegation.WithContextMenu
import ua.gov.diia.core.util.delegation.WithErrorHandling
import ua.gov.diia.core.util.delegation.WithRatingDialog
import ua.gov.diia.core.util.delegation.WithRetryLastAction
import ua.gov.diia.core.util.extensions.lifecycle.asLiveData
import ua.gov.diia.core.util.extensions.vm.executeAction
import ua.gov.diia.ps_criminal_cert.ui.CriminalCertConst
import ua.gov.diia.publicservice.helper.PSNavigationHelper
import javax.inject.Inject

@HiltViewModel
class CriminalCertStepAddressVM @Inject constructor(
    @AuthorizedClient private val apiAddressSearch: ApiAddressSearch,
    private val contextMenuDelegate: WithContextMenu<ContextMenuField>,
    errorHandlingDelegate: WithErrorHandling,
    retryActionDelegate: WithRetryLastAction,
    private val withRatingDialog: WithRatingDialog,
    private val navigationHelper: PSNavigationHelper,
    addressParameterMapper: AddressParameterMapper,
) : AddressSearchVM(apiAddressSearch, addressParameterMapper, errorHandlingDelegate, retryActionDelegate),
    WithContextMenu<ContextMenuField> by contextMenuDelegate,
    WithRatingDialog by withRatingDialog,
    PSNavigationHelper by navigationHelper{

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading = _isLoading.asLiveData()

    init {
        loadContent()
    }

    private fun loadContent() {
        executeAction(progressIndicator = _isLoading) {
            // Address search api requires call with empty body first
            apiAddressSearch.getFieldContext(
                featureCode = CriminalCertConst.ADDRESS_FEATURE_CODE,
                addressTemplateCode = CriminalCertConst.ADDRESS_SCHEMA
            )
            val data = apiAddressSearch.getFieldContext(
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
            setAddressSearchArs(data, CriminalCertConst.ADDRESS_FEATURE_CODE, CriminalCertConst.ADDRESS_SCHEMA)
        }
    }
    fun getRatingForm() {
        getRating(
            category = CriminalCertConst.RATING_SERVICE_CATEGORY,
            serviceCode = CriminalCertConst.RATING_SERVICE_CODE
        )
    }

    fun sendRatingRequest(ratingRequest: RatingRequest) {
        sendRating(
            ratingRequest = ratingRequest,
            category = CriminalCertConst.RATING_SERVICE_CATEGORY,
            serviceCode = CriminalCertConst.RATING_SERVICE_CODE
        )
    }

}