package ua.gov.diia.ui_base.mappers.document

import android.content.res.Resources
import android.os.Parcelable
import ua.gov.diia.core.models.document.DiiaDocument
import ua.gov.diia.core.models.document.DocAction
import ua.gov.diia.ui_base.components.molecule.list.ListItemMlcData

interface BaseDocumentActionProvider {
    fun isDocumentProvider(document: Parcelable): Boolean
    fun listOfActions(
        docParcelable: Parcelable,
        resources: Resources
    ): List<ListItemMlcData>

    fun listOfGeneralActions(
        document: DiiaDocument,
        enableStackActions: Boolean,
    ): List<ListItemMlcData>

    fun listOfManualActions(
        docParcelable: Parcelable,
        availableActionsForUser: List<DocAction>?
    ): List<ListItemMlcData>
}