package ua.gov.diia.ps_criminal_cert.ui.steps.confirm

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
import ua.gov.diia.ps_criminal_cert.databinding.FragmentCriminalCertStepConfirmBinding
import ua.gov.diia.ps_criminal_cert.ui.CriminalCertConst
import ua.gov.diia.core.models.ConsumableItem
import ua.gov.diia.core.models.rating_service.RatingRequest
import ua.gov.diia.core.ui.dynamicdialog.ActionsConst
import ua.gov.diia.core.util.delegation.WithCrashlytics
import ua.gov.diia.core.util.event.observeUiDataEvent
import ua.gov.diia.core.util.extensions.fragment.navigate
import ua.gov.diia.core.util.extensions.fragment.openLink
import ua.gov.diia.ui_base.util.navigation.openTemplateDialog
import ua.gov.diia.core.util.extensions.fragment.registerForNavigationResult
import ua.gov.diia.core.util.extensions.fragment.registerForTemplateDialogNavResult
import ua.gov.diia.ps_criminal_cert.ui.CriminalCertRatingScreenCodes
import javax.inject.Inject

@AndroidEntryPoint
class CriminalCertStepConfirmF : Fragment() {

    @Inject
    lateinit var withCrashlytics: WithCrashlytics
    private val viewModel: CriminalCertStepConfirmVM by viewModels()
    private val args: CriminalCertStepConfirmFArgs by navArgs()
    private var binding: FragmentCriminalCertStepConfirmBinding? = null

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
        binding = FragmentCriminalCertStepConfirmBinding.inflate(inflater, container, false)
            .apply {
                lifecycleOwner = viewLifecycleOwner
                vm = viewModel

                orderBtn.setOnButtonClickListener { viewModel.confirm(args.dataUser) }
                backBtn.setOnClickListener { findNavController().popBackStack() }
                viewModel.load(args.dataUser)
            }
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(viewModel) {
            showTemplateDialog.observeUiDataEvent(viewLifecycleOwner, ::openTemplateDialog)
            openContextMenu.observeUiDataEvent(viewLifecycleOwner) { menu ->
                viewModel.navigateToContextMenu(
                    this@CriminalCertStepConfirmF,
                    menu
                )
            }
            openLinkAM.observeUiDataEvent(viewLifecycleOwner) { openLink(it, withCrashlytics) }

            navigateToDetails.observeUiDataEvent(viewLifecycleOwner, ::navigateToDetails)

            showRatingDialogByUserInitiative.observeUiDataEvent(viewLifecycleOwner) { ratingModel ->
                viewModel.navigateToRatingService(
                    fragment = this@CriminalCertStepConfirmF,
                    ratingFormModel = ratingModel,
                    id = null,
                    destinationId = R.id.criminalCertStepConfirmF,
                    resultKey = ActionsConst.RESULT_KEY_RATING_SERVICE,
                    screenCode = CriminalCertRatingScreenCodes.SC_APPLICATION,
                    ratingType = ActionsConst.TYPE_USER_INITIATIVE,
                    formCode = ratingModel.formCode
                )
            }
        }
        registerForTemplateDialogNavResult { action ->
            findNavController().popBackStack()
            when (action) {
                ActionsConst.GENERAL_RETRY -> viewModel.retryLastAction()
                ActionsConst.FAQ_CATEGORY -> viewModel.navigateToFaq(this@CriminalCertStepConfirmF, CriminalCertConst.FEATURE_CODE)
                ActionsConst.SUPPORT_SERVICE -> viewModel.navigateToSupport(this@CriminalCertStepConfirmF)
                CriminalCertConst.TEMPLATE_ACTION_OPEN_STATUS -> navigateToStatus()


                CriminalCertConst.FEATURE_CODE -> {
                    viewModel.navigateToDetails()
                }

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

    private fun navigateToStatus() {
        viewModel.navigateToDPRecoveryHomeF(this@CriminalCertStepConfirmF, args.dataUser.publicService?.resourceId)
    }

    private fun navigateToDetails(applicationId: String) {
        navigate(
            CriminalCertStepConfirmFDirections.actionCriminalCertStepConfirmFToCriminalCertDetailsF(
                certId = applicationId,
                isNew = true
            )
        )
    }
}