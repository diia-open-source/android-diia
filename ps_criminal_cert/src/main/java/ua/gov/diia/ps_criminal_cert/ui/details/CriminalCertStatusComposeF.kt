package ua.gov.diia.ps_criminal_cert.ui.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import dagger.hilt.android.AndroidEntryPoint
import ua.gov.diia.core.models.ConsumableItem
import ua.gov.diia.core.models.rating_service.RatingRequest
import ua.gov.diia.core.ui.dynamicdialog.ActionsConst
import ua.gov.diia.core.util.extensions.fragment.doOnSystemBackPressed
import ua.gov.diia.core.util.extensions.fragment.navigate
import ua.gov.diia.core.util.extensions.fragment.registerForNavigationResult
import ua.gov.diia.core.util.extensions.fragment.registerForTemplateDialogNavResult
import ua.gov.diia.ps_criminal_cert.R
import ua.gov.diia.ps_criminal_cert.ui.CriminalCertConst.FEATURE_CODE
import ua.gov.diia.ps_criminal_cert.ui.CriminalCertRatingScreenCodes
import ua.gov.diia.ui_base.components.infrastructure.PublicServiceScreen
import ua.gov.diia.ui_base.components.infrastructure.collectAsEffect
import ua.gov.diia.ui_base.components.infrastructure.event.UIActionKeysCompose
import ua.gov.diia.ui_base.navigation.BaseNavigation
import ua.gov.diia.ui_base.util.navigation.openTemplateDialog

@AndroidEntryPoint
class CriminalCertStatusComposeF : Fragment() {

    private val viewModel: CriminalCertStatusComposeVM by viewModels()
    private val args: CriminalCertStatusComposeFArgs by navArgs()
    private var composeView: ComposeView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        with(viewModel) {
            getScreenContent(args.certId)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        composeView = ComposeView(requireContext())
        return composeView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        composeView?.setContent {
            val topGroup = viewModel.topGroupData
            val body = viewModel.bodyData
            val bottom = viewModel.bottomData

            val contentLoaded = viewModel.contentLoaded.collectAsState(
                initial = Pair(
                    UIActionKeysCompose.PAGE_LOADING_CIRCULAR,
                    true
                )
            )
            val progressIndicator =
                viewModel.progressIndicator.collectAsState(initial = Pair("", true))

            viewModel.apply {

                navigation.collectAsEffect { navigation ->
                    when (navigation) {
                        is BaseNavigation.Back -> {
                            navigateToList()
                        }

                        is BaseNavigation.ContextMenu -> {
                            navigateToContextMenu(
                                this@CriminalCertStatusComposeF,
                                navigation.contextMenuArray ?: emptyArray()
                            )
                        }
                    }
                }

                shareCert.collectAsEffect {
                    sendPdf(this@CriminalCertStatusComposeF,it.file, it.name)
                }
                downloadCert.collectAsEffect {
                    sendZip(this@CriminalCertStatusComposeF, it.file, it.name)
                }

                showTemplateDialog.collectAsEffect {
                    openTemplateDialog(it.peekContent())
                }
                showRatingDialogByUserInitiative.collectAsEffect {
                    val ratingModel = it.peekContent()
                    val formCode: String? = ratingModel.formCode
                    navigateToRatingService(
                        fragment = this@CriminalCertStatusComposeF,
                        ratingFormModel = ratingModel,
                        id = args.certId,
                        destinationId = R.id.criminalCertDetailsF,
                        resultKey = ActionsConst.RESULT_KEY_RATING_SERVICE,
                        screenCode = CriminalCertRatingScreenCodes.SC_STATUS,
                        ratingType = ActionsConst.TYPE_USER_INITIATIVE,
                        formCode = formCode
                    )
                }
                showRatingDialog.collectAsEffect {
                    val ratingModel = it.peekContent()
                    val formCode: String? = ratingModel.formCode
                    navigateToRatingService(
                        fragment = this@CriminalCertStatusComposeF,
                        ratingFormModel = ratingModel,
                        id = args.certId,
                        destinationId = R.id.criminalCertDetailsF,
                        resultKey = ActionsConst.RESULT_KEY_RATING_SERVICE,
                        screenCode = CriminalCertRatingScreenCodes.SC_STATUS,
                        ratingType = ActionsConst.RATING_TYPE_REQUESTED,
                        formCode = formCode
                    )
                }
            }

            PublicServiceScreen(
                contentLoaded = contentLoaded.value,
                progressIndicator = progressIndicator.value,
                toolbar = topGroup,
                body = body,
                bottom = bottom,
                onEvent = {
                    viewModel.onUIAction(it)
                })
        }

        registerForTemplateDialogNavResult { action ->
            findNavController().popBackStack()
            when (action) {
                ActionsConst.GENERAL_RETRY -> viewModel.retryLastAction()
                ActionsConst.FAQ_CATEGORY -> viewModel.navigateToFaq(
                    this@CriminalCertStatusComposeF,
                    FEATURE_CODE
                )
                ActionsConst.SUPPORT_SERVICE -> viewModel.navigateToSupport(this@CriminalCertStatusComposeF)
                ActionsConst.ERROR_DIALOG_DEAL_WITH_IT -> findNavController().popBackStack()
                ActionsConst.DIALOG_ACTION_CODE_CLOSE -> findNavController().popBackStack()
                ActionsConst.RATING -> viewModel.getRatingForm()
            }
        }

        registerForNavigationResult<ConsumableItem>(ActionsConst.RESULT_KEY_RATING_SERVICE) { event ->
            event.consumeEvent<RatingRequest> { rating -> viewModel.sendRatingRequest(rating) }
        }
        doOnSystemBackPressed(::navigateToList)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        composeView = null
    }

    private fun navigateToList() {
        if (args.isNew) {
            navigate(
                CriminalCertStatusComposeFDirections.actionCriminalCertDetailsFToCriminalCertHomeF(
                    contextMenu = viewModel.getMenu(),
                    isNew = args.isNew
                )
            )
        } else {
            findNavController().popBackStack()
        }
    }
}