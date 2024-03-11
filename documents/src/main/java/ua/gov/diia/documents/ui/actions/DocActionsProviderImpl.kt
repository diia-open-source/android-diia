package ua.gov.diia.documents.ui.actions

import android.content.res.Resources
import android.os.Parcelable
import ua.gov.diia.documents.helper.DocumentsHelper
import ua.gov.diia.documents.models.DiiaDocument
import ua.gov.diia.documents.models.DocError
import ua.gov.diia.documents.models.ManualDocs
import ua.gov.diia.documents.util.BaseDocActionItemProcessor
import ua.gov.diia.documents.util.BaseDocumentActionProvider
import ua.gov.diia.documents.util.DocumentActionMapper
import ua.gov.diia.ui_base.components.atom.button.ButtonIconCircledLargeAtmData
import ua.gov.diia.ui_base.components.infrastructure.DataActionWrapper
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiText
import ua.gov.diia.ui_base.components.molecule.list.ListItemMlcData
import ua.gov.diia.ui_base.components.organism.list.ContextMenuOrgData
import javax.inject.Inject



class DocActionsProviderImpl @Inject constructor(
    val actionProviders: List<@JvmSuppressWildcards BaseDocumentActionProvider>,
    val documentActionMapper: DocumentActionMapper,
    val documentsHelper: DocumentsHelper,
    val actionItemProcessorList: List<@JvmSuppressWildcards BaseDocActionItemProcessor>) : DocActionsProvider {


    override fun provideActions(
        document: DiiaDocument,
        manualDocs: ManualDocs?,
        enableStackActions: Boolean,
        resources: Resources
    ): ContextMenuOrgData {
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
        return ContextMenuOrgData(
            docActions = listOfActions(document, resources).toMutableList(),
            generalActions = listOfGeneralActions(
                document,
                enableStackActions,
                resources
            ).toMutableList(),
            manualActions = listOfManualActions(document, manualDocs),
            qr, ean13, documentsHelper.showVerificationButtons(document)
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
        resources: Resources
    ): List<ListItemMlcData> {
        val generalActions = listOf(
            documentActionMapper.docActionForType(
                document,
                ContextMenuType.RATE_DOCUMENT.code,
                ContextMenuType.RATE_DOCUMENT.name,
                resources
            ),
            if (enableStackActions) documentActionMapper.docActionForType(
                document,
                ContextMenuType.CHANGE_DISPLAY_ORDER.code,
                ContextMenuType.CHANGE_DISPLAY_ORDER.name,
                resources
            ) else documentActionMapper.docActionForType(
                document,
                ContextMenuType.CHANGE_DOC_ORDERING.code,
                ContextMenuType.CHANGE_DOC_ORDERING.name,
                resources
            ),
            documentActionMapper.docActionForType(document,
                ContextMenuType.FAQS.code,
                ContextMenuType.FAQS.name,
                resources)
        )
        val housingCertificatesGeneralActions = listOf(
            if (enableStackActions) documentActionMapper.docActionForType(
                document,
                ContextMenuType.CHANGE_DISPLAY_ORDER.code,
                ContextMenuType.CHANGE_DISPLAY_ORDER.name,
                resources
            ) else documentActionMapper.docActionForType(
                document,
                ContextMenuType.CHANGE_DOC_ORDERING.code,
                ContextMenuType.CHANGE_DOC_ORDERING.name,
                resources
            ), documentActionMapper.docActionForType(document,
                ContextMenuType.FAQS.code,
                ContextMenuType.FAQS.name,
                resources)

        )
        val list = documentsHelper.provideActions(document, enableStackActions, resources)
        if(list != null) {
            return list
        }
        return if (documentsHelper.isDocRequireGeneralMenuActions(document)) {
            generalActions
        } else if(documentsHelper.isDocRequireHousingMenuActions(document)) {
            housingCertificatesGeneralActions
        } else {
            emptyList()
        }
    }

    private fun listOfManualActions(
        document: DiiaDocument,
        manualDocs: ManualDocs?
    ): List<ListItemMlcData>? {
        return when (document) {
            is DocError -> {
                checkAvailableDocsEnum(manualDocs)
            }

            else -> {
                null
            }
        }
    }

    private fun checkAvailableDocsEnum(docs: ManualDocs?): List<ListItemMlcData> {
        val result = mutableListOf<ListItemMlcData>()
        docs?.documents.orEmpty().forEach {


            val menu =
                ContextMenuType.values().find { menu -> menu.code == it.code }
            if (menu != null) {
                result.add(
                    ListItemMlcData(
                        actionKey = menu.code,
                        id = it.code,
                        label = UiText.DynamicString(it.name),
                        action = DataActionWrapper(type = menu.code)
                    )
                )
            }

            var resultItem: ListItemMlcData? = null
            for (itemProcessor in actionItemProcessorList) {
                resultItem = itemProcessor.generateListItem(it.code, it.name)
                if(resultItem != null) {
                    result.add(resultItem)
                    break
                }
            }
        }
        return result
    }

}