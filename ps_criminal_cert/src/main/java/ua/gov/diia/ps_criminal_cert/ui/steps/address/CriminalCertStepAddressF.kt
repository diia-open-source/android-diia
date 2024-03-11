package ua.gov.diia.ps_criminal_cert.ui.steps.address

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import dagger.hilt.android.AndroidEntryPoint
import ua.gov.diia.ps_criminal_cert.R
import ua.gov.diia.ps_criminal_cert.databinding.FragmentCriminalCertStepAddressBinding
import ua.gov.diia.core.models.ConsumableItem
import ua.gov.diia.address_search.models.AddressIdentifier
import ua.gov.diia.core.models.rating_service.RatingRequest
import ua.gov.diia.search.models.SearchableBullet
import ua.gov.diia.search.models.SearchableItem
import ua.gov.diia.address_search.ui.AddressSearchControllerF
import ua.gov.diia.core.ui.dynamicdialog.ActionsConst
import ua.gov.diia.ps_criminal_cert.ui.CriminalCertConst
import ua.gov.diia.core.util.event.observeUiDataEvent
import ua.gov.diia.core.util.extensions.fragment.navigate
import ua.gov.diia.ui_base.util.navigation.openTemplateDialog
import ua.gov.diia.core.util.extensions.fragment.registerForNavigationResult
import ua.gov.diia.core.util.extensions.fragment.registerForTemplateDialogNavResult
import ua.gov.diia.ps_criminal_cert.ui.CriminalCertRatingScreenCodes

@AndroidEntryPoint
class CriminalCertStepAddressF : AddressSearchControllerF() {

    override val viewModel: CriminalCertStepAddressVM by viewModels()
    private val args: CriminalCertStepAddressFArgs by navArgs()
    private var binding: FragmentCriminalCertStepAddressBinding? = null

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
        binding = FragmentCriminalCertStepAddressBinding.inflate(inflater, container, false)
            .apply {
                lifecycleOwner = viewLifecycleOwner
                vm = viewModel
                backBtn.setOnClickListener { findNavController().popBackStack() }
            }
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(viewModel) {
            showTemplateDialog.observeUiDataEvent(viewLifecycleOwner, ::openTemplateDialog)
            openContextMenu.observeUiDataEvent(viewLifecycleOwner) { menu ->
                viewModel.navigateToContextMenu(
                    this@CriminalCertStepAddressF,
                    menu
                )
            }

            addressResult.observeUiDataEvent(viewLifecycleOwner, ::navigateNext)

            showRatingDialogByUserInitiative.observeUiDataEvent(viewLifecycleOwner) { ratingModel ->
                viewModel.navigateToRatingService(
                    fragment = this@CriminalCertStepAddressF,
                    ratingFormModel = ratingModel,
                    id = null,
                    destinationId = R.id.criminalCertStepAddressF,
                    resultKey = ActionsConst.RESULT_KEY_RATING_SERVICE,
                    screenCode = CriminalCertRatingScreenCodes.SC_REGISTRATION_PLACE_SELECTION,
                    ratingType = ActionsConst.TYPE_USER_INITIATIVE,
                    formCode = ratingModel.formCode
                )
            }
        }
        registerForTemplateDialogNavResult { action ->
            findNavController().popBackStack()
            when (action) {
                ActionsConst.GENERAL_RETRY -> viewModel.retryLastAction()
                ActionsConst.FAQ_CATEGORY -> viewModel.navigateToFaq(this@CriminalCertStepAddressF, CriminalCertConst.FEATURE_CODE)
                ActionsConst.SUPPORT_SERVICE -> viewModel.navigateToSupport(this@CriminalCertStepAddressF)
                ActionsConst.RATING -> viewModel.getRatingForm()
            }
        }
        registerForNavigationResult<ConsumableItem>(ActionsConst.RESULT_KEY_RATING_SERVICE) { event ->
            event.consumeEvent<RatingRequest> { rating ->   viewModel.sendRatingRequest(rating) }
        }
    }

    override fun navigateToListSearch(data: Array<SearchableItem>, resultKey: String) {
        navigate(
            CriminalCertStepAddressFDirections.actionCriminalCertStepAddressFToSearchF(
                key = resultKey,
                searchableList = data
            )
        )
    }

    override fun navigateToBulletSearch(data: Array<SearchableBullet>, resultKey: String) {
        navigate(
            CriminalCertStepAddressFDirections.actionCriminalCertStepAddressFToSearchBulletF(
                resultKey = resultKey,
                data = data,
                screenHeader = viewModel.screenHeader.value.orEmpty(),
                contentTitle = viewModel.addressDescription.value.orEmpty()
            )
        )
    }

    private fun navigateNext(addressIdentifier: AddressIdentifier) {
        navigate(
            CriminalCertStepAddressFDirections.actionCriminalCertStepAddressFToCriminalCertStepContactsF(
                contextMenu = args.contextMenu,
                dataUser = args.dataUser.copy(
                    registrationAddressId = addressIdentifier.resourceId
                )
            )
        )
    }
}