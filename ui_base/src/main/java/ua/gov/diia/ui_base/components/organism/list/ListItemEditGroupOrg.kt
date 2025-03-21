package ua.gov.diia.ui_base.components.organism.list

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ua.gov.diia.core.models.common_compose.org.list.ListItemEditGroupOrg
import ua.gov.diia.ui_base.components.DiiaResourceIcon
import ua.gov.diia.ui_base.components.atom.button.BtnPlainIconAtm
import ua.gov.diia.ui_base.components.atom.button.BtnPlainIconAtmData
import ua.gov.diia.ui_base.components.atom.button.toUiModel
import ua.gov.diia.ui_base.components.atom.divider.DividerSlimAtom
import ua.gov.diia.ui_base.components.infrastructure.DataActionWrapper
import ua.gov.diia.ui_base.components.infrastructure.UIElementData
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.event.UIActionKeysCompose
import ua.gov.diia.ui_base.components.infrastructure.state.UIState
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiIcon
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiText
import ua.gov.diia.ui_base.components.molecule.list.ListItemMlcData
import ua.gov.diia.ui_base.components.theme.BlackAlpha50
import ua.gov.diia.ui_base.components.theme.BlackSqueeze
import ua.gov.diia.ui_base.components.theme.DiiaTextStyle
import ua.gov.diia.ui_base.components.theme.White

@Composable
fun ListItemEditGroupOrg(
    modifier: Modifier = Modifier,
    data: ListItemEditGroupOrgData,
    onUIAction: (UIAction) -> Unit
) {

    Column(modifier = modifier
        .padding(top = 24.dp, start = 24.dp, end = 24.dp,)
        .background(
            color = White, shape = RoundedCornerShape(8.dp)
        )) {

        data.title?.let {
            Text(
                modifier = Modifier.padding(top = 16.dp, start = 16.dp, end = 16.dp),
                text = it.asString(),
                style = DiiaTextStyle.t3TextBody
            )
        }
        data.description?.let {
            Text(
                modifier = Modifier.padding(top = 4.dp, bottom = 16.dp, start = 16.dp, end = 16.dp),
                text = it.asString(),
                style = DiiaTextStyle.t4TextSmallDescription,
                color = BlackAlpha50
            )
        }
// ITEMS LIST
        data.items?.let { list ->
            DividerSlimAtom(
                modifier = Modifier.padding(
                    top = if(data.description != null) 0.dp else 16.dp),
                color = BlackSqueeze)
            list.forEachIndexed { index, item ->
                ua.gov.diia.ui_base.components.molecule.list.ListItemMlc(
                    data = item,
                    onUIAction = {
                        onUIAction(
                            UIAction(
                                actionKey = data.actionKey,
                                data = it.data,
                                optionalId = data.id,
                                action = it.action
                            )
                        )
                    },
                )
                if (index != data.items.size - 1) {
                    DividerSlimAtom(color = BlackSqueeze)
                }
            }
        }

        DividerSlimAtom(
            modifier = Modifier.height(1.dp),
            color = BlackSqueeze
        )
// BTN ADD ITEM
        Row(
            modifier = modifier.padding(vertical = 16.dp)
                .fillMaxWidth()
                .clickable {
                    if (data.items?.size?.let { it < 10 } != false) {
                        onUIAction(
                            UIAction(
                                actionKey = data.actionKey,
                                action = data.button?.action
                            )
                        )
                    }
                },
            horizontalArrangement = Arrangement.Center
        ) {
            data.button?.let{
                BtnPlainIconAtm(
                    modifier = modifier,
                    data = data.button,

                    ) {
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
}

data class ListItemEditGroupOrgData(
    val actionKey: String = UIActionKeysCompose.MEDIA_UPLOAD_GROUP_ORG,
    val id: String? = null,
    val title: UiText? = null,
    val description: UiText? = null,
    val button: BtnPlainIconAtmData?,
    val items: SnapshotStateList<ListItemMlcData>? = null

) : UIElementData {
    fun setContent(list: List<ListItemMlcData>): ListItemEditGroupOrgData {
        val newItems = SnapshotStateList<ListItemMlcData>()
        newItems.addAll(list)
        return this.copy(
            items = if (newItems.isEmpty()) null else newItems,
        )
    }
}

fun ListItemEditGroupOrg.toUiModel(): ListItemEditGroupOrgData {
    return ListItemEditGroupOrgData(
        title = this.title?.let { UiText.DynamicString(it) },
        description = this.description?.let { UiText.DynamicString(it) },
        button = btnPlainIconAtm?.toUiModel()
    )
}

@Preview
@Composable
fun FileUploadGroupOrgPreview() {
    val data = ListItemEditGroupOrgData(
        title = UiText.DynamicString("title"),
        description = UiText.DynamicString("description"),
        button = BtnPlainIconAtmData(
            id = "123",
            label = UiText.DynamicString("label"),
            icon = UiIcon.DrawableResource(DiiaResourceIcon.ADD.code)
        )
    )

    ListItemEditGroupOrg(data = data) {}
}

@Preview
@Composable
fun FileTitleOrgEmptyPreview() {
    val data = ListItemEditGroupOrgData(
        title = UiText.DynamicString("Додайте файли"),
        description = UiText.DynamicString("Підтримуються PNG, JPG, HEIC формати файлів. Розмір файлу повинен бути не більше 100 Мб. Можна додати не більше 10 файлів."),
        button = BtnPlainIconAtmData(
            id = "123",
            label = UiText.DynamicString("Додати файли"),
            icon = UiIcon.DrawableResource(DiiaResourceIcon.ADD.code)
        ),
    )
    ListItemEditGroupOrg(data = data) {}
}

@Preview
@Composable
fun FileTitleOrgPreview() {
    val data = ListItemEditGroupOrgData(
        title = UiText.DynamicString("Актуалізуйте номери телефонів:"),
        button = BtnPlainIconAtmData(
            id = "123",
            label = UiText.DynamicString("Додати номер телефону"),
            icon = UiIcon.DrawableResource(DiiaResourceIcon.ADD.code)
        ),
        items = SnapshotStateList<ListItemMlcData>().apply {
            repeat(5) {
                add(
                    ListItemMlcData(
                        id = "123",
                        label = UiText.DynamicString("Label"),
                        iconRight = UiIcon.DrawableResource(DiiaResourceIcon.DELETE.code),
                        action = DataActionWrapper(
                            type = "type",
                            subtype = "subtype",
                            resource = "resource"
                        ),
                        interactionState = UIState.Interaction.Enabled
                    )
                )
            }
        },
    )
    ListItemEditGroupOrg(data = data) {}
}