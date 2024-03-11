package ua.gov.diia.ps_criminal_cert.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import ua.gov.diia.ps_criminal_cert.R
import ua.gov.diia.ps_criminal_cert.databinding.FragmentCriminalCertHomeBinding
import ua.gov.diia.ps_criminal_cert.ui.CriminalCertConst
import ua.gov.diia.core.models.ConsumableItem
import ua.gov.diia.core.models.rating_service.RatingRequest
import ua.gov.diia.core.ui.dynamicdialog.ActionsConst
import ua.gov.diia.core.util.event.observeUiDataEvent
import ua.gov.diia.core.util.extensions.fragment.doOnSystemBackPressed
import ua.gov.diia.core.util.extensions.fragment.navigate
import ua.gov.diia.ui_base.util.navigation.openTemplateDialog
import ua.gov.diia.core.util.extensions.fragment.registerForNavigationResult
import ua.gov.diia.core.util.extensions.fragment.registerForTemplateDialogNavResult
import ua.gov.diia.ps_criminal_cert.ui.CriminalCertRatingScreenCodes

@AndroidEntryPoint
class CriminalCertHomeF : Fragment() {

    private val viewModel: CriminalCertHomeVM by viewModels()
    private val args: CriminalCertHomeFArgs by navArgs()
    private var binding: FragmentCriminalCertHomeBinding? = null

    private var tabsAdapter: CriminalCertHomeTabsAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        with(viewModel) {
            setContextMenu(args.contextMenu)
            setCertId(args.certId, args.directionFlag, args.resourceId)
            setScreenName(getString(R.string.criminal_cert_title))
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentCriminalCertHomeBinding.inflate(inflater, container, false)
            .apply {
                lifecycleOwner = viewLifecycleOwner
                vm = viewModel
                viewModel.setListRefreshing()
                setupTabs()

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
                    this@CriminalCertHomeF,
                    menu
                )
            }

            navigateToWelcome.observeUiDataEvent(viewLifecycleOwner, ::navigateToWelcome)
            navigateToDetails.observeUiDataEvent(viewLifecycleOwner, ::navigateToDetails)

            showRatingDialogByUserInitiative.observeUiDataEvent(viewLifecycleOwner) { ratingModel ->
                viewModel.navigateToRatingService(
                    fragment = this@CriminalCertHomeF,
                    ratingFormModel = ratingModel,
                    id = null,
                    destinationId = R.id.criminalCertHomeF,
                    resultKey = ActionsConst.RESULT_KEY_RATING_SERVICE,
                    screenCode = CriminalCertRatingScreenCodes.SC_HOME,
                    ratingType = ActionsConst.TYPE_USER_INITIATIVE,
                    formCode = ratingModel.formCode
                )
            }
        }
        registerForTemplateDialogNavResult { action ->
            findNavController().popBackStack()
            when (action) {
                ActionsConst.GENERAL_RETRY -> viewModel.retryLastAction()
                ActionsConst.FAQ_CATEGORY -> viewModel.navigateToFaq(this@CriminalCertHomeF, CriminalCertConst.FEATURE_CODE)
                ActionsConst.SUPPORT_SERVICE -> viewModel.navigateToSupport(this@CriminalCertHomeF)
                ActionsConst.RATING -> viewModel.getRatingForm()
            }
        }
        registerForNavigationResult<ConsumableItem>(ActionsConst.RESULT_KEY_RATING_SERVICE) { event ->
            event.consumeEvent<RatingRequest> { rating -> viewModel.sendRatingRequest(rating) }
        }
    }

    private fun navigateToWelcome(skip: Boolean) {
        if (skip) {
            navigate(
                CriminalCertHomeFDirections.actionCriminalCertHomeFToCriminalCertWelcomeFSkip(
                    contextMenu = args.contextMenu,
                    certId = args.certId,
                    publicService = args.publicService,
                    resourceId = args.resourceId,
                    directionFlag = args.directionFlag
                )
            )
        } else {
            navigate(
                CriminalCertHomeFDirections.actionCriminalCertHomeFToCriminalCertWelcomeF(
                    contextMenu = args.contextMenu,
                    publicService = args.publicService,
                    resourceId = args.resourceId,
                    directionFlag = args.directionFlag
                )
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        tabsAdapter = null
        binding = null
    }

    private fun navigateToDetails(id: String) {
        navigate(
            CriminalCertHomeFDirections.actionCriminalCertHomeFToCriminalCertDetailsF(
                certId = id
            )
        )
    }

    private fun navigateToBack() {
        if (viewModel.directionFlag.value == false) {
            findNavController().popBackStack()
        } else {
            viewModel.navigateToDamagedPropertyRecovery(this@CriminalCertHomeF, args.certId)
        }
    }

    private fun FragmentCriminalCertHomeBinding.setupTabs() {
        tabsAdapter = CriminalCertHomeTabsAdapter(
            contextMenu = args.contextMenu,
            fragmentManager = childFragmentManager,
            lifecycle = viewLifecycleOwner.lifecycle
        )
        viewPager.offscreenPageLimit = 2
        viewPager.adapter = tabsAdapter
        if (args.isNew) {
            viewPager.setCurrentItem(1, false)
        }
        TabLayoutMediator(tabs, viewPager) { tab, pos ->
            when (pos) {
                0 -> tab.text = getString(R.string.criminal_cert_tab_title_done)
                else -> tab.text = getString(R.string.criminal_cert_tab_title_processing)
            }
        }.attach()
    }
}