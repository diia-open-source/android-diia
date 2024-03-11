package ua.gov.diia.opensource.helper.documents

import android.content.res.Resources
import android.os.Parcelable
import ua.gov.diia.doc_driver_license.DriverLicenseV2
import ua.gov.diia.documents.models.LocalizationType
import ua.gov.diia.documents.ui.actions.ContextMenuType
import ua.gov.diia.documents.util.BaseDocumentActionProvider
import ua.gov.diia.documents.util.DocumentActionMapper
import ua.gov.diia.ui_base.components.molecule.list.ListItemMlcData

class DriverLicenceActionProvider(
    private val documentActionMapper: DocumentActionMapper,
) : BaseDocumentActionProvider {

    override fun isDocumentProvider(document: Parcelable): Boolean {
        return document is DriverLicenseV2.Data
    }

    override fun listOfActions(
        docParcelable: Parcelable,
        resources: Resources,
    ): List<ListItemMlcData> {
        val document = docParcelable as DriverLicenseV2.Data

        val menu = mutableListOf(
            documentActionMapper.docActionForType(
                document,
                ContextMenuType.FULL_DOC.code,
                ContextMenuType.FULL_DOC.name,
                resources
            )
        )
        if (document.frontCard.ua == null) {
            //ignore in this case
        } else {
            val type =
                if (document.localization() == LocalizationType.eng)
                    ContextMenuType.TRANSLATE_TO_UA
                else
                    ContextMenuType.TRANSLATE_TO_ENG
            menu.add(documentActionMapper.docActionForType(document, type.code, type.name, resources))
        }

        menu.add(
            documentActionMapper.docActionForType(
                document,
                ContextMenuType.REPLACE_DRIVER_LICENSE.code,
                ContextMenuType.REPLACE_DRIVER_LICENSE.name,
                resources
            )
        )
        return menu
    }

}