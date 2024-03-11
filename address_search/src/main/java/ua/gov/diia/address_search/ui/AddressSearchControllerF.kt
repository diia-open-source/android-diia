package ua.gov.diia.address_search.ui

import android.os.Bundle
import android.view.View
import androidx.annotation.CallSuper
import androidx.fragment.app.Fragment
import ua.gov.diia.address_search.models.SearchType
import ua.gov.diia.address_search.models.AddressSearchRequest
import ua.gov.diia.search.models.SearchableBullet
import ua.gov.diia.search.models.SearchableItem
import ua.gov.diia.core.util.event.observeUiDataEvent
import ua.gov.diia.core.util.extensions.fragment.registerForNavigationResultOnce

abstract class AddressSearchControllerF : Fragment() {

    abstract val viewModel: AddressSearchVM

    @CallSuper
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.navigateToAddressSelection.observeUiDataEvent(viewLifecycleOwner,this::navigateToSearch)

        registerForNavigationResultOnce(CompoundAddressResultKey.RESULT_KEY_REAL_ESTATE, viewModel::setSelectedRealEstate)
        registerForNavigationResultOnce(CompoundAddressResultKey.RESULT_KEY_PRECISION, viewModel::setSelectedPrecision)
        registerForNavigationResultOnce(CompoundAddressResultKey.RESULT_KEY_DESC, viewModel::setSelectedDescription)
        registerForNavigationResultOnce(CompoundAddressResultKey.RESULT_KEY_COUNTRY, viewModel::setSelectedCountry)
        registerForNavigationResultOnce(CompoundAddressResultKey.RESULT_KEY_REGION, viewModel::setSelectedRegion)
        registerForNavigationResultOnce(CompoundAddressResultKey.RESULT_KEY_DISTRICT, viewModel::setSelectedDistrict)
        registerForNavigationResultOnce(CompoundAddressResultKey.RESULT_KEY_CITY_TYPE, viewModel::setSelectedCityType)
        registerForNavigationResultOnce(CompoundAddressResultKey.RESULT_KEY_CITY, viewModel::setSelectedCity)
        registerForNavigationResultOnce(CompoundAddressResultKey.RESULT_KEY_POST_OFFICE, viewModel::setSelectedPostOffice)
        registerForNavigationResultOnce(CompoundAddressResultKey.RESULT_KEY_STREET_TYPE, viewModel::setSelectedStreetType)
        registerForNavigationResultOnce(CompoundAddressResultKey.RESULT_KEY_STREET, viewModel::setSelectedStreet)
        registerForNavigationResultOnce(CompoundAddressResultKey.RESULT_KEY_HOUSE, viewModel::setSelectedHouse)
        registerForNavigationResultOnce(CompoundAddressResultKey.RESULT_KEY_CORP, viewModel::setSelectedCorp)
        registerForNavigationResultOnce(CompoundAddressResultKey.RESULT_KEY_APARTMENT, viewModel::setSelectedApartment)
        registerForNavigationResultOnce(CompoundAddressResultKey.RESULT_KEY_ZIP, viewModel::setSelectedZip)
    }

    private fun navigateToSearch(request: AddressSearchRequest) {
        @Suppress("UNCHECKED_CAST")
        when (request.searchType) {
            SearchType.LIST -> navigateToListSearch(
                data = request.items as Array<SearchableItem>,
                resultKey = request.resultCode
            )
            SearchType.BULLET -> navigateToBulletSearch(
               data = request.items as Array<SearchableBullet>,
               resultKey = request.resultCode
            )
        }
    }

    abstract fun navigateToListSearch(data: Array<SearchableItem>, resultKey: String)

    abstract fun navigateToBulletSearch(data: Array<SearchableBullet>, resultKey: String)
}