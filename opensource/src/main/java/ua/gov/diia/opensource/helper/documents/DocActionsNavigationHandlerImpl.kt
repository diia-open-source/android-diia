package ua.gov.diia.opensource.helper.documents

import android.os.Parcelable
import ua.gov.diia.core.util.extensions.fragment.navigate
import ua.gov.diia.documents.models.DiiaDocument
import ua.gov.diia.documents.ui.actions.DocActionsDFCompose
import ua.gov.diia.documents.ui.actions.DocActionsDFComposeArgs
import ua.gov.diia.documents.ui.actions.DocActionsNavigationHandler
import ua.gov.diia.documents.ui.actions.DocActionsVMCompose
import ua.gov.diia.documents.ui.fullinfo.FullInfoFCompose
import ua.gov.diia.documents.ui.fullinfo.FullInfoFComposeArgs
import ua.gov.diia.opensource.NavMainXmlDirections
import ua.gov.diia.ui_base.components.infrastructure.navigation.NavigationPath
import javax.inject.Inject

class DocActionsNavigationHandlerImpl @Inject constructor() :
    DocActionsNavigationHandler {
    override fun handleNavigation(
        fragment: DocActionsDFCompose,
        navigation: NavigationPath,
        args: DocActionsDFComposeArgs,
    ) {
        with(fragment) {
            when (navigation) {
                is DocActionsVMCompose.Navigation.NavToFullInfo -> {
                    fullDocAction(args.doc)
                }

                is DocActionsVMCompose.Navigation.ToDocStackOrder -> {
                    dismiss()
                    navigate(NavMainXmlDirections.actionGlobalToStackOrder())
                }

                is DocActionsVMCompose.Navigation.ToDocStackOrderWithType -> {
                    dismiss()
                    navigate(NavMainXmlDirections.actionGlobalToStackOrder((args.doc as DiiaDocument).getItemType()))
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