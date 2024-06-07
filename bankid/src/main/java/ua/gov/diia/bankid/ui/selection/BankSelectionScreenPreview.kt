package ua.gov.diia.bankid.ui.selection

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.tooling.preview.Preview
import ua.gov.diia.core.models.common_compose.atm.SpacerAtmType
import ua.gov.diia.core.ui.dynamicdialog.ActionsConst
import ua.gov.diia.ui_base.R
import ua.gov.diia.ui_base.components.DiiaResourceIcon
import ua.gov.diia.ui_base.components.atom.space.SpacerAtmData
import ua.gov.diia.ui_base.components.infrastructure.DataActionWrapper
import ua.gov.diia.ui_base.components.infrastructure.ServiceScreen
import ua.gov.diia.ui_base.components.infrastructure.UIElementData
import ua.gov.diia.ui_base.components.infrastructure.event.UIActionKeysCompose
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiIcon
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiText
import ua.gov.diia.ui_base.components.molecule.header.TitleGroupMlcData
import ua.gov.diia.ui_base.components.molecule.input.SearchInputV2Data
import ua.gov.diia.ui_base.components.molecule.list.ListItemMlcData
import ua.gov.diia.ui_base.components.molecule.message.StubMessageMlcData
import ua.gov.diia.ui_base.components.molecule.text.TextLabelMlcData
import ua.gov.diia.ui_base.components.organism.header.TopGroupOrgData
import ua.gov.diia.ui_base.components.organism.list.ListItemGroupOrgData
import ua.gov.diia.ui_base.components.organism.list.PlainListWithSearchOrganismData

@Composable
@Preview
fun BankSelectionScreenPreview() {
    val _toolbarData = remember { mutableStateListOf<UIElementData>() }
    val toolbarData: SnapshotStateList<UIElementData> = _toolbarData
    val _bodyData = remember { mutableStateListOf<UIElementData>() }
    val bodyData: SnapshotStateList<UIElementData> = _bodyData
    _toolbarData.add(
        TopGroupOrgData(
            titleGroupMlcData = TitleGroupMlcData(
                heroText = UiText.DynamicString("Оберіть свій банк"),
                leftNavIcon = TitleGroupMlcData.LeftNavIcon(
                    code = DiiaResourceIcon.BACK.code,
                    accessibilityDescription = UiText.StringResource(R.string.accessibility_back_button),
                    action = DataActionWrapper(
                        type = ActionsConst.ACTION_NAVIGATE_BACK,
                        subtype = null,
                        resource = null
                    )
                )
            )
        )
    )

    _bodyData.add(
        TextLabelMlcData(
            text = UiText.DynamicString("Для авторизації за допомогою BankID, оберіть свій банк та увійдіть до системи інтернет-банкінгу. Авторизація не передбачає жодного доступу до фінансової інформації.")
        )
    )

    val list =
        ListItemGroupOrgData(itemsList = SnapshotStateList<ListItemMlcData>().apply {
            add(
                ListItemMlcData(
                    label = UiText.DynamicString("Label"),
                    iconLeft = UiIcon.DrawableResource(DiiaResourceIcon.MESSAGE.code),
                )
            )
            add(
                ListItemMlcData(
                    label = UiText.DynamicString("Label"),
                    iconLeft = UiIcon.DrawableResource(DiiaResourceIcon.MESSAGE.code),
                )
            )
            add(
                ListItemMlcData(
                    label = UiText.DynamicString("Label"),
                    description = UiText.DynamicString("Description"),
                    iconLeft = UiIcon.DrawableResource(DiiaResourceIcon.MESSAGE.code),
                )
            )
            add(
                ListItemMlcData(
                    label = UiText.DynamicString("Label"),
                    description = UiText.DynamicString("Description"),
                    iconLeft = UiIcon.DrawableResource(DiiaResourceIcon.OUT_LINK.code),
                )
            )
        })
    _bodyData.add(
        PlainListWithSearchOrganismData(
            searchData = SearchInputV2Data(placeholder = UiText.DynamicString("Пошук")),
            fullList = list,
            displayedList = list,
            emptyListData = StubMessageMlcData(
                icon = UiText.DynamicString("\uD83D\uDD90"),
                title = UiText.DynamicString("На жаль, сталася помилка"),
                description = UiText.DynamicString("Перелік банків недоступний. Спробуйте трошки пізніше.")
            )
        )
    )
    _bodyData.add(SpacerAtmData(SpacerAtmType.SPACER_32))
    ServiceScreen(toolbar = toolbarData,
        body = bodyData,
        contentLoaded = Pair(UIActionKeysCompose.PAGE_LOADING_TRIDENT_WITH_BACK_NAVIGATION, true),
        onEvent = { })
}