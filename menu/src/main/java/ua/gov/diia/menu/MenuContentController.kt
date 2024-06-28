package ua.gov.diia.menu

import androidx.compose.runtime.mutableStateListOf
import ua.gov.diia.core.models.common_compose.atm.SpacerAtmType
import ua.gov.diia.menu.ui.MenuActionsKey
import ua.gov.diia.ui_base.components.DiiaResourceIcon
import ua.gov.diia.ui_base.components.atom.button.BtnPrimaryDefaultAtmData
import ua.gov.diia.ui_base.components.atom.space.SpacerAtmData
import ua.gov.diia.ui_base.components.atom.text.textwithparameter.TextWithParametersConstants
import ua.gov.diia.ui_base.components.infrastructure.DataActionWrapper
import ua.gov.diia.ui_base.components.infrastructure.UIElementData
import ua.gov.diia.ui_base.components.infrastructure.addAllIfNotNull
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiIcon
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiText
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.toDynamicString
import ua.gov.diia.ui_base.components.molecule.list.ListItemMlcData
import ua.gov.diia.ui_base.components.molecule.text.TextLabelMlcData
import ua.gov.diia.ui_base.components.organism.list.ListItemGroupOrgData
import javax.inject.Inject

class MenuContentController @Inject constructor() {

    fun configureBody(isShowBadge: Boolean): List<UIElementData> {

        val menuUser: List<ListItemMlcData> = listOf(
            ListItemMlcData(
                id = MenuActionsKey.OPEN_NOTIFICATION,
                label = UiText.StringResource(R.string.settings_notifications),
                iconLeft = UiIcon.DrawableResource(
                    if (isShowBadge) {
                        DiiaResourceIcon.NEW_MESSAGE.code
                    } else {
                        DiiaResourceIcon.NOTIFICATION_MESSAGE.code
                    }
                ),
                action = DataActionWrapper(
                    type = MenuActionsKey.OPEN_NOTIFICATION
                ),
                componentId = UiText.StringResource(R.string.menu_item_message_test_tag)
            )
        )

        val menuSigning: List<ListItemMlcData> = listOf(
            ListItemMlcData(
                id = MenuActionsKey.OPEN_DIIA_ID,
                label = UiText.StringResource(R.string.settings_diia_id),
                iconLeft = UiIcon.DrawableResource(DiiaResourceIcon.KEY.code),
                action = DataActionWrapper(
                    type = MenuActionsKey.OPEN_DIIA_ID
                ),
                componentId = UiText.StringResource(R.string.menu_item_signature_test_tag)

            ),
            ListItemMlcData(
                id = MenuActionsKey.OPEN_SIGNE_HISTORY,
                label = UiText.StringResource(R.string.settings_signing_history),
                iconLeft = UiIcon.DrawableResource(DiiaResourceIcon.SOME_DOCS.code),
                action = DataActionWrapper(
                    type = MenuActionsKey.OPEN_SIGNE_HISTORY
                ),
                componentId = UiText.StringResource(R.string.menu_item_signature_history_test_tag)
            )
        )

        val menuSettings: List<ListItemMlcData> = listOf(
            ListItemMlcData(
                id = MenuActionsKey.OPEN_SETTINGS,
                label = UiText.StringResource(R.string.menu_settings),
                iconLeft = UiIcon.DrawableResource(DiiaResourceIcon.SETTINGS.code),
                action = DataActionWrapper(
                    type = MenuActionsKey.OPEN_SETTINGS
                ),
                componentId = UiText.StringResource(R.string.menu_item_settings_test_tag)
            ),
            ListItemMlcData(
                id = MenuActionsKey.OPEN_PLAY_MARKET,
                label = UiText.StringResource(R.string.settings_set_estimate_label),
                iconLeft = UiIcon.DrawableResource(DiiaResourceIcon.REFRESH.code),
                action = DataActionWrapper(
                    type = MenuActionsKey.OPEN_PLAY_MARKET
                ),
                componentId = UiText.StringResource(R.string.menu_item_update_app_test_tag)
            ),
            ListItemMlcData(
                id = MenuActionsKey.OPEN_APP_SESSIONS,
                label = UiText.StringResource(R.string.app_session_header),
                iconLeft = UiIcon.DrawableResource(DiiaResourceIcon.DEVICE.code),
                action = DataActionWrapper(
                    type = MenuActionsKey.OPEN_APP_SESSIONS
                ),
                componentId = UiText.StringResource(R.string.menu_item_connected_devices_test_tag)
            )
        )

        val menuSupport: List<ListItemMlcData> = listOf(
            ListItemMlcData(
                id = MenuActionsKey.OPEN_SUPPORT,
                label = UiText.StringResource(R.string.settings_support_label),
                iconLeft = UiIcon.DrawableResource(DiiaResourceIcon.MESSAGE.code),
                action = DataActionWrapper(
                    type = MenuActionsKey.OPEN_SUPPORT
                ),
                componentId = UiText.StringResource(R.string.menu_item_support_service_test_tag)
            ),
            ListItemMlcData(
                id = MenuActionsKey.COPY_DEVICE_UID,
                label = UiText.StringResource(R.string.settings_copy_device_id_label),
                iconLeft = UiIcon.DrawableResource(DiiaResourceIcon.COPY.code),
                action = DataActionWrapper(
                    type = MenuActionsKey.COPY_DEVICE_UID
                ),
                componentId = UiText.StringResource(R.string.menu_item_copy_device_number_test_tag)
            ),
            ListItemMlcData(
                id = MenuActionsKey.OPEN_FAQ,
                label = UiText.StringResource(R.string.settings_faq_label),
                iconLeft = UiIcon.DrawableResource(DiiaResourceIcon.FAQ.code),
                action = DataActionWrapper(
                    type = MenuActionsKey.OPEN_FAQ
                ),
                componentId = UiText.StringResource(R.string.menu_item_faq_test_tag)
            )
        )
        val linkText = "{app_policy}"
        val linkParameter =
            ua.gov.diia.ui_base.components.atom.text.textwithparameter.TextParameter(
                type = TextWithParametersConstants.TYPE_LINK,
                data = ua.gov.diia.ui_base.components.atom.text.textwithparameter.TextParameter.Data(
                    name = UiText.DynamicString("app_policy"),
                    alt = UiText.DynamicString("Повідомлення про обробку персональних даних"),
                    resource = UiText.DynamicString("https://diia.gov.ua/app_policy"),
                )
            )
        val bodyData = mutableStateListOf<UIElementData>()
        bodyData.addAllIfNotNull(
            ListItemGroupOrgData(itemsList = menuUser, componentId = UiText.StringResource(R.string.menu_group_personal_test_tag)),
            ListItemGroupOrgData(itemsList = menuSigning, componentId = UiText.StringResource(R.string.menu_group_signature_test_tag)),
            ListItemGroupOrgData(itemsList = menuSettings, componentId = UiText.StringResource(R.string.menu_group_settings_test_tag)),
            ListItemGroupOrgData(itemsList = menuSupport, componentId = UiText.StringResource(R.string.menu_group_support_test_tag)),
            SpacerAtmData(SpacerAtmType.SPACER_16),
            BtnPrimaryDefaultAtmData(
                id = MenuActionsKey.LOGOUT,
                actionKey = MenuActionsKey.LOGOUT,
                componentId = UiText.StringResource(R.string.menu_btn_primary_exit_test_tag),
                title = UiText.DynamicString("Вийти")
            ),
            SpacerAtmData(SpacerAtmType.SPACER_8),
            TextLabelMlcData(
                text = linkText.toDynamicString(),
                parameters = listOf(linkParameter),
                actionKey = MenuActionsKey.OPEN_POLICY,
                componentId = UiText.StringResource(R.string.menu_link_atm_personal_data_test_tag)
            ),
            SpacerAtmData(SpacerAtmType.SPACER_32)
        )
        return bodyData
    }

}