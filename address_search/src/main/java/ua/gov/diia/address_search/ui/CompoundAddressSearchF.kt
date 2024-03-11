package ua.gov.diia.address_search.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import dagger.hilt.android.AndroidEntryPoint
import ua.gov.diia.address_search.databinding.FragmentCompoundAddressSearchBinding
import ua.gov.diia.address_search.models.SearchType
import ua.gov.diia.address_search.models.AddressIdentifier
import ua.gov.diia.address_search.models.AddressItem
import ua.gov.diia.address_search.models.AddressSearchRequest
import ua.gov.diia.search.models.SearchableBullet
import ua.gov.diia.search.models.SearchableItem
import ua.gov.diia.core.ui.dynamicdialog.ActionsConst
import ua.gov.diia.core.util.event.observeUiDataEvent
import ua.gov.diia.core.util.extensions.fragment.*
import ua.gov.diia.ui_base.util.navigation.openTemplateDialog

@AndroidEntryPoint
class CompoundAddressSearchF : Fragment() {

    private val viewModel: CompoundAddressSearchVM by viewModels()
    private val args: CompoundAddressSearchFArgs by navArgs()

    private var binding: FragmentCompoundAddressSearchBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCompoundAddressSearchBinding.inflate(inflater, container, false)
            .apply {
                lifecycleOwner = viewLifecycleOwner
                vm = viewModel

                ivBack.setOnClickListener { findNavController().popBackStack() }
            }
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.setArs(args.initialDataSet, args.featureCode, args.schemaCode)

        viewModel.showTemplateDialog
            .observeUiDataEvent(viewLifecycleOwner, this::openTemplateDialog)

        viewModel.navigateToAddressSelection
            .observeUiDataEvent(viewLifecycleOwner, this::navigateToSearch)

        viewModel.setAddressSelection
            .observeUiDataEvent(viewLifecycleOwner, this::setSelectionResult)

        registerForTemplateDialogNavResult { action ->
            findNavController().popBackStack()
            when (action) {
                ActionsConst.GENERAL_RETRY -> viewModel.launchRetryAction()

                ActionsConst.ERROR_DIALOG_DEAL_WITH_IT,
                ActionsConst.DIALOG_ACTION_CODE_CLOSE -> navigateToCaller()
            }
        }

        registerForNavigationResultOnce(
            CompoundAddressResultKey.RESULT_KEY_COUNTRY,
            viewModel::setSelectedCountry
        )
        registerForNavigationResultOnce(
            CompoundAddressResultKey.RESULT_KEY_REGION,
            viewModel::setSelectedRegion
        )
        registerForNavigationResultOnce(
            CompoundAddressResultKey.RESULT_KEY_DISTRICT,
            viewModel::setSelectedDistrict
        )
        registerForNavigationResultOnce(
            CompoundAddressResultKey.RESULT_KEY_CITY_TYPE,
            viewModel::setSelectedCityType
        )
        registerForNavigationResultOnce(
            CompoundAddressResultKey.RESULT_KEY_CITY,
            viewModel::setSelectedCity
        )
        registerForNavigationResultOnce(
            CompoundAddressResultKey.RESULT_KEY_POST_OFFICE,
            viewModel::setSelectedPostOffice
        )
        registerForNavigationResultOnce(
            CompoundAddressResultKey.RESULT_KEY_STREET_TYPE,
            viewModel::setSelectedStreetType
        )
        registerForNavigationResultOnce(
            CompoundAddressResultKey.RESULT_KEY_STREET,
            viewModel::setSelectedStreet
        )
        registerForNavigationResultOnce(
            CompoundAddressResultKey.RESULT_KEY_HOUSE,
            viewModel::setSelectedHouse
        )
        registerForNavigationResultOnce(
            CompoundAddressResultKey.RESULT_KEY_CORP,
            viewModel::setSelectedCorp
        )
        registerForNavigationResultOnce(
            CompoundAddressResultKey.RESULT_KEY_APARTMENT,
            viewModel::setSelectedApartment
        )
        registerForNavigationResultOnce(
            CompoundAddressResultKey.RESULT_KEY_ZIP,
            viewModel::setSelectedZip
        )
    }

    private fun navigateToSearch(request: AddressSearchRequest) {
        when (request.searchType) {
            SearchType.LIST -> navigateToListSearch(request.items, request.resultCode)
            SearchType.BULLET -> navigateToBulletSearch(request.items, request.resultCode)
        }
    }

    private fun navigateToListSearch(data: Array<AddressItem>, resultKey: String) {
        @Suppress("UNCHECKED_CAST")
        navigate(
            CompoundAddressSearchFDirections.actionDestinationCompoundAddressToDestinationSearchF(
                key = resultKey, searchableList = data as Array<SearchableItem>
            )
        )
    }

    private fun navigateToBulletSearch(data: Array<AddressItem>, resultKey: String) {
        val header = args.initialDataSet.title ?: return
        val title = args.initialDataSet.description ?: return
        @Suppress("UNCHECKED_CAST")
        navigate(
            CompoundAddressSearchFDirections.actionDestinationCompoundAddressToDestinationSearchBulletF(
                screenHeader = header,
                contentTitle = title,
                resultKey = resultKey,
                data = data as Array<SearchableBullet>
            ),
            findNavController()
        )
    }

    private fun setSelectionResult(address: AddressIdentifier) {
        setNavigationResult(result = address, key = args.resultKey)
        findNavController().popBackStack()
    }

    private fun navigateToCaller() {
        //close dialog
        findNavController().popBackStack()
        //pop to the caller
        findNavController().popBackStack()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}