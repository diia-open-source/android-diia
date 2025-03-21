package ua.gov.diia.ui_base.components.organism

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ua.gov.diia.core.models.common_compose.org.media.GroupFilesAddOrg
import ua.gov.diia.ui_base.components.DiiaResourceIcon
import ua.gov.diia.ui_base.components.atom.button.BtnPlainIconAtm
import ua.gov.diia.ui_base.components.atom.button.BtnPlainIconAtmData
import ua.gov.diia.ui_base.components.atom.button.toUiModel
import ua.gov.diia.ui_base.components.atom.divider.DividerSlimAtom
import ua.gov.diia.ui_base.components.infrastructure.UIElementData
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.event.UIActionKeysCompose
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiIcon
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiText
import ua.gov.diia.ui_base.components.molecule.list.ListItemMlc
import ua.gov.diia.ui_base.components.molecule.list.ListItemMlcData
import ua.gov.diia.ui_base.components.molecule.list.toUiModel
import ua.gov.diia.ui_base.components.noRippleClickable
import ua.gov.diia.ui_base.components.theme.BlackSqueeze
import ua.gov.diia.ui_base.components.theme.White

@Composable
fun GroupFilesAddOrg(
    modifier: Modifier = Modifier,
    data: GroupFilesAddOrgData,
    onUIAction: (UIAction) -> Unit
) {
    Column(modifier = modifier
        .padding(top = 24.dp, start = 24.dp, end = 24.dp,)
        .background(
            color = White, shape = RoundedCornerShape(8.dp)
        )) {

        data.materialItems?.let { items ->
            items.forEachIndexed { index, item ->
                ListItemMlc(
                    data = item,
                    onUIAction = {
                        onUIAction(
                            UIAction(
                                actionKey = data.actionKey,
                                data = it.data,
                                action = it.action
                            )
                        )
                    }
                )
                DividerSlimAtom(color = BlackSqueeze)
            }
        }
        Row(
            modifier = modifier
                .fillMaxWidth()
                .noRippleClickable {
                    if (data.materialItems?.size?.let { it < 10 } ?: true) {
                        onUIAction(
                            UIAction(
                                actionKey = data.actionKey,
                                action = data.button.action
                            )
                        )
                    }
                },
            horizontalArrangement = Arrangement.Center
        ) {
            BtnPlainIconAtm(
                modifier = modifier
                    .padding(vertical = 16.dp),
                data = data.button,

            ){
                onUIAction(
                    UIAction(
                        actionKey = data.actionKey,
                        action = data.button.action
                    )
                )
            }
        }
    }
}

data class GroupFilesAddOrgData(
    val actionKey: String = UIActionKeysCompose.FILE_UPLOAD_GROUP_ORG,
    val componentId: UiText? = null,
    val id: UiText? = null,
    val materialItems: SnapshotStateList<ListItemMlcData>? = null,
    val button: BtnPlainIconAtmData
) : UIElementData

fun GroupFilesAddOrg.toUiModel(): GroupFilesAddOrgData {
    val listItems: GroupFilesAddOrg = this
    return GroupFilesAddOrgData(
        componentId = this.componentId?.let {UiText.DynamicString(it)},
        id = this.id?.let {UiText.DynamicString(it)},
        materialItems = SnapshotStateList<ListItemMlcData>().apply {
            listItems.items?.let { list ->
                list.forEach { item ->
                    add(
                        item.listItemMlc.toUiModel()
                    )
                }
            }
        },
        button = btnPlainIconAtm.toUiModel()
    )
}

@Preview
@Composable
fun GroupFilesAddOrgPreview() {
    val data = GroupFilesAddOrgData(
        button = BtnPlainIconAtmData(
            id = "123",
            label = UiText.DynamicString("label"),
            icon = UiIcon.DrawableResource(DiiaResourceIcon.ADD.code)
        )
    )
    GroupFilesAddOrg(data = data) {}
}

@Preview
@Composable
fun GroupFilesAddOrgTestPreview() {
    val data = GroupFilesAddOrgData(
        button = BtnPlainIconAtmData(
            id = "123",
            label = UiText.DynamicString("Додати документ"),
            icon = UiIcon.DrawableResource(DiiaResourceIcon.ADD.code)
        ),
        materialItems = SnapshotStateList<ListItemMlcData>().apply {
            add(
                ListItemMlcData(
                    componentId = UiText.DynamicString("123"),
                    id = "123",
                    label = UiText.DynamicString("Документи для підтвердження права власності"),
                    description = UiText.DynamicString(
                        "Дата: 06 лютого 2024\n" +
                                "4 файли"
                    ),
                    iconRight = UiIcon.DrawableResource(DiiaResourceIcon.DELETE.code),
                )
            )
            add(
                ListItemMlcData(
                    componentId = UiText.DynamicString("124"),
                    id = "124",
                    label = UiText.DynamicString("Документи для підтвердження руйнувань"),
                    description = UiText.DynamicString(
                        "Дата: 06 лютого 2024\n" +
                                "2 файли"
                    ),
                    iconRight = UiIcon.DrawableResource(DiiaResourceIcon.DELETE.code),
                )
            )
        }
    )

    GroupFilesAddOrg(data = data) {}
}