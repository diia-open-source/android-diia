package ua.gov.diia.menu

import androidx.compose.runtime.mutableStateListOf
import ua.gov.diia.menu.ui.MenuActionsKey
import ua.gov.diia.ui_base.components.CommonDiiaResourceIcon
import ua.gov.diia.ui_base.components.atom.button.BtnPrimaryDefaultAtmData
import ua.gov.diia.ui_base.components.atom.space.SpacerAtmData
import ua.gov.diia.ui_base.components.atom.space.SpacerAtmType
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
import ua.gov.diia.ui_base.components.atom.text.textwithparameter.TextParameter
import javax.inject.Inject

class MenuContentController @Inject constructor() {

    fun configureBody(isShowBadge: Boolean): List<UIElementData> {

        val menuUser: List<ListItemMlcData> = listOf(
            ListItemMlcData(
                id = MenuActionsKey.OPEN_NOTIFICATION,
                label = UiText.StringResource(R.string.settings_notifications),
                iconLeft = UiIcon.DrawableResource(
                    if (isShowBadge) {
                        CommonDiiaResourceIcon.NEW_MESSAGE.code
                    } else {
                        CommonDiiaResourceIcon.NOTIFICATION_MESSAGE.code
                    }
                ),
                action = DataActionWrapper(
                    type = MenuActionsKey.OPEN_NOTIFICATION
                )
            )
        )

        val menuSigning: List<ListItemMlcData> = listOf(
            ListItemMlcData(
                id = MenuActionsKey.OPEN_DIIA_ID,
                label = UiText.StringResource(R.string.settings_diia_id),
                iconLeft = UiIcon.DrawableResource(CommonDiiaResourceIcon.KEY.code),
                action = DataActionWrapper(
                    type = MenuActionsKey.OPEN_DIIA_ID
                )
            ),
            ListItemMlcData(
                id = MenuActionsKey.OPEN_SIGN_HISTORY,
                label = UiText.StringResource(R.string.settings_signing_history),
                iconLeft = UiIcon.DrawableResource(CommonDiiaResourceIcon.SOME_DOCS.code),
                action = DataActionWrapper(
                    type = MenuActionsKey.OPEN_SIGN_HISTORY
                )
            )
        )

        val menuSettings: List<ListItemMlcData> = listOf(
            ListItemMlcData(
                id = MenuActionsKey.OPEN_SETTINGS,
                label = UiText.StringResource(R.string.menu_settings),
                iconLeft = UiIcon.DrawableResource(CommonDiiaResourceIcon.SETTINGS.code),
                action = DataActionWrapper(
                    type = MenuActionsKey.OPEN_SETTINGS
                )
            ),
            ListItemMlcData(
                id = MenuActionsKey.OPEN_PLAY_MARKET,
                label = UiText.StringResource(R.string.settings_set_estimate_label),
                iconLeft = UiIcon.DrawableResource(CommonDiiaResourceIcon.REFRESH.code),
                action = DataActionWrapper(
                    type = MenuActionsKey.OPEN_PLAY_MARKET
                )
            ),
            ListItemMlcData(
                id = MenuActionsKey.OPEN_APP_SESSIONS,
                label = UiText.StringResource(R.string.app_session_header),
                iconLeft = UiIcon.DrawableResource(CommonDiiaResourceIcon.DEVICE.code),
                action = DataActionWrapper(
                    type = MenuActionsKey.OPEN_APP_SESSIONS
                )
            )
        )

        val menuSupport: List<ListItemMlcData> = listOf(
            ListItemMlcData(
                id = MenuActionsKey.OPEN_SUPPORT,
                label = UiText.StringResource(R.string.settings_support_label),
                iconLeft = UiIcon.DrawableResource(CommonDiiaResourceIcon.MESSAGE.code),
                action = DataActionWrapper(
                    type = MenuActionsKey.OPEN_SUPPORT
                )
            ),
            ListItemMlcData(
                id = MenuActionsKey.COPY_DEVICE_UID,
                label = UiText.StringResource(R.string.settings_copy_device_id_label),
                iconLeft = UiIcon.DrawableResource(CommonDiiaResourceIcon.COPY.code),
                action = DataActionWrapper(
                    type = MenuActionsKey.COPY_DEVICE_UID
                )
            ),
            ListItemMlcData(
                id = MenuActionsKey.OPEN_FAQ,
                label = UiText.StringResource(R.string.settings_faq_label),
                iconLeft = UiIcon.DrawableResource(CommonDiiaResourceIcon.FAQ.code),
                action = DataActionWrapper(
                    type = MenuActionsKey.OPEN_FAQ
                )
            )
        )
        val linkText = "{app_policy}"
        val linkParameter =
            TextParameter(
                type = TextWithParametersConstants.TYPE_LINK,
                data = TextParameter.Data(
                    name = UiText.DynamicString("app_policy"),
                    alt = UiText.DynamicString("Повідомлення про обробку персональних даних"),
                    resource = UiText.DynamicString("https://diia.gov.ua/app_policy"),
                )
            )
        val bodyData = mutableStateListOf<UIElementData>()
        bodyData.addAllIfNotNull(
            ListItemGroupOrgData(itemsList = menuUser),
            ListItemGroupOrgData(itemsList = menuSigning),
            ListItemGroupOrgData(itemsList = menuSettings),
            ListItemGroupOrgData(itemsList = menuSupport),
            SpacerAtmData(SpacerAtmType.SPACER_16),
            BtnPrimaryDefaultAtmData(
                id = MenuActionsKey.LOGOUT,
                actionKey = MenuActionsKey.LOGOUT,
                title = UiText.DynamicString("Вийти")
            ),
            SpacerAtmData(SpacerAtmType.SPACER_8),
            TextLabelMlcData(
                text = linkText.toDynamicString(),
                parameters = listOf(linkParameter),
                actionKey = MenuActionsKey.OPEN_POLICY
            ),
            SpacerAtmData(SpacerAtmType.SPACER_32)
        )
        return bodyData
    }

}