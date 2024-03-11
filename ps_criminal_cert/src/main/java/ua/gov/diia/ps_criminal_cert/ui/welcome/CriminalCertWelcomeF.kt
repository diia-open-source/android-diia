package ua.gov.diia.ps_criminal_cert.ui.welcome

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
import ua.gov.diia.ps_criminal_cert.databinding.FragmentCriminalCertWelcomeBinding
import ua.gov.diia.core.models.ConsumableItem
import ua.gov.diia.ps_criminal_cert.models.CriminalCertUserData
import ua.gov.diia.ps_criminal_cert.models.request.PublicService
import ua.gov.diia.core.models.rating_service.RatingRequest
import ua.gov.diia.core.ui.dynamicdialog.ActionsConst
import ua.gov.diia.ps_criminal_cert.ui.CriminalCertConst
import ua.gov.diia.core.util.event.observeUiDataEvent
import ua.gov.diia.core.util.event.observeUiEvent
import ua.gov.diia.core.util.extensions.fragment.*
import ua.gov.diia.ui_base.util.navigation.openTemplateDialog
import ua.gov.diia.ps_criminal_cert.ui.CriminalCertRatingScreenCodes

@AndroidEntryPoint
class CriminalCertWelcomeF : Fragment() {

    private val viewModel: CriminalCertWelcomeVM by viewModels()
    private val args: CriminalCertWelcomeFArgs by navArgs()
    private var binding: FragmentCriminalCertWelcomeBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        with(viewModel) {
            setContextMenu(args.contextMenu)
            handleNotification(args.certId)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentCriminalCertWelcomeBinding.inflate(inflater, container, false)
            .apply {
                lifecycleOwner = viewLifecycleOwner
                vm = viewModel
                viewModel.loadContent(
                    publicService = args.publicService,
                    directionFlag = args.directionFlag,
                    resourceId = args.resourceId
                )
                backBtn.setOnClickListener {
                    navigateToBack()
                }
                doOnSystemBackPressed { navigateToBack() }
            }
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(viewModel) {
            showTemplateDialog.observeUiDataEvent(viewLifecycleOwner, ::openTemplateDialog)
            openContextMenu.observeUiDataEvent(viewLifecycleOwner) { menu ->
                viewModel.navigateToContextMenu(
                    this@CriminalCertWelcomeF,
                    menu
                )
            }

            navigateToReasons.observeUiEvent(viewLifecycleOwner, ::navigateToReasons)
            navigateToDetails.observeUiDataEvent(viewLifecycleOwner, ::navigateToDetails)
            navigateToRequester.observeUiEvent(viewLifecycleOwner, ::navigateToRequester)

            showRatingDialogByUserInitiative.observeUiDataEvent(viewLifecycleOwner) { ratingModel ->
                viewModel.navigateToRatingService(
                    fragment = this@CriminalCertWelcomeF,
                    ratingFormModel = ratingModel,
                    id = null,
                    destinationId = R.id.criminalCertWelcomeF,
                    resultKey = ActionsConst.RESULT_KEY_RATING_SERVICE,
                    screenCode = CriminalCertRatingScreenCodes.SC_START,
                    ratingType = ActionsConst.TYPE_USER_INITIATIVE,
                    formCode = ratingModel.formCode
                )
            }
        }
        registerForTemplateDialogNavResult { action ->
            findNavController().popBackStack()
            when (action) {
                ActionsConst.GENERAL_RETRY -> viewModel.retryLastAction()
                ActionsConst.FAQ_CATEGORY -> viewModel.navigateToFaq(this@CriminalCertWelcomeF, CriminalCertConst.FEATURE_CODE)
                ActionsConst.SUPPORT_SERVICE -> viewModel.navigateToSupport(this@CriminalCertWelcomeF)
                ActionsConst.RATING -> viewModel.getRatingForm()
            }
        }
        registerForNavigationResult<ConsumableItem>(ActionsConst.RESULT_KEY_RATING_SERVICE) { event ->
            event.consumeEvent<RatingRequest> { rating ->   viewModel.sendRatingRequest(rating) }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    private fun navigateToReasons() {
        navigate(
            CriminalCertWelcomeFDirections.actionCriminalCertWelcomeFToCriminalCertStepReasonsF(
                contextMenu = args.contextMenu
            )
        )
    }

    private fun navigateToRequester() {
        navigate(
            CriminalCertWelcomeFDirections.actionCriminalCertWelcomeFToCriminalCertStepRequesterF(
                contextMenu = args.contextMenu,
                dataUser = CriminalCertUserData(
                    reasonId = null,
                    certificateType = null,
                    publicService = PublicService(
                        code = args.publicService,
                        resourceId = args.resourceId
                    )
                )
            )
        )
    }

    private fun navigateToDetails(id: String) {
        navigate(
            CriminalCertWelcomeFDirections.actionCriminalCertWelcomeFToCriminalCertDetailsF(
                certId = id
            )
        )
    }

    private fun navigateToBack() {
        if (viewModel.directionFlag.value == false) {
            findNavController().popBackStack()
        } else {
            viewModel.navigateToDamagedPropertyRecovery(this@CriminalCertWelcomeF, args.certId)
        }
    }
}