package ua.gov.diia.documents.ui.gallery

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import ua.gov.diia.core.models.common.BackStackEvent
import ua.gov.diia.core.models.rating_service.RatingRequest
import ua.gov.diia.core.ui.dynamicdialog.ActionsConst
import ua.gov.diia.core.util.delegation.WithBuildConfig
import ua.gov.diia.core.util.delegation.WithCrashlytics
import ua.gov.diia.core.util.extensions.activity.setWindowBrightness
import ua.gov.diia.core.util.extensions.fragment.collapseApp
import ua.gov.diia.core.util.extensions.fragment.currentDestinationId
import ua.gov.diia.core.util.extensions.fragment.navigateOnce
import ua.gov.diia.core.util.extensions.fragment.openLink
import ua.gov.diia.core.util.extensions.fragment.sendImage
import ua.gov.diia.core.util.extensions.fragment.sendPdf
import ua.gov.diia.diia_storage.AndroidBase64Wrapper
import ua.gov.diia.documents.NavDocActionsDirections
import ua.gov.diia.documents.R
import ua.gov.diia.documents.helper.DocumentsHelper
import ua.gov.diia.core.models.document.DiiaDocument
import ua.gov.diia.core.models.document.ManualDocs
import ua.gov.diia.documents.models.DocumentAction
import ua.gov.diia.documents.navigation.ConfirmRemoveDocBackStackResult
import ua.gov.diia.documents.navigation.Earn13CodeBackStackResult
import ua.gov.diia.documents.navigation.QRCodeBackStackResult
import ua.gov.diia.documents.navigation.RateDocumentBackStackResult
import ua.gov.diia.documents.navigation.RemoveDocumentBackStackResult
import ua.gov.diia.documents.navigation.VerificationCodeBackStackResult
import ua.gov.diia.documents.ui.DocsConst
import ua.gov.diia.documents.util.view.showCopyDocIdClipedSnackBar
import ua.gov.diia.ui_base.components.infrastructure.HomeScreenTab
import ua.gov.diia.ui_base.components.infrastructure.collectAsEffect
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.event.UIActionKeysCompose
import ua.gov.diia.ui_base.navigation.BaseNavigation
import ua.gov.diia.ui_base.util.navigation.openTemplateDialog
import ua.gov.diia.web.util.extensions.fragment.navigateToWebView
import javax.inject.Inject

@AndroidEntryPoint
class DocGalleryFCompose : Fragment() {

    @Inject
    lateinit var withCrashlytics: WithCrashlytics

    @Inject
    lateinit var withBuildConfig: WithBuildConfig

    @Inject
    lateinit var documentsHelper: DocumentsHelper

    @Inject
    lateinit var navigationSubscriptionHandler: DocGalleryNavigationHelper
    private var composeView: ComposeView? = null
    private val viewModel: DocGalleryVMCompose by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        composeView = ComposeView(requireContext())
        return composeView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        composeView?.setContent {
            val body = viewModel.bodyData
            val progressIndicator =
                viewModel.progressIndicator.collectAsState(
                    initial = Pair(
                        "",
                        true
                    )
                )
            val connectivityState = viewModel.connectivity.collectAsState(true)

            val contentLoaded = viewModel.contentLoaded.collectAsState(
                initial = Pair(
                    UIActionKeysCompose.PAGE_LOADING_TRIDENT,
                    true
                )
            )
            viewModel.apply {
                navigation.collectAsEffect { navigation ->
                    when (navigation) {
                        is BaseNavigation.Back -> {
                            activity?.collapseApp()
                        }

                        is DocGalleryVMCompose.Navigation.ToDocActions -> {
                            navigateToDocActions(
                                navigation.doc,
                                navigation.position,
                                navigation.manualDocs
                            )
                        }

                        is DocGalleryVMCompose.Navigation.NavToVehicleInsurance -> {
                            documentsHelper.onTickerClick(this@DocGalleryFCompose, navigation.doc)
                        }

                        is DocGalleryVMCompose.Navigation.ToDocStackOrder -> navigateToDocOrder()

                        is DocGalleryVMCompose.Navigation.ToDocStack -> {
                            navigateToStackDocs(navigation.doc)
                        }
                    }
                }
                docAction.collectAsEffect { action ->
                    when (action) {
                        is DocGalleryVMCompose.DocActions.OpenWebView -> {
                            navigateToWebView(action.url)
                        }

                        is DocGalleryVMCompose.DocActions.OpenNewFlow -> {
                            documentsHelper.navigateToFlow(this@DocGalleryFCompose, action.code)
                        }

                        is DocGalleryVMCompose.DocActions.OpenLink -> {
                            context?.openLink(action.link, withCrashlytics)
                        }

                        is DocGalleryVMCompose.DocActions.DocNumberCopy -> {
                            composeView?.showCopyDocIdClipedSnackBar(
                                action.value,
                                topPadding = 40f
                            )
                        }

                        is DocGalleryVMCompose.DocActions.ShareImage -> {
                            context?.sendImage(
                                action.data.byteArray,
                                action.data.fileName,
                                withBuildConfig.getApplicationId()
                            )
                        }

                        is DocGalleryVMCompose.DocActions.HighBrightness -> {
                            activity?.setWindowBrightness()
                        }

                        is DocGalleryVMCompose.DocActions.DefaultBrightness -> {
                            activity?.setWindowBrightness(true)
                        }
                    }
                }

                viewModel.navigationBackStackEventFlow.collectAsEffect {
                    when (it) {
                        is BackStackEvent.UserActionResult -> {
                            it.data.consumeEvent { docAction ->
                                when (docAction) {
                                    ActionsConst.GENERAL_RETRY -> viewModel.retryLastAction()
                                }
                            }
                        }

                        is BackStackEvent.TemplateRetryResult -> {
                            it.data.consumeEvent { action ->
                                when (action) {
                                    ActionsConst.GENERAL_RETRY -> viewModel.retryLastAction()
                                    ActionsConst.DIALOG_ACTION_CODE_CLOSE,
                                    ActionsConst.ERROR_DIALOG_DEAL_WITH_IT -> findNavController().popBackStack()
                                }
                            }
                        }

                        is BackStackEvent.RatingResult -> {
                            it.data.consumeEvent<RatingRequest> { rating ->
                                viewModel.sendRatingRequest(rating)
                            }
                        }

                        is RemoveDocumentBackStackResult -> {
                            it.data.consumeEvent<DiiaDocument> { doc ->
                                viewModel.removeDoc(doc)
                            }
                        }

                        is ConfirmRemoveDocBackStackResult -> {
                            it.data.consumeEvent<DiiaDocument> { doc ->
                                viewModel.showConfirmDeleteTemplateRemote(doc)
                            }
                        }

                        is RateDocumentBackStackResult -> {
                            it.data.consumeEvent<DiiaDocument> { doc ->
                                viewModel.showRating(doc)
                            }
                        }

                        is QRCodeBackStackResult -> {
                            it.data.consumeEvent<DocumentAction> { docAction ->
                                viewModel.onUIAction(
                                    UIAction(
                                        actionKey = docAction.actionKey,
                                        data = docAction.docType,
                                        optionalId = docAction.position,
                                        optionalType = docAction.id
                                    )
                                )
                            }
                        }

                        is Earn13CodeBackStackResult -> {
                            it.data.consumeEvent<DocumentAction> { docAction ->
                                viewModel.onUIAction(
                                    UIAction(
                                        actionKey = docAction.actionKey,
                                        data = docAction.docType,
                                        optionalId = docAction.position,
                                        optionalType = docAction.id
                                    )
                                )
                            }
                        }

                        is VerificationCodeBackStackResult -> {
                            it.data.consumeEvent<DocumentAction> { docAction ->
                                viewModel.onUIAction(
                                    UIAction(
                                        actionKey = docAction.actionKey,
                                        data = docAction.docType,
                                        optionalId = docAction.position,
                                        optionalType = docAction.id
                                    )
                                )
                            }
                        }
                    }
                }
            }

            viewModel.certificatePdf.observe(viewLifecycleOwner) { event ->
                event.getContentIfNotHandled()?.let { e ->
                    val bArray = AndroidBase64Wrapper().decode(e.docPDF.toByteArray())
                    context?.sendPdf(
                        bArray,
                        event.peekContent().name,
                        withBuildConfig.getApplicationId()
                    )
                }
            }

            viewModel.documentPdf.observe(viewLifecycleOwner) { event ->
                event.getContentIfNotHandled()?.let { e ->
                    val bArray = AndroidBase64Wrapper().decode(e.docPDF.toByteArray())
                    context?.sendPdf(
                        bArray,
                        event.peekContent().name,
                        withBuildConfig.getApplicationId()
                    )
                }
            }

            viewModel.showTemplateDialog.collectAsEffect {
                openTemplateDialog(it.peekContent())
            }

            viewModel.showRatingDialogByUserInitiative.collectAsEffect {
                val ratingModel = it.peekContent()
                documentsHelper.navigateToRatingService(
                    this,
                    viewModel.currentDocId().orEmpty(),
                    ratingModel,
                    isFromStack = false
                )
            }

            HomeScreenTab(
                progressIndicator = progressIndicator.value,
                contentLoaded = contentLoaded.value,
                connectivityState = connectivityState.value,
                body = body,
                onEvent = {
                    viewModel.onUIAction(it)
                })
        }
        navigationSubscriptionHandler.subscribeForNavigationEvents(
            fragment = this,
            navigationBackStackEventFlow = viewModel.navigationBackStackEventFlow,
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        composeView = null
    }

    private fun navigateToDocActions(
        doc: DiiaDocument,
        position: Int,
        manualDocs: ManualDocs?
    ) {
        navigateOnce(
            destination = NavDocActionsDirections.actionGlobalDestinationDocActionsCompose(
                doc = doc,
                position = position,
                enableStackActions = false,
                currentlyDisplayedOdcTypes = DocsConst.DOCUMENT_TYPE_ALL,
                manualDocs = manualDocs,
                resultDestinationId = currentDestinationId ?: return
            ),
            targetDestinationId = R.id.destination_docActionsCompose
        )
    }

    private fun navigateToStackDocs(doc: DiiaDocument) {
        documentsHelper.navigateToStackDocs(this, doc)
    }

    override fun onPause() {
        super.onPause()
        activity?.setWindowBrightness(true)
        viewModel.clearDocFocus()
    }

    private fun navigateToDocOrder() {
        documentsHelper.navigateToDocOrder(this)
    }

    override fun onResume() {
        super.onResume()
        viewModel.scrollToLastDocPos()
    }
}