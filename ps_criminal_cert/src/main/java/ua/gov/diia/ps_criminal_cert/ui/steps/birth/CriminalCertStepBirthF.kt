package ua.gov.diia.ps_criminal_cert.ui.steps.birth

import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import dagger.hilt.android.AndroidEntryPoint
import ua.gov.diia.ps_criminal_cert.R
import ua.gov.diia.ps_criminal_cert.databinding.FragmentCriminalCertStepBirthBinding
import ua.gov.diia.core.models.ConsumableItem
import ua.gov.diia.address_search.models.NationalityItem
import ua.gov.diia.ps_criminal_cert.models.enums.CriminalCertScreen
import ua.gov.diia.ps_criminal_cert.models.Birth
import ua.gov.diia.core.models.rating_service.RatingRequest
import ua.gov.diia.core.ui.dynamicdialog.ActionsConst
import ua.gov.diia.ps_criminal_cert.ui.CriminalCertConst
import ua.gov.diia.core.util.event.observeUiDataEvent
import ua.gov.diia.core.util.extensions.fragment.navigate
import ua.gov.diia.ui_base.util.navigation.openTemplateDialog
import ua.gov.diia.core.util.extensions.fragment.registerForNavigationResult
import ua.gov.diia.core.util.extensions.fragment.registerForNavigationResultOnce
import ua.gov.diia.core.util.extensions.fragment.registerForTemplateDialogNavResult
import ua.gov.diia.ps_criminal_cert.ui.CriminalCertRatingScreenCodes

@AndroidEntryPoint
class CriminalCertStepBirthF : Fragment() {

    private val viewModel: CriminalCertStepBirthVM by viewModels()
    private val args: CriminalCertStepBirthFArgs by navArgs()
    private var binding: FragmentCriminalCertStepBirthBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        with(viewModel) {
            setContextMenu(args.contextMenu)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentCriminalCertStepBirthBinding.inflate(inflater, container, false)
            .apply {
                lifecycleOwner = viewLifecycleOwner
                vm = viewModel
                viewModel.loadContent()
                otherCountryInput.setImeOptions(EditorInfo.IME_ACTION_NEXT)
                otherCountryInput.setTextInputType(InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_CAP_WORDS)
                cityInput.setTextInputType(InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_CAP_WORDS)
                cityInput.setImeOptions(EditorInfo.IME_ACTION_DONE)
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
                    this@CriminalCertStepBirthF,
                    menu
                )
            }

            viewModel.navigateToCountrySelection.observeUiDataEvent(viewLifecycleOwner, ::navigateToCountrySelection)
            onNextEvent.observeUiDataEvent(viewLifecycleOwner, ::navigateNext)

            showRatingDialogByUserInitiative.observeUiDataEvent(viewLifecycleOwner) { ratingModel ->
                viewModel.navigateToRatingService(
                    fragment = this@CriminalCertStepBirthF,
                    ratingFormModel = ratingModel,
                    id = null,
                    destinationId = R.id.criminalCertStepBirthF,
                    resultKey = ActionsConst.RESULT_KEY_RATING_SERVICE,
                    screenCode = CriminalCertRatingScreenCodes.SC_BIRTH_PLACE,
                    ratingType = ActionsConst.TYPE_USER_INITIATIVE,
                    formCode = ratingModel.formCode
                )
            }
        }
        registerForTemplateDialogNavResult { action ->
            findNavController().popBackStack()
            when (action) {
                ActionsConst.GENERAL_RETRY -> viewModel.retryLastAction()
                ActionsConst.FAQ_CATEGORY -> viewModel.navigateToFaq(this@CriminalCertStepBirthF, CriminalCertConst.FEATURE_CODE)
                ActionsConst.SUPPORT_SERVICE -> viewModel.navigateToSupport(this@CriminalCertStepBirthF)
                ActionsConst.RATING -> viewModel.getRatingForm()
            }
        }

        registerForNavigationResultOnce(NATIONALITIES, viewModel::setCountry)
        registerForNavigationResult<ConsumableItem>(ActionsConst.RESULT_KEY_RATING_SERVICE) { event ->
            event.consumeEvent<RatingRequest> { rating ->   viewModel.sendRatingRequest(rating) }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    private fun navigateToCountrySelection(items: List<NationalityItem>) {
        navigate(
            CriminalCertStepBirthFDirections.actionCriminalCertStepBirthFToSearchF(
                key = NATIONALITIES,
                searchableList = items.toTypedArray()
            )
        )
    }

    private fun navigateNext(screenData: Pair<CriminalCertScreen, Birth>) {
        val destination = when (screenData.first) {
            CriminalCertScreen.BIRTH_PLACE -> {
                //already here
                null
            }
            CriminalCertScreen.NATIONALITIES -> CriminalCertStepBirthFDirections.actionCriminalCertStepBirthFToCriminalCertStepNationalityF(
                contextMenu = args.contextMenu,
                dataUser = args.dataUser.copy(
                    birth = screenData.second
                )
            )
            CriminalCertScreen.REGISTRATION_PLACE -> CriminalCertStepBirthFDirections.actionCriminalCertStepBirthFToCriminalCertStepAddressF(
                contextMenu = args.contextMenu,
                dataUser = args.dataUser.copy(
                    birth = screenData.second
                )
            )
            CriminalCertScreen.CONTACTS -> CriminalCertStepBirthFDirections.actionCriminalCertStepBirthFToCriminalCertStepContactsF(
                contextMenu = args.contextMenu,
                dataUser = args.dataUser.copy(
                    birth = screenData.second
                )
            )
        }
        destination?.run(::navigate)
    }

    private companion object {
        private const val NATIONALITIES = "nationalities"
    }
}