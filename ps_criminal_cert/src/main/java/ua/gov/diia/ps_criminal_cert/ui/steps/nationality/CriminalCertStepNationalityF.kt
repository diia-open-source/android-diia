package ua.gov.diia.ps_criminal_cert.ui.steps.nationality

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import dagger.hilt.android.AndroidEntryPoint
import ua.gov.diia.ps_criminal_cert.R
import ua.gov.diia.ps_criminal_cert.databinding.FragmentCriminalCertStepNationalityBinding
import ua.gov.diia.core.models.ConsumableItem
import ua.gov.diia.address_search.models.NationalityItem
import ua.gov.diia.ps_criminal_cert.models.enums.CriminalCertScreen
import ua.gov.diia.core.models.rating_service.RatingRequest
import ua.gov.diia.core.ui.dynamicdialog.ActionsConst
import ua.gov.diia.ps_criminal_cert.ui.CriminalCertConst
import ua.gov.diia.core.util.event.observeUiDataEvent
import ua.gov.diia.ui_base.util.navigation.openTemplateDialog
import ua.gov.diia.core.util.extensions.fragment.*
import ua.gov.diia.ps_criminal_cert.ui.CriminalCertRatingScreenCodes

@AndroidEntryPoint
class CriminalCertStepNationalityF : Fragment() {

    private val viewModel: CriminalCertStepNationalityVM by viewModels()
    private val args: CriminalCertStepNationalityFArgs by navArgs()
    private var binding: FragmentCriminalCertStepNationalityBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        with(viewModel) {
            setContextMenu(args.contextMenu)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCriminalCertStepNationalityBinding.inflate(
            inflater,
            container,
            false
        )
            .apply {
                lifecycleOwner = viewLifecycleOwner
                vm = viewModel
                viewModel.loadContent()
                backBtn.setOnClickListener { findNavController().popBackStack() }
                countryView.onAddItem(viewModel::addCountry)
                countryView.onRemove(viewModel::removeCountry)
                countryView.onSelect(viewModel::selectCountry)
            }
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(viewModel) {
            showTemplateDialog.observeUiDataEvent(
                viewLifecycleOwner,
                ::openTemplateDialog
            )
            openContextMenu.observeUiDataEvent(viewLifecycleOwner) { menu ->
                viewModel.navigateToContextMenu(
                    this@CriminalCertStepNationalityF,
                    menu
                )
            }

            viewModel.navigateToCountrySelection.observeUiDataEvent(
                viewLifecycleOwner,
                ::navigateToCountrySelection
            )
            onNextEvent.observeUiDataEvent(viewLifecycleOwner, ::navigateNext)

            showRatingDialogByUserInitiative.observeUiDataEvent(
                viewLifecycleOwner
            ) { ratingModel ->
                viewModel.navigateToRatingService(
                    fragment = this@CriminalCertStepNationalityF,
                    ratingFormModel = ratingModel,
                    id = null,
                    destinationId = R.id.criminalCertStepNationalityF,
                    resultKey = ActionsConst.RESULT_KEY_RATING_SERVICE,
                    screenCode = CriminalCertRatingScreenCodes.SC_NATIONALITY,
                    ratingType = ActionsConst.TYPE_USER_INITIATIVE,
                    formCode = ratingModel.formCode
                )
            }
        }
        registerForTemplateDialogNavResult { action ->
            findNavController().popBackStack()
            when (action) {
                ActionsConst.GENERAL_RETRY -> viewModel.retryLastAction()
                ActionsConst.FAQ_CATEGORY -> viewModel.navigateToFaq(
                    this@CriminalCertStepNationalityF,
                    CriminalCertConst.FEATURE_CODE
                )
                ActionsConst.SUPPORT_SERVICE -> viewModel.navigateToSupport(this@CriminalCertStepNationalityF)
                ActionsConst.RATING -> viewModel.getRatingForm()
            }
        }

        registerForNavigationResultOnce(SEARCH, viewModel::setCountry)

        registerForNavigationResult<ConsumableItem>(ActionsConst.RESULT_KEY_RATING_SERVICE) { event ->
            event.consumeEvent<RatingRequest> { rating ->
                viewModel.sendRatingRequest(
                    rating
                )
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    private fun navigateToCountrySelection(items: List<NationalityItem>) {
        navigate(
            CriminalCertStepNationalityFDirections.actionCriminalCertStepNationalityFToSearchF(
                key = SEARCH,
                searchableList = items.toTypedArray()
            )
        )
    }

    private fun navigateNext(screenData: Pair<CriminalCertScreen, List<String>>) {
        val destination = when (screenData.first) {
            CriminalCertScreen.BIRTH_PLACE -> {
                CriminalCertStepNationalityFDirections.actionCriminalCertStepNationalityFToCriminalCertStepBirthF(
                    contextMenu = args.contextMenu,
                    dataUser = args.dataUser.copy(
                        nationalities = screenData.second
                    )
                )
            }
            CriminalCertScreen.NATIONALITIES -> {
                //already here
                null
            }
            CriminalCertScreen.REGISTRATION_PLACE -> CriminalCertStepNationalityFDirections.actionCriminalCertStepNationalityFToCriminalCertStepAddressF(
                contextMenu = args.contextMenu,
                dataUser = args.dataUser.copy(
                    nationalities = screenData.second
                )
            )
            CriminalCertScreen.CONTACTS -> CriminalCertStepNationalityFDirections.actionCriminalCertStepNationalityFToCriminalCertStepContactsF(
                contextMenu = args.contextMenu,
                dataUser = args.dataUser.copy(
                    nationalities = screenData.second
                )
            )
        }
        destination?.run(::navigate)
    }

    private companion object {
        private const val SEARCH = "SEARCH"
    }
}