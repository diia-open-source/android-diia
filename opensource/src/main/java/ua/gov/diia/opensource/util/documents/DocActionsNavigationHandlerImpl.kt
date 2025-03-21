package ua.gov.diia.opensource.util.documents

import android.os.Parcelable
import androidx.navigation.fragment.findNavController
import ua.gov.diia.core.models.ConsumableItem
import ua.gov.diia.core.util.extensions.fragment.navigate
import ua.gov.diia.core.util.extensions.fragment.setNavigationResult
import ua.gov.diia.documents.ui.DocsConst
import ua.gov.diia.core.models.document.DiiaDocument
import ua.gov.diia.core.models.document.ui.DocumentsContextMenuActions
import ua.gov.diia.documents.ui.actions.DocActionsDFCompose
import ua.gov.diia.documents.ui.actions.DocActionsDFComposeArgs
import ua.gov.diia.documents.ui.actions.DocActionsNavigationHandler
import ua.gov.diia.documents.ui.actions.DocActionsVMCompose
import ua.gov.diia.documents.ui.fullinfo.FullInfoFCompose
import ua.gov.diia.documents.ui.fullinfo.FullInfoFComposeArgs
import ua.gov.diia.opensource.NavMainDirections
import javax.inject.Inject

class DocActionsNavigationHandlerImpl @Inject constructor() :
    DocActionsNavigationHandler {
    override fun handleNavigation(
        fragment: DocActionsDFCompose,
        navigation: DocActionsVMCompose.DocActions,
        args: DocActionsDFComposeArgs
    ) {
        with(fragment) {
            when (navigation) {
                is DocActionsVMCompose.DocActions.NavigateByDocAction -> {
                    when (navigation.action) {
                        DocumentsContextMenuActions.FAQS.action -> {
                            dismiss()
                            //For some docs FAQ code should be different from type
                            val type = when (val doc = args.doc as DiiaDocument) {

                                else -> doc.getItemType()
                            }
                            navigate(
                                NavMainDirections.actionHomeFToSettingsF(),
                                findNavController()
                            )
                        }

                        DocumentsContextMenuActions.FULL_DOC.action -> {
                            fullDocAction(args.doc)
                        }

                        DocumentsContextMenuActions.CHANGE_DOC_ORDERING.action -> {
                            dismiss()
                            navigate(NavMainDirections.actionGlobalToStackOrder())
                        }

                        DocumentsContextMenuActions.CHANGE_DISPLAY_ORDER.action -> {
                            dismiss()
                            navigate(NavMainDirections.actionGlobalToStackOrder((args.doc as DiiaDocument).getItemType()))
                        }

                        DocumentsContextMenuActions.DOWNLOAD_CERTIFICATE_PDF.action,
                        DocumentsContextMenuActions.DOWNLOAD_DOCUMENT_PDF.action -> {
                            val doc = args.doc as DiiaDocument
                            dismiss()
                            setNavigationResult(
                                arbitraryDestination = args.resultDestinationId,
                                key = DocsConst.RESULT_KEY_DOWNLOAD_PDF,
                                data = ConsumableItem(doc)
                            )
                        }

                        else -> {}
                    }
                }

                else -> {}
            }
        }
    }

    private fun DocActionsDFCompose.fullDocAction(document: Parcelable) {
        val fullDocFragment = FullInfoFCompose().apply {
            arguments = FullInfoFComposeArgs(document).toBundle()
        }
        dismiss()
        fullDocFragment.show(
            requireActivity().supportFragmentManager,
            FULL_DOC_INFO_TRANSACTION_TAG
        )
    }

    companion object {
        private const val FULL_DOC_INFO_TRANSACTION_TAG = "FULL_DOC_INF"
    }
}