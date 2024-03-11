package ua.gov.diia.documents.util

import android.content.res.Resources
import android.os.Parcelable
import ua.gov.diia.ui_base.components.molecule.list.ListItemMlcData

interface BaseDocumentActionProvider {
    fun isDocumentProvider(document: Parcelable): Boolean
    fun listOfActions(
        docParcelable: Parcelable,
        resources: Resources): List<ListItemMlcData>

}