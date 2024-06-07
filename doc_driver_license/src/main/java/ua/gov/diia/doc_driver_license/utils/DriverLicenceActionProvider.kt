package ua.gov.diia.doc_driver_license.utils

import android.content.res.Resources
import android.os.Parcelable
import ua.gov.diia.doc_driver_license.R
import ua.gov.diia.doc_driver_license.models.v2.DriverLicenseV2
import ua.gov.diia.documents.models.DiiaDocument
import ua.gov.diia.documents.models.DocAction
import ua.gov.diia.documents.models.LocalizationType
import ua.gov.diia.documents.ui.DocumentsContextMenuActions
import ua.gov.diia.documents.ui.toListItemMlcData
import ua.gov.diia.documents.util.BaseDocumentActionProvider
import ua.gov.diia.ui_base.components.DiiaResourceIcon
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiIcon
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiText
import ua.gov.diia.ui_base.components.molecule.list.ListItemMlcData

class DriverLicenceActionProvider :
    BaseDocumentActionProvider {
    override fun isDocumentProvider(document: Parcelable): Boolean {
        return document is DriverLicenseV2.Data
    }

    override fun listOfActions(
        docParcelable: Parcelable,
        resources: Resources
    ): List<ListItemMlcData> {
        val document = docParcelable as DriverLicenseV2.Data

        val menu = mutableListOf(
            DocumentsContextMenuActions.FULL_DOC.toListItemMlcData(
                label = UiText.StringResource(R.string.full_info),
                iconLeft = UiIcon.DrawableResource(DiiaResourceIcon.DOC_INFO.code)
            )
        )
        document.frontCard.ua?.let {
            if (document.localization() == LocalizationType.eng) {
                menu.add(
                    DocumentsContextMenuActions.TRANSLATE_TO_UA.toListItemMlcData(
                        label = UiText.StringResource(R.string.translate_to_ua),
                        iconLeft = UiIcon.DrawableResource(DiiaResourceIcon.UA.code),
                    )
                )
            } else {
                menu.add(
                    DocumentsContextMenuActions.TRANSLATE_TO_ENG.toListItemMlcData(
                        label = UiText.StringResource(R.string.translate_to_eng),
                        iconLeft = UiIcon.DrawableResource(DiiaResourceIcon.EN.code)
                    )
                )
            }
        }
        return menu
    }

    override fun listOfGeneralActions(
        document: DiiaDocument,
        enableStackActions: Boolean
    ): List<ListItemMlcData> {
        return listOf(
            DocumentsContextMenuActions.RATE_DOCUMENT.toListItemMlcData(
                label = UiText.StringResource(R.string.rate_document),
                iconLeft = UiIcon.DrawableResource(DiiaResourceIcon.RATING.code),
            ),
            if (enableStackActions) {
                DocumentsContextMenuActions.CHANGE_DISPLAY_ORDER.toListItemMlcData(
                    label = UiText.StringResource(R.string.stack_change_doc_type_ordering),
                    iconLeft = UiIcon.DrawableResource(DiiaResourceIcon.REORDER.code),
                )
            } else {
                DocumentsContextMenuActions.CHANGE_DOC_ORDERING.toListItemMlcData(
                    label = UiText.StringResource(R.string.change_doc_ordering),
                    iconLeft = UiIcon.DrawableResource(DiiaResourceIcon.REORDER.code),
                )
            },
            DocumentsContextMenuActions.FAQS.toListItemMlcData(
                label = UiText.StringResource(R.string.faq),
                iconLeft = UiIcon.DrawableResource(DiiaResourceIcon.FAQ.code)
            )
        )
    }

    override fun listOfManualActions(
        docParcelable: Parcelable,
        availableActionsForUser: List<DocAction>?
    ): List<ListItemMlcData> {
        return listOf(
            DriverLicenseContextMenuActions.REPLACE_DRIVER_LICENSE.toListItemMlcData(
                label = UiText.StringResource(R.string.dl_replace_feature_title),
                iconLeft = UiIcon.DrawableResource(DiiaResourceIcon.REFRESH.code),
            )
        )
    }

}