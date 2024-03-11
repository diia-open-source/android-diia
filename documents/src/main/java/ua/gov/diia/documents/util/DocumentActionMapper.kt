package ua.gov.diia.documents.util

import android.content.res.Resources
import android.os.Parcelable
import ua.gov.diia.documents.R
import ua.gov.diia.documents.models.DiiaDocument
import ua.gov.diia.documents.ui.actions.ContextMenuType
import ua.gov.diia.ui_base.components.CommonDiiaResourceIcon
import ua.gov.diia.ui_base.components.infrastructure.DataActionWrapper
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiIcon
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiText
import ua.gov.diia.ui_base.components.molecule.list.ListItemMlcData
import javax.inject.Inject

class DocumentActionMapper @Inject constructor(
    private val actionItemProcessorList: List<@JvmSuppressWildcards BaseDocActionItemProcessor>
) {

    fun docActionForType(
        doc: DiiaDocument,
        typeCode: String,
        typeName: String,
        resources: Resources
    ): ListItemMlcData {
        actionItemProcessorList.firstNotNullOfOrNull { it.generateListItem(typeCode) }?.let {
            return it
        }

        return when (typeCode) {
            ContextMenuType.FULL_DOC.code -> ListItemMlcData(
                actionKey = typeName,
                id = typeName,
                label = UiText.StringResource(R.string.full_info),
                iconLeft = UiIcon.DrawableResource(CommonDiiaResourceIcon.DOC_INFO.code),
                action = DataActionWrapper(type = typeName)
            )

            ContextMenuType.HOUSING_CERTIFICATES.code -> ListItemMlcData(
                actionKey = typeName,
                id = typeName,
                label = UiText.StringResource(R.string.full_info),
                iconLeft = UiIcon.DrawableResource(CommonDiiaResourceIcon.DOC_INFO.code),
                action = DataActionWrapper(type = typeName)
            )

            ContextMenuType.PNP.code -> ListItemMlcData(
                actionKey = typeName,
                id = typeName,
                label = UiText.StringResource(R.string.pnp),
                action = DataActionWrapper(type = typeName)
            )

            ContextMenuType.DOWNLOAD_CERTIFICATE_PDF.code -> ListItemMlcData(
                actionKey = typeName,
                id = typeName,
                label = UiText.StringResource(R.string.download_certificate_pdf),
                iconLeft = UiIcon.DrawableResource(CommonDiiaResourceIcon.DOWNLOAD.code),
                action = DataActionWrapper(type = typeName)
            )
            ContextMenuType.VERIFICATION_CODE.code -> {
                val verificationCodesCount = getVerificationCodesCount(doc)
                val text = resources.getQuantityText(
                    R.plurals.code_verification_plural,
                    verificationCodesCount
                )
                ListItemMlcData(
                    actionKey = typeName,
                    id = typeName,
                    label = UiText.DynamicString(text.toString()),
                    iconLeft = UiIcon.DrawableResource(CommonDiiaResourceIcon.QR.code),
                    action = DataActionWrapper(type = typeName)
                )
            }

            ContextMenuType.INSURANCE.code -> ListItemMlcData(
                actionKey = typeName,
                id = typeName,
                label = UiText.StringResource(R.string.vl_insurance),
                iconLeft = UiIcon.DrawableResource(CommonDiiaResourceIcon.POLICE.code),
                action = DataActionWrapper(type = typeName)
            )

            ContextMenuType.OPEN_SAME_DOC_TYPE.code ->
                ListItemMlcData(
                    actionKey = typeName,
                    id = typeName,
                    label = UiText.StringResource(R.string.stack_all_same_doc_type),
                    action = DataActionWrapper(type = typeName)
                )

            ContextMenuType.CHANGE_DISPLAY_ORDER.code -> ListItemMlcData(
                actionKey = typeName,
                id = typeName,
                label = UiText.StringResource(R.string.stack_change_doc_type_ordering),
                iconLeft = UiIcon.DrawableResource(CommonDiiaResourceIcon.REORDER.code),
                action = DataActionWrapper(type = typeName)
            )

            ContextMenuType.CHANGE_DOC_ORDERING.code ->
                ListItemMlcData(
                    actionKey = typeName,
                    id = typeName,
                    label = UiText.StringResource(R.string.change_doc_ordering),
                    iconLeft = UiIcon.DrawableResource(CommonDiiaResourceIcon.REORDER.code),
                    action = DataActionWrapper(type = typeName)
                )

            ContextMenuType.FAQS.code -> ListItemMlcData(
                actionKey = typeName,
                id = typeName,
                label = UiText.StringResource(R.string.faq),
                iconLeft = UiIcon.DrawableResource(CommonDiiaResourceIcon.FAQ.code),
                action = DataActionWrapper(type = typeName)
            )

            ContextMenuType.REPLACE_DRIVER_LICENSE.code ->
                ListItemMlcData(
                    actionKey = typeName,
                    id = typeName,
                    label = UiText.StringResource(R.string.dl_replace_feature_title),
                    iconLeft = UiIcon.DrawableResource(CommonDiiaResourceIcon.REFRESH.code),
                    action = DataActionWrapper(type = typeName)
                )


            ContextMenuType.REMOVE_DOC.code ->
                ListItemMlcData(
                    actionKey = typeName,
                    id = typeName,
                    label = UiText.StringResource(R.string.remove_doc_action),
                    iconLeft = UiIcon.DrawableResource(CommonDiiaResourceIcon.DELETE.code),
                    action = DataActionWrapper(type = typeName)
                )

            ContextMenuType.TRANSLATE_TO_UA.code ->
                ListItemMlcData(
                    actionKey = typeName,
                    id = typeName,
                    label = UiText.StringResource(R.string.translate_to_ua),
                    iconLeft = UiIcon.DrawableResource(CommonDiiaResourceIcon.UA.code),
                    action = DataActionWrapper(type = typeName)
                )

            ContextMenuType.TRANSLATE_TO_ENG.code ->
                ListItemMlcData(
                    actionKey = typeName,
                    id = typeName,
                    label = UiText.StringResource(R.string.translate_to_eng),
                    iconLeft = UiIcon.DrawableResource(CommonDiiaResourceIcon.EN.code),
                    action = DataActionWrapper(type = typeName)
                )

            ContextMenuType.MILITARY_BOND.code -> ListItemMlcData(
                actionKey = typeName,
                id = typeName,
                label = UiText.StringResource(R.string.military_bonds_menu_go_to_mb),
                iconLeft = UiIcon.DrawableResource(CommonDiiaResourceIcon.TRIDENT.code),
                action = DataActionWrapper(type = typeName)

            )

            ContextMenuType.MILITARY_BOND_REMOVE_FROM_DOCS.code -> ListItemMlcData(
                actionKey = typeName,
                id = typeName,
                label = UiText.StringResource(R.string.military_bonds_remove_from_docs),
                iconLeft = UiIcon.DrawableResource(CommonDiiaResourceIcon.DELETE.code),
                action = DataActionWrapper(type = typeName)
            )

            ContextMenuType.RESIDENCE_CERT.code ->
                ListItemMlcData(
                    actionKey = typeName,
                    id = typeName,
                    label = UiText.StringResource(R.string.residence_cert),
                    iconLeft = UiIcon.DrawableResource(CommonDiiaResourceIcon.HOME_DOC.code),
                    action = DataActionWrapper(type = typeName)
                )


            ContextMenuType.RESIDENCE_CERT_CHILD.code ->
                ListItemMlcData(
                    actionKey = typeName,
                    id = typeName,
                    label = UiText.StringResource(R.string.residence_cert),
                    iconLeft = UiIcon.DrawableResource(CommonDiiaResourceIcon.HOME_DOC.code),
                    action = DataActionWrapper(type = typeName)
                )

            ContextMenuType.EDIT_INTERNALLY_DISPLACED_PERSON_ADDRESS.code -> ListItemMlcData(
                actionKey = typeName,
                id = typeName,
                label = UiText.StringResource(R.string.idp_edit_person_address),
                iconLeft = UiIcon.DrawableResource(CommonDiiaResourceIcon.EDIT.code),
                action = DataActionWrapper(type = typeName)
            )

            ContextMenuType.INTERNALLY_DISPLACED_CERT_CANCEL.code -> ListItemMlcData(
                actionKey = typeName,
                id = typeName,
                label = UiText.StringResource(R.string.idp_action_button_cancel),
                iconLeft = UiIcon.DrawableResource(CommonDiiaResourceIcon.CANCEL.code),
                action = DataActionWrapper(type = typeName)
            )

            ContextMenuType.PROPER_USER_SHARE.code -> ListItemMlcData(
                actionKey = typeName,
                id = typeName,
                label = UiText.StringResource(R.string.idp_share_car),
                iconLeft = UiIcon.DrawableResource(CommonDiiaResourceIcon.SHARE.code),
                action = DataActionWrapper(type = typeName)
            )

            ContextMenuType.PROPER_USER_OWNER_CANCEL.code -> ListItemMlcData(
                actionKey = typeName,
                id = typeName,
                label = UiText.StringResource(R.string.idp_share_car_owner_cancel),
                iconLeft = UiIcon.DrawableResource(CommonDiiaResourceIcon.CANCEL.code),
                action = DataActionWrapper(type = typeName)
            )

            ContextMenuType.PROPER_USER_PROPER_CANCEL.code -> ListItemMlcData(
                actionKey = typeName,
                id = typeName,
                label = UiText.StringResource(R.string.idp_share_car_proper_cancel),
                iconLeft = UiIcon.DrawableResource(CommonDiiaResourceIcon.CANCEL.code),
                action = DataActionWrapper(type = typeName)
            )

            ContextMenuType.RATE_DOCUMENT.code -> ListItemMlcData(
                actionKey = typeName,
                id = typeName,
                label = UiText.StringResource(R.string.rate_document),
                iconLeft = UiIcon.DrawableResource(CommonDiiaResourceIcon.RATING.code),
                action = DataActionWrapper(type = typeName)
            )

            ContextMenuType.SHARE_WITH_FRIENDS.code -> ListItemMlcData(
                actionKey = typeName,
                id = typeName,
                label = UiText.StringResource(R.string.settings_share_with_friends),
                iconLeft = UiIcon.DrawableResource(CommonDiiaResourceIcon.SHARE.code),
                action = DataActionWrapper(type = typeName)
            )

            ContextMenuType.VEHICLE_RE_REGISTRATION.code -> ListItemMlcData(
                actionKey = typeName,
                id = typeName,
                label = UiText.StringResource(R.string.vehicle_re_registration),
                iconLeft = UiIcon.DrawableResource(CommonDiiaResourceIcon.WALLET.code),
                action = DataActionWrapper(type = typeName)
            )

            ContextMenuType.FOUNDING_REQUEST.code -> ListItemMlcData(
                actionKey = typeName,
                id = typeName,
                label = UiText.StringResource(R.string.dp_recovery_menu_go_to_founding_request),
                iconLeft = UiIcon.DrawableResource(CommonDiiaResourceIcon.WALLET.code),
                action = DataActionWrapper(type = typeName)
            )

            ContextMenuType.UPDATE_DOC.code -> ListItemMlcData(
                actionKey = typeName,
                id = typeName,
                label = UiText.StringResource(R.string.update_document),
                iconLeft = UiIcon.DrawableResource(CommonDiiaResourceIcon.REFRESH.code),
                action = DataActionWrapper(type = typeName)
            )


            //Dynamic items, data is fetched from web (api/v1/documents/manual)
//            ContextMenuType.BIRTH_CERTIFICATE, ContextMenuType.VACCINATION_CERTIFICATE, ContextMenuType.CHILD_VACCINATION_CERTIFICATE,
//            ContextMenuType.INTERNATIONAL_VACCINATION_CERTIFICATE, ContextMenuType.REQUEST_PROPER_USER_ASSIGNING, ContextMenuType.PENSION_CARD,
//            ContextMenuType.RESIDENCE_PERMIT_PERMANENT, ContextMenuType.RESIDENCE_PERMIT_TEMPORARY
            else -> throw Exception()
        }
    }

    private fun getVerificationCodesCount(document: Parcelable): Int {
        check(document is DiiaDocument)
        return document.verificationCodesCount()
    }
}