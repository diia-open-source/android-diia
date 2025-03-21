package ua.gov.diia.doc_manual_options.utils

import android.content.res.Resources
import android.os.Parcelable
import ua.gov.diia.doc_manual_options.models.DocManualOptions
import ua.gov.diia.core.models.document.DiiaDocument
import ua.gov.diia.core.models.document.DocAction
import ua.gov.diia.ui_base.components.infrastructure.DataActionWrapper
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiText
import ua.gov.diia.ui_base.components.molecule.list.ListItemMlcData
import ua.gov.diia.ui_base.mappers.document.BaseDocumentActionProvider

class DocManualOptionsActionProvider :
    BaseDocumentActionProvider {
    override fun isDocumentProvider(document: Parcelable): Boolean {
        return document is DocManualOptions
    }

    override fun listOfActions(
        docParcelable: Parcelable,
        resources: Resources
    ): List<ListItemMlcData> {
        return emptyList()
    }

    override fun listOfGeneralActions(
        document: DiiaDocument,
        enableStackActions: Boolean
    ): List<ListItemMlcData> {
        return emptyList()
    }

    override fun listOfManualActions(docParcelable: Parcelable,availableActionsForUser: List<DocAction>?): List<ListItemMlcData> {
        if (availableActionsForUser == null) return emptyList()
        val result = mutableListOf<ListItemMlcData>()
        availableActionsForUser.forEach {
            val menu =
                DocManualOptionsContextMenuActions.entries.find { menu ->
                    menu.action == it.code
                }
            if (menu != null) {
                result.add(
                    ListItemMlcData(
                        actionKey = menu.action,
                        id = it.code,
                        label = UiText.DynamicString(it.name),
                        action = DataActionWrapper(type = menu.action)
                    )
                )
            }
        }
        return result
    }
}