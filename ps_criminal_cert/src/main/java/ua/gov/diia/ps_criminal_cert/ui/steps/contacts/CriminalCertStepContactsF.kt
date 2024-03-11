package ua.gov.diia.ps_criminal_cert.ui.steps.contacts

import android.os.Bundle
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
import ua.gov.diia.ps_criminal_cert.databinding.FragmentCriminalCertStepContactsBinding
import ua.gov.diia.core.models.ConsumableItem
import ua.gov.diia.core.models.rating_service.RatingRequest
import ua.gov.diia.core.ui.dynamicdialog.ActionsConst
import ua.gov.diia.ps_criminal_cert.ui.CriminalCertConst
import ua.gov.diia.core.util.event.observeUiDataEvent
import ua.gov.diia.core.util.extensions.fragment.*
import ua.gov.diia.ui_base.util.navigation.openTemplateDialog
import ua.gov.diia.ps_criminal_cert.ui.CriminalCertRatingScreenCodes

@AndroidEntryPoint
class CriminalCertStepContactsF : Fragment() {

    private val viewModel: CriminalCertStepContactsVM by viewModels()
    private val args: CriminalCertStepContactsFArgs by navArgs()
    private var binding: FragmentCriminalCertStepContactsBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        with(viewModel) {
            setContextMenu(args.contextMenu)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentCriminalCertStepContactsBinding.inflate(inflater, container, false)
            .apply {
                lifecycleOwner = viewLifecycleOwner
                vm = viewModel
                viewModel.loadContent()
                backBtn.setOnClickListener { findNavController().popBackStack() }
                phoneInput.setImeOptions(EditorInfo.IME_ACTION_DONE)
            }
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(viewModel) {
            showTemplateDialog.observeUiDataEvent(viewLifecycleOwner, ::openTemplateDialog)
            openContextMenu.observeUiDataEvent(viewLifecycleOwner) { menu ->
                viewModel.navigateToContextMenu(
                    this@CriminalCertStepContactsF,
                    menu
                )
            }

            onNextEvent.observeUiDataEvent(viewLifecycleOwner, ::navigateNext)

            showRatingDialogByUserInitiative.observeUiDataEvent(viewLifecycleOwner) { ratingModel ->
                viewModel.navigateToRatingService(
                    fragment = this@CriminalCertStepContactsF,
                    ratingFormModel = ratingModel,
                    id = null,
                    destinationId = R.id.criminalCertStepContactsF,
                    resultKey = ActionsConst.RESULT_KEY_RATING_SERVICE,
                    screenCode = CriminalCertRatingScreenCodes.SC_CONTACTS,
                    ratingType = ActionsConst.TYPE_USER_INITIATIVE,
                    formCode = ratingModel.formCode
                )
            }
        }
        registerForTemplateDialogNavResult { action ->
            findNavController().popBackStack()
            when (action) {
                ActionsConst.GENERAL_RETRY -> viewModel.retryLastAction()
                ActionsConst.FAQ_CATEGORY -> viewModel.navigateToFaq(this@CriminalCertStepContactsF, CriminalCertConst.FEATURE_CODE)
                ActionsConst.SUPPORT_SERVICE -> viewModel.navigateToSupport(this@CriminalCertStepContactsF)
                ActionsConst.RATING -> viewModel.getRatingForm()
            }
        }
        registerForNavigationResult<ConsumableItem>(ActionsConst.RESULT_KEY_RATING_SERVICE) { event ->
            event.consumeEvent<RatingRequest> { rating ->   viewModel.sendRatingRequest(rating) }
        }
    }

    private fun navigateNext(phone: String) {
        navigate(
            CriminalCertStepContactsFDirections.actionCriminalCertStepContactsFToCriminalCertStepConfirmF(
                contextMenu = args.contextMenu,
                dataUser = args.dataUser.copy(
                    phoneNumber = phone
                )
            )
        )
    }
}