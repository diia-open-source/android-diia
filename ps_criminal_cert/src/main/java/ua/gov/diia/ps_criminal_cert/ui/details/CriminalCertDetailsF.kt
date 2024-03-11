package ua.gov.diia.ps_criminal_cert.ui.details

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
import ua.gov.diia.ps_criminal_cert.databinding.FragmentCriminalCertDetailsBinding
import ua.gov.diia.core.models.ConsumableItem
import ua.gov.diia.core.models.rating_service.RatingRequest
import ua.gov.diia.core.ui.dynamicdialog.ActionsConst
import ua.gov.diia.ps_criminal_cert.ui.CriminalCertConst
import ua.gov.diia.core.util.delegation.download_files.base64.DownloadableBase64File
import ua.gov.diia.core.util.event.observeUiDataEvent
import ua.gov.diia.core.util.extensions.fragment.doOnSystemBackPressed
import ua.gov.diia.core.util.extensions.fragment.navigate
import ua.gov.diia.ui_base.util.navigation.openTemplateDialog
import ua.gov.diia.core.util.extensions.fragment.registerForNavigationResult
import ua.gov.diia.core.util.extensions.fragment.registerForTemplateDialogNavResult
import ua.gov.diia.ps_criminal_cert.ui.CriminalCertRatingScreenCodes

@AndroidEntryPoint
class CriminalCertDetailsF : Fragment() {

    private val viewModel: CriminalCertDetailsVM by viewModels()
    private val args: CriminalCertDetailsFArgs by navArgs()
    private var binding: FragmentCriminalCertDetailsBinding? = null

    private var adapter: CriminalCertLoadActionsAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        with(viewModel) {
            setContextMenu(args.contextMenu)
            load(args.certId)
            setScreenName(getString(R.string.criminal_cert_title))
        }
        adapter = CriminalCertLoadActionsAdapter {
            viewModel.loadAction(it, args.certId)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentCriminalCertDetailsBinding.inflate(inflater, container, false)
            .apply {
                lifecycleOwner = viewLifecycleOwner
                vm = viewModel

                recyclerView.adapter = adapter
                backBtn.setOnClickListener { navigateToList() }
            }
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(viewModel) {
            showTemplateDialog.observeUiDataEvent(viewLifecycleOwner, ::openTemplateDialog)
            openContextMenu.observeUiDataEvent(viewLifecycleOwner) { menu ->
                viewModel.navigateToContextMenu(
                    this@CriminalCertDetailsF,
                    menu
                )
            }
            showRatingDialog.observeUiDataEvent(viewLifecycleOwner) { ratingModel ->
                viewModel.navigateToRatingService(
                    this@CriminalCertDetailsF,
                    ratingModel,
                    args.certId,
                    R.id.criminalCertDetailsF,
                    RESULT_KEY_RATING,
                    CriminalCertRatingScreenCodes.SC_STATUS,
                    formCode = ratingModel.formCode
                )
            }
            showRatingDialogByUserInitiative.observeUiDataEvent(viewLifecycleOwner) { ratingModel ->
                viewModel.navigateToRatingService(
                    fragment = this@CriminalCertDetailsF,
                    ratingFormModel = ratingModel,
                    id = null,
                    destinationId = R.id.criminalCertDetailsF,
                    resultKey = ActionsConst.RESULT_KEY_RATING_SERVICE,
                    screenCode = CriminalCertRatingScreenCodes.SC_STATUS,
                    ratingType = ActionsConst.TYPE_USER_INITIATIVE,
                    formCode = ratingModel.formCode
                )
            }
            pdfEvent.observeUiDataEvent(viewLifecycleOwner, ::openPdf)
            zipEvent.observeUiDataEvent(viewLifecycleOwner, ::shareZip)
        }

        registerForTemplateDialogNavResult { action ->
            findNavController().popBackStack()
            when (action) {
                ActionsConst.GENERAL_RETRY -> viewModel.retryLastAction()
                ActionsConst.FAQ_CATEGORY -> viewModel.navigateToFaq(this@CriminalCertDetailsF, CriminalCertConst.FEATURE_CODE)
                ActionsConst.SUPPORT_SERVICE -> viewModel.navigateToSupport(this@CriminalCertDetailsF)
                ActionsConst.RATING -> viewModel.getRatingForm()
            }
        }
        registerForNavigationResult<ConsumableItem>(ActionsConst.RESULT_KEY_RATING_SERVICE) { event ->
            event.consumeEvent<RatingRequest> { rating ->   viewModel.sendRatingRequest(rating) }
        }

        viewModel.state.observe(viewLifecycleOwner) {
            adapter?.submitList(it.loadActions)
        }

        doOnSystemBackPressed(::navigateToList)
    }

    private fun navigateToList() {
        if (args.isNew) {
            navigate(
                CriminalCertDetailsFDirections.actionCriminalCertDetailsFToCriminalCertHomeF(
                    contextMenu = viewModel.getMenu(),
                    isNew = args.isNew
                )
            )
        } else {
            findNavController().popBackStack()
        }
    }

    private fun openPdf(pdfData: DownloadableBase64File) {
        viewModel.sendPdf(this@CriminalCertDetailsF, pdfData.file, pdfData.name)
    }

    private fun shareZip(zipData: DownloadableBase64File) {
        viewModel.sendZip(this@CriminalCertDetailsF, zipData.file, zipData.name)
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

    companion object {
        private const val RESULT_KEY_RATING = "RATING"
    }
}