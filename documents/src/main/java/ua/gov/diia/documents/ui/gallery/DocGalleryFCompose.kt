package ua.gov.diia.documents.ui.gallery

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import dagger.hilt.android.AndroidEntryPoint
import ua.gov.diia.core.util.delegation.WithBuildConfig
import ua.gov.diia.core.util.delegation.WithCrashlytics
import ua.gov.diia.core.util.extensions.activity.setWindowBrightness
import ua.gov.diia.core.util.extensions.fragment.currentDestinationId
import ua.gov.diia.core.util.extensions.fragment.navigate
import ua.gov.diia.core.util.extensions.fragment.sendImage
import ua.gov.diia.core.util.extensions.fragment.sendPdf
import ua.gov.diia.diia_storage.AndroidBase64Wrapper
import ua.gov.diia.documents.NavDocActionsDirections
import ua.gov.diia.documents.R
import ua.gov.diia.documents.helper.DocumentsHelper
import ua.gov.diia.documents.models.DiiaDocument
import ua.gov.diia.documents.models.ManualDocs
import ua.gov.diia.documents.ui.fullinfo.FullInfoFCompose
import ua.gov.diia.documents.ui.fullinfo.FullInfoFComposeArgs
import ua.gov.diia.documents.util.view.showCopyDocIdClipedSnackBar
import ua.gov.diia.ui_base.components.DiiaResourceIconProvider
import ua.gov.diia.ui_base.components.infrastructure.HomeScreenTab
import ua.gov.diia.ui_base.components.infrastructure.collectAsEffect
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

    @Inject
    lateinit var diiaResourceIconProvider: DiiaResourceIconProvider

    private var composeView: ComposeView? = null
    private val viewModel: DocGalleryVMCompose by viewModels()
    private val args: DocGalleryFComposeArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        composeView = ComposeView(requireContext())
        return composeView
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.doInit(args.settings ?: DocFSettings.default)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.invalidateDataSource()

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


            viewModel.apply {
                navigation.collectAsEffect { navigation ->
                    when (navigation) {

                        is DocGalleryVMCompose.Navigation.ToDocActions -> {
                            navigateToDocActions(
                                navigation.doc,
                                navigation.position,
                                navigation.manualDocs
                            )
                        }

                        is DocGalleryVMCompose.Navigation.NavToVehicleInsurance -> {
                            fullDocAction(navigation.doc)
                        }

                        is DocGalleryVMCompose.Navigation.ToDocStackOrder -> navigateToDocOrder()

                        is DocGalleryVMCompose.Navigation.ToDocStack -> {
                            navigateToStackDocs(navigation.doc)
                        }
                    }
                }
                docAction.collectAsEffect { action ->
                    when (action) {
                        is DocGalleryVMCompose.DocActions.OpenDriverAccount -> {
                            navigateToWebView(getString(R.string.url_driver_e_find_address))
                        }

                        is DocGalleryVMCompose.DocActions.OpenElectronicQueue -> {
                            navigateToWebView(getString(R.string.url_driver_e_queue))
                        }

                        is DocGalleryVMCompose.DocActions.DocNumberCopy -> {
                            composeView?.showCopyDocIdClipedSnackBar(
                                action.value,
                                topPadding = 40f
                            )
                        }

                        is DocGalleryVMCompose.DocActions.ShareImage -> {
                            sendImage(
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
            }

            viewModel.certificatePdf.observe(viewLifecycleOwner) { event ->
                event.getContentIfNotHandled()?.let { e ->
                    val bArray =
                        AndroidBase64Wrapper().decode(e.docPDF.toByteArray())
                    sendPdf(
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
                    viewModel,
                    ratingModel,
                    isFromStack = false
                )
            }

            HomeScreenTab(
                progressIndicator = progressIndicator.value,
                connectivityState = connectivityState.value,
                body = body,
                onEvent = {
                    viewModel.onUIAction(it)
                },
                diiaResourceIconProvider = diiaResourceIconProvider,
            )
        }
        navigationSubscriptionHandler.subscribeForNavigationEvents(
            this,
            viewModel
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        composeView = null
    }

    private fun fullDocAction(document: DiiaDocument) {
        val fullDocFragment = FullInfoFCompose().apply {
            arguments = FullInfoFComposeArgs(document).toBundle()
        }
        fullDocFragment.show(
            requireActivity().supportFragmentManager,
            FULL_DOC_INFO_TRANSACTION_TAG
        )
    }

    private fun navigateToDocActions(
        doc: DiiaDocument,
        position: Int,
        manualDocs: ManualDocs?
    ) {
        navigate(
            NavDocActionsDirections.actionGlobalDestinationDocActionsCompose(
                doc = doc,
                position = position,
                enableStackActions = false,
                currentlyDisplayedOdcTypes = viewModel.settings.documentType,
                manualDocs = manualDocs,
                resultDestinationId = currentDestinationId ?: return
            )
        )
    }

    private fun navigateToStackDocs(doc: DiiaDocument) {
        documentsHelper.navigateToStackDocs(this, doc)
    }

    override fun onPause() {
        super.onPause()
        activity?.setWindowBrightness(true)
    }

    private fun navigateToDocOrder() {
        documentsHelper.navigateToDocOrder(this)
    }

    override fun onResume() {
        super.onResume()
        viewModel.scrollToLastDocPos()
    }

    companion object {
        private const val FULL_DOC_INFO_TRANSACTION_TAG = "FULL_DOC_INF"
    }
}