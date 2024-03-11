package ua.gov.diia.ps_criminal_cert.ui.steps.reason

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
import ua.gov.diia.ps_criminal_cert.databinding.FragmentCriminalCertStepReasonsBinding
import ua.gov.diia.core.models.ConsumableItem
import ua.gov.diia.core.ui.dynamicdialog.ActionsConst
import ua.gov.diia.ps_criminal_cert.ui.CriminalCertConst
import ua.gov.diia.ps_criminal_cert.models.CriminalCertUserData
import ua.gov.diia.core.models.rating_service.RatingRequest
import ua.gov.diia.core.util.decorators.ListDelimiterDecorator
import ua.gov.diia.core.util.event.observeUiDataEvent
import ua.gov.diia.core.util.extensions.fragment.*
import ua.gov.diia.ps_criminal_cert.ui.CriminalCertRatingScreenCodes
import ua.gov.diia.ui_base.util.navigation.openTemplateDialog

@AndroidEntryPoint
class CriminalCertStepReasonsF : Fragment() {

    private val viewModel: CriminalCertStepReasonsVM by viewModels()
    private val args: CriminalCertStepReasonsFArgs by navArgs()
    private var binding: FragmentCriminalCertStepReasonsBinding? = null

    private var adapter: CriminalCertReasonsAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        with(viewModel) {
            setContextMenu(args.contextMenu)
        }
        adapter = CriminalCertReasonsAdapter(viewModel::selectReason)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentCriminalCertStepReasonsBinding.inflate(inflater, container, false)
            .apply {
                lifecycleOwner = viewLifecycleOwner
                vm = viewModel
                viewModel.loadContent()
                recyclerView.adapter = adapter
                recyclerView.addItemDecoration(
                    ListDelimiterDecorator(
                        context = requireContext(),
                        dividerRes = R.drawable.divider,
                        ignorePadding = true,
                        includeTopAge = false,
                        includeBottomAge = false
                    )
                )

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
                    this@CriminalCertStepReasonsF,
                    menu
                )
            }

            onNextEvent.observeUiDataEvent(viewLifecycleOwner, ::navigateNext)

            showRatingDialogByUserInitiative.observeUiDataEvent(viewLifecycleOwner) { ratingModel ->
                viewModel.navigateToRatingService(
                    fragment = this@CriminalCertStepReasonsF,
                    ratingFormModel = ratingModel,
                    id = null,
                    destinationId = R.id.criminalCertStepReasonsF,
                    resultKey = ActionsConst.RESULT_KEY_RATING_SERVICE,
                    screenCode = CriminalCertRatingScreenCodes.SC_REASON_SELECTION,
                    ratingType = ActionsConst.TYPE_USER_INITIATIVE,
                    formCode = ratingModel.formCode
                )
            }
        }
        registerForTemplateDialogNavResult { action ->
            findNavController().popBackStack()
            when (action) {
                ActionsConst.GENERAL_RETRY -> viewModel.retryLastAction()
                ActionsConst.FAQ_CATEGORY -> viewModel.navigateToFaq(this@CriminalCertStepReasonsF, CriminalCertConst.FEATURE_CODE)
                ActionsConst.SUPPORT_SERVICE -> viewModel.navigateToSupport(this@CriminalCertStepReasonsF)
                ActionsConst.RATING -> viewModel.getRatingForm()
            }
        }
        registerForNavigationResult<ConsumableItem>(ActionsConst.RESULT_KEY_RATING_SERVICE) { event ->
            event.consumeEvent<RatingRequest> { rating ->   viewModel.sendRatingRequest(rating) }
        }

        viewModel.state.observe(viewLifecycleOwner) {
            adapter?.submitList(it.reasons)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding?.recyclerView?.adapter = null
        binding = null
    }

    override fun onDestroy() {
        super.onDestroy()
        adapter = null
    }

    private fun navigateNext(reasonId: String) {
        navigate(
            CriminalCertStepReasonsFDirections.actionCriminalCertStepReasonsFToCriminalCertStepTypeF(
                contextMenu = args.contextMenu,
                dataUser = CriminalCertUserData(reasonId = reasonId)
            )
        )
    }
}