package ua.gov.diia.documents.ui.actions

import android.content.res.Resources
import android.os.Parcelable
import ua.gov.diia.documents.helper.DocumentsHelper
import ua.gov.diia.core.models.document.DiiaDocument
import ua.gov.diia.core.models.document.ManualDocs
import ua.gov.diia.ui_base.mappers.document.BaseDocumentActionProvider
import ua.gov.diia.ui_base.components.atom.button.ButtonIconCircledLargeAtmData
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiText
import ua.gov.diia.ui_base.components.molecule.list.ListItemMlcData
import ua.gov.diia.ui_base.components.organism.list.ContextIconMenuOrgData
import javax.inject.Inject


class DocActionsProviderImpl @Inject constructor(
    val actionProviders: List<@JvmSuppressWildcards BaseDocumentActionProvider>,
    val documentsHelper: DocumentsHelper
) : DocActionsProvider {


    override fun provideActions(
        document: DiiaDocument,
        manualDocs: ManualDocs?,
        enableStackActions: Boolean,
        resources: Resources
    ): ContextIconMenuOrgData {
        val qr = ButtonIconCircledLargeAtmData(
            actionKey = VerificationActions.VERIFICATION_CODE_QR,
            id = "qr",
            label = "QR-код",
            icon = UiText.StringResource(ua.gov.diia.ui_base.R.drawable.ic_doc_qr_selected)
        )
        val ean13 = ButtonIconCircledLargeAtmData(
            actionKey = VerificationActions.VERIFICATION_CODE_EAN13,
            id = "ean",
            label = "Штрихкод",
            icon = UiText.StringResource(ua.gov.diia.ui_base.R.drawable.ic_doc_ean13_selected),
        )
        return ContextIconMenuOrgData(
            docActions = listOfActions(document, resources).toMutableList(),
            manualActions = listOfManualActions(document, manualDocs),
            generalActions = listOfGeneralActions(
                document,
                enableStackActions,
            ).toMutableList(),
            qr = qr,
            ean13 = ean13,
            showButtons = documentsHelper.showVerificationButtons(document),
            showDividerForManualActions = manualDocs != null
        )
    }

    private fun listOfActions(
        document: Parcelable,
        resources: Resources
    ): List<ListItemMlcData> {
        val actionProvider = actionProviders.find { it.isDocumentProvider(document) }
        return actionProvider?.let {
            actionProvider.listOfActions(document, resources)
        } ?: emptyList()
    }

    private fun listOfGeneralActions(
        document: DiiaDocument,
        enableStackActions: Boolean,
    ): List<ListItemMlcData> {
        val generalActionProvider = actionProviders.find { it.isDocumentProvider(document) }
        return generalActionProvider?.let {
            generalActionProvider.listOfGeneralActions(document, enableStackActions)
        } ?: emptyList()
    }

    private fun listOfManualActions(
        document: DiiaDocument,
        manualDocs: ManualDocs?
    ): List<ListItemMlcData> {
        val manualActionsProvider = actionProviders.find { it.isDocumentProvider(document) }
        return manualActionsProvider?.let {
            manualActionsProvider.listOfManualActions(document, manualDocs?.documents)
        } ?: emptyList()
    }

}