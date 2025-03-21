package ua.gov.diia.ps_criminal_cert.ui.steps.requester

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
import ua.gov.diia.ps_criminal_cert.databinding.FragmentCriminalCertStepRequesterBinding
import ua.gov.diia.core.models.ConsumableItem
import ua.gov.diia.ps_criminal_cert.models.enums.CriminalCertScreen
import ua.gov.diia.ps_criminal_cert.models.enums.CriminalCertScreen.BIRTH_PLACE
import ua.gov.diia.ps_criminal_cert.models.enums.CriminalCertScreen.CONTACTS
import ua.gov.diia.ps_criminal_cert.models.enums.CriminalCertScreen.NATIONALITIES
import ua.gov.diia.ps_criminal_cert.models.enums.CriminalCertScreen.REGISTRATION_PLACE
import ua.gov.diia.core.ui.dynamicdialog.ActionsConst
import ua.gov.diia.ps_criminal_cert.ui.CriminalCertConst
import ua.gov.diia.ps_criminal_cert.models.PreviousNames
import ua.gov.diia.core.models.rating_service.RatingRequest
import ua.gov.diia.core.util.event.observeUiDataEvent
import ua.gov.diia.core.util.extensions.fragment.*
import ua.gov.diia.ps_criminal_cert.ui.CriminalCertRatingScreenCodes
import ua.gov.diia.ui_base.util.navigation.openTemplateDialog

@AndroidEntryPoint
class CriminalCertStepRequesterF : Fragment() {

    private val viewModel: CriminalCertStepRequesterVM by viewModels()
    private val args: CriminalCertStepRequesterFArgs by navArgs()
    private var binding: FragmentCriminalCertStepRequesterBinding? = null

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
        binding = FragmentCriminalCertStepRequesterBinding.inflate(inflater, container, false)
            .apply {
                lifecycleOwner = viewLifecycleOwner
                vm = viewModel
                viewModel.loadContent()

                firstNameView.onAddItem(viewModel::addFirstName)
                middleNameView.onAddItem(viewModel::addMiddleName)
                lastNameView.onAddItem(viewModel::addLastName)

                firstNameView.onRemove(viewModel::removeFirstName)
                middleNameView.onRemove(viewModel::removeMiddleName)
                lastNameView.onRemove(viewModel::removeLastName)

                firstNameView.onChanged(viewModel::updateFirstName)
                middleNameView.onChanged(viewModel::updateMiddleName)
                lastNameView.onChanged(viewModel::updateLastName)

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
                    this@CriminalCertStepRequesterF,
                    menu
                )
            }
            isAllNamesValid.observe(viewLifecycleOwner) {
                binding?.nextBtn?.setIsEnabled(it)
            }
            onNextEvent.observeUiDataEvent(viewLifecycleOwner, ::navigateNext)

            showRatingDialogByUserInitiative.observeUiDataEvent(viewLifecycleOwner) { ratingModel ->
                viewModel.navigateToRatingService(
                    fragment = this@CriminalCertStepRequesterF,
                    ratingFormModel = ratingModel,
                    id = null,
                    destinationId = R.id.criminalCertStepRequesterF,
                    resultKey = ActionsConst.RESULT_KEY_RATING_SERVICE,
                    screenCode = CriminalCertRatingScreenCodes.SC_REQUESTER_DATA,
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
                    this@CriminalCertStepRequesterF,
                    CriminalCertConst.FEATURE_CODE
                )

                ActionsConst.SUPPORT_SERVICE -> viewModel.navigateToSupport(this@CriminalCertStepRequesterF)
                CriminalCertConst.FEATURE_CODE -> findNavController().popBackStack()
                ActionsConst.RATING -> viewModel.getRatingForm()
            }
        }
        registerForNavigationResult<ConsumableItem>(ActionsConst.RESULT_KEY_RATING_SERVICE) { event ->
            event.consumeEvent<RatingRequest> { rating -> viewModel.sendRatingRequest(rating) }
        }
    }

    private fun navigateNext(screenData: Pair<CriminalCertScreen, PreviousNames>) {
        val destination = when (screenData.first) {
            BIRTH_PLACE -> CriminalCertStepRequesterFDirections.actionCriminalCertStepRequesterFToCriminalCertStepBirthF(
                contextMenu = args.contextMenu,
                dataUser = args.dataUser.copy(
                    prevNames = screenData.second
                )
            )

            NATIONALITIES -> CriminalCertStepRequesterFDirections.actionCriminalCertStepRequesterFToCriminalCertStepNationalityF(
                contextMenu = args.contextMenu,
                dataUser = args.dataUser.copy(
                    prevNames = screenData.second
                )
            )

            REGISTRATION_PLACE -> CriminalCertStepRequesterFDirections.actionCriminalCertStepRequesterFToCriminalCertStepAddressF(
                contextMenu = args.contextMenu,
                dataUser = args.dataUser.copy(
                    prevNames = screenData.second
                )
            )

            CONTACTS -> CriminalCertStepRequesterFDirections.actionCriminalCertStepRequesterFToCriminalCertStepContactsF(
                contextMenu = args.contextMenu,
                dataUser = args.dataUser.copy(
                    prevNames = screenData.second
                )
            )
        }
        navigate(destination)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}