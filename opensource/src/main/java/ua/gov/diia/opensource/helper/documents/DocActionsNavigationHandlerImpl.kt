package ua.gov.diia.opensource.helper.documents

import android.os.Parcelable
import ua.gov.diia.core.util.extensions.fragment.navigate
import ua.gov.diia.documents.models.DiiaDocument
import ua.gov.diia.documents.ui.DocumentsContextMenuActions
import ua.gov.diia.documents.ui.actions.DocActionsDFCompose
import ua.gov.diia.documents.ui.actions.DocActionsDFComposeArgs
import ua.gov.diia.documents.ui.actions.DocActionsNavigationHandler
import ua.gov.diia.documents.ui.actions.DocActionsVMCompose
import ua.gov.diia.documents.ui.fullinfo.FullInfoFCompose
import ua.gov.diia.documents.ui.fullinfo.FullInfoFComposeArgs
import ua.gov.diia.opensource.NavMainXmlDirections
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
                        DocumentsContextMenuActions.FULL_DOC.action -> {
                            fullDocAction(args.doc)
                        }

                        DocumentsContextMenuActions.CHANGE_DOC_ORDERING.action -> {
                            dismiss()
                            navigate(NavMainXmlDirections.actionGlobalToStackOrder())
                        }

                        DocumentsContextMenuActions.CHANGE_DISPLAY_ORDER.action -> {
                            dismiss()
                            navigate(
                                NavMainXmlDirections.actionGlobalToStackOrder(
                                    (args.doc as DiiaDocument).getItemType()
                                )
                            )
                        }

                        else -> Unit
                    }
                }

                else -> Unit
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